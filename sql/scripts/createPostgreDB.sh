#! /bin/bash
echo "creating db named ... "$USER"_DB"
createdb -h localhost -p $PGPORT $USER"_DB"
pg_ctl status

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_tables.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_indexes.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/load_data.sql