#!/bin/bash
psql -h localhost -p $PGPORT $DB_NAME"_DB" < /extra/avala022/needed_files/CS166-Project-Phase3/sql/src/create_tables.sql > /dev/null
sleep 5

echo "Query time without indexes"
cat <(echo '\timing') /extra/avala022/needed_files/CS166-Project-Phase3/sql/src/view_queries.sql | psql -h localhost -p $PGPORT $DB_NAME"_DB" | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

psql -h localhost -p $PGPORT $DB_NAME"_DB" < /extra/avala022/needed_files/CS166-Project-Phase3/sql/src/create_indexes.sql > /dev/null

echo "Query time with indexes"
cat <(echo '\timing') /extra/avala022/needed_files/CS166-Project-Phase3/sql/src/view_queries.sql |psql -h localhost -p $PGPORT $DB_NAME"_DB" | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

