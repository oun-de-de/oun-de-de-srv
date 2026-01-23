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

        location / {
            proxy_pass http://http_backend;
            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto \$scheme;
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
      MYSQL_DATABASE: UserDB
      MYSQL_USER: admin
      MYSQL_PASSWORD: Admin123
    restart: unless-stopped
    networks:
      - app-network

  srv:
    image: dacnguyen9101/oun-de-de:$IMAGE_TAG
    depends_on:
      - db
    environment:
      GIT_SHA: $GIT_SHA
    restart: unless-stopped
    networks:
      - app-network

  nginx:
    image: nginx:1.24.0
    ports:
      - "80:80"
    depends_on:
      - srv
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
EOF

docker compose down
docker compose pull
docker compose up -d
