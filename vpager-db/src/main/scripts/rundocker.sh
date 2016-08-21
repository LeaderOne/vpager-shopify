#!/usr/bin/env bash

docker run --name vpager -h vpager --net=vpager-net -e POSTGRES_PASSWORD=N0t@Number -p 5432:5432 -d leaderone/fenrircyndb
