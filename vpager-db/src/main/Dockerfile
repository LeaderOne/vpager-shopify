FROM postgres:9.4.5

ADD scripts/create-db.sh /docker-entrypoint-initdb.d/
ADD scripts/create-vpager-tables.sql /tmp