#!/usr/bin/env bash

docker run -p 8080:8080 --name=vpager-service --net=vpager-net -t fenrircyn/vpager-service