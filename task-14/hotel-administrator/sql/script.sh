#!/bin/bash

DB_USER="your_username"
DB_PASSWORD="your_password"
DB_NAME="your_database"
DB_HOST="localhost"

mysql -u "$DB_USER" -p"$DB_PASSWORD" -h "$DB_HOST" "$DB_NAME" < DDL.sql

if [ $? -ne 0 ]; then
    echo "Error executing DDL.sql"
    exit 1
fi

mysql -u "$DB_USER" -p"$DB_PASSWORD" -h "$DB_HOST" "$DB_NAME" < DML.sql

if [ $? -ne 0 ]; then
    echo "Error executing DML.sql"
    exit 1
fi

echo "Scripts executed successfully!"
