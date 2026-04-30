cat <<EOF > .env
APP_DOMAIN=$APP_DOMAIN
SSL_EMAIL=$SSL_EMAIL
EOF

cat <<EOF > 99-autoreload.sh
# 99-autoreload.sh

#!/bin/sh
while :; do
    # Optional: Instead of sleep, detect config changes and only reload if necessary.
    sleep 6h
    nginx -t && nginx -s reload
done &
EOF

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

        server_name $APP_DOMAIN;


        location / {
            return 301 https://\$host\$request_uri;
        }

        location /.well-known/acme-challenge/ {
            root /var/www/certbot;
        }
    }

    server {
        listen 443 ssl;
        listen [::]:443 ssl;

        server_name $APP_DOMAIN;

        ssl_certificate     /etc/letsencrypt/live/$APP_DOMAIN/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/$APP_DOMAIN/privkey.pem;
        include /etc/letsencrypt/options-ssl-nginx.conf;
        ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

        location / {
            proxy_pass http://http_backend;

            proxy_set_header X-Url-Scheme \$scheme;
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
  # db:
  #   image: mysql:8.4.7
  #   environment:
  #     MYSQL_ROOT_PASSWORD: rootpassword
  #     MYSQL_DATABASE: OunDeDeDB
  #     MYSQL_USER: admin
  #     MYSQL_PASSWORD: password
  #   restart: unless-stopped
  #   networks:
  #     - app-network
  #   volumes:
  #     - mysql_data:/var/lib/mysql

  srv:
    image: dacnguyen9101/oun-de-de:$IMAGE_TAG
    restart: unless-stopped
    # depends_on:
    #   - db
    environment:
      GIT_SHA: $GIT_SHA
      DB_PASSWORD: $DB_PASSWORD
      DB_URL: $DB_URL
      DB_USRNAME: $DB_USRNAME
      JWT_SECRET: $JWT_SECRET
      # disabled when production
      CORS_CONFIG_PROPERTIES_ALLOWED_ORIGINS: "*"
    # CORS_CONFIG_PROPERTIES_ALLOWED_ORIGINS: "http://cdtphuhoi.com,https://cdtphuhoi.com"
    # SPRING_JPA_HIBERNATE_DDL_AUTO: create-drop
    networks:
      - app-network

  nginx:
    image: nginx:1.24.0
    restart: unless-stopped
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - srv
    environment:
      APP_DOMAIN: $APP_DOMAIN
      SSL_EMAIL: $SSL_EMAIL
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/certbot/conf:/etc/letsencrypt
      - ./nginx/certbot/www:/var/www/certbot
      - ./99-autoreload.sh:/docker-entrypoint.d/99-autoreload.sh
    networks:
      - app-network

  certbot:
    image: certbot/certbot:latest
    restart: unless-stopped
    volumes:
      - ./nginx/certbot/conf:/etc/letsencrypt
      - ./nginx/certbot/www:/var/www/certbot
    entrypoint: "/bin/sh -c 'trap exit TERM; while :; do certbot renew; sleep 12h & wait \$\${!}; done;'"

networks:
  app-network:
    driver: bridge

# volumes:
#   mysql_data:
EOF

docker compose down
docker compose pull
docker compose up -d
