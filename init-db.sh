#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRESQL_USERNAME" --dbname="$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE driver_db;
    CREATE DATABASE rating_db;
EOSQL
