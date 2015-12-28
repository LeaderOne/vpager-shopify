#!/usr/bin/env bash

export PGUSER=postgres
echo "***CREATING DATABASE***"
psql < /tmp/create-vpager-tables.sql