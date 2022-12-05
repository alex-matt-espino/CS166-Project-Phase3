#!/bin/bash
psql -h localhost -p $PGPORT $USER"_DB" < ../src/create_tables.sql > /dev/null
sleep 5

echo "Query time without indexes"
cat <(echo '\timing') ../src/view_queries.sql | psql -h localhost -p $PGPORT $USER"_DB" | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

psql -h localhost -p $PGPORT $USER"_DB" < ../src/create_indexes.sql > /dev/null

echo "Query time with indexes"
cat <(echo '\timing') ../src/view_queries.sql |psql -h localhost -p $PGPORT $USER"_DB" | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

