<!-- After installation you need to add path to environment variable -->
<!-- In start, environment variables => environment variables => path edit => new C:\Program Files\PostgreSQL\15\bin or another version -->

<!-- To connect from bash -->
<!-- then enter password from installation -->
psql username=postgres 

<!-- By default you will be connected to postgres db, you can specify another db -->
psql username=postgres dbname=test



<!-- AFTER CONNECT -->
\c test <!-- connect to db test -->
\l <!-- show dbs list -->
\d <!-- show tables into connected db -->
\q <!-- quit from psql -->



pip install 