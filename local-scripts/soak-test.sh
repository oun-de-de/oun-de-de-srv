#!/bin/bash

URL="http://localhost:8080/api/v1/customers?page=0&size=1000"
AUTH="Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0OTEwMSIsImlhdCI6MTc3MjIxNTE0MiwiZXhwIjoxNzcyMjE4NzQyfQ.SpPJylwNVylzf6NEkbypDGjYhLzlug30H47kYq4nY_I"

END_TIME=$((SECONDS + 3600))

while [ $SECONDS -lt $END_TIME ]; do
  for i in {1..20}; do
    curl -s -o /dev/null \
      -H "accept: */*" \
      -H "$AUTH" \
      "$URL" &
  done
  wait
  sleep 1
done