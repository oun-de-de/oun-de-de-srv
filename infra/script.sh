cat <<EOF > nginx.conf
worker_processes auto;

events {
    worker_connections 2048;
}

http {
    upstream http_backend {
       server srv:8080;
    }

    server {
        listen 80;
        listen [::]:80;

        server_name oun-de-de-srv.cloud;

        return 301 https://\$host\$request_uri;
    }

    server {
        listen 443 ssl;
        listen [::]:443 ssl;

        server_name oun-de-de-srv.cloud;

        ssl_certificate     /etc/letsencrypt/live/oun-de-de-srv.cloud/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/oun-de-de-srv.cloud/privkey.pem;

        location / {
            proxy_pass http://http_backend;

            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto https;
        }
    }
}
EOF

cat <<EOF > docker-compose.yaml
services:
  db:
    image: mysql:8.4.7
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: OunDeDeDB
      MYSQL_USER: admin
      MYSQL_PASSWORD: password
    # restart: unless-stopped
    networks:
      - app-network

  srv:
    image: dacnguyen9101/oun-de-de:$IMAGE_TAG
    depends_on:
      - db
    environment:
      GIT_SHA: $GIT_SHA
      DB_PASSWORD: $DB_PASSWORD
      DB_URL: $DB_URL
      DB_USRNAME: $DB_USRNAME
      JWT_SECRET: $JWT_SECRET
    # SPRING_JPA_HIBERNATE_DDL_AUTO: create-drop
    # restart: unless-stopped
    networks:
      - app-network

  nginx:
    image: nginx:1.24.0
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - srv
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - /etc/letsencrypt/live/oun-de-de-srv.cloud/fullchain.pem:/etc/letsencrypt/live/oun-de-de-srv.cloud/fullchain.pem:ro
      - /etc/letsencrypt/live/oun-de-de-srv.cloud/privkey.pem:/etc/letsencrypt/live/oun-de-de-srv.cloud/privkey.pem:ro
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
EOF

docker compose down
docker compose pull
docker compose up -d
