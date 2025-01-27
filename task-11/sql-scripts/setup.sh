DB_USER="root"
DB_PASSWORD="root"

echo "Creating database structure..."
mysql -u"$DB_USER" -p"$DB_PASSWORD" < DDL.sql

echo "Populating database with data..."
mysql -u"$DB_USER" -p"$DB_PASSWORD" < DML.sql

echo "Database setup completed!"
