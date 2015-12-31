#!/usr/bin/env bash

docker stop vpager-db && docker rm vpager-db && docker rmi fenrircyn/vpager-db
docker build -t fenrircyn/vpager-db .
docker run --name vpager --net=vpager-net -e POSTGRES_PASSWORD=N0t@Number -p 5432:5432 -d fenrircyn/vpager-db