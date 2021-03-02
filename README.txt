Implemented:
	database interaction with the domain model
	REST API
	simple React UI with the list of wallets and their balances with the option to add / withdraw money from wallet
Optional implement:
	wallet to wallet transaction at Rest
Used:
	Spring Boot
	Reactjs
	Postgre database

Rest port: 8080
configuration: application.properties
dbcreation: schema-postgres.sql
React application port: 3000

Rest services:
	get /login?user_login=<login> = returns user by login
	get /user?user_id=<id> = returns user by id
	get /users = returns all users
	post /user/add = adds user
		Content-Type=application/json
		body: {"login": "someuser", "name": "MegaUser"}
	get /user/history?user_id=<id> = returns user history

	get /wallet?user_id=<id1>&wallet_id=<id2> = returns wallet by id
	get /wallets?user_id=<id> = returns user wallets
	post /wallet/add = adds wallet
		Content-Type=application/json
		body: {"userId": 1, "currencyCode": "USD"}
	get /wallet/history?wallet_id=<id> = returns wallet history

	post /money/add = adds sum to wallet
		Content-Type=application/json
		body: {"userId": 1, "walletId":1, "sum": "100"}
	post /money/remove = removes sum from wallet
		Content-Type=application/json
		body: {"userId": 1, "walletId":1, "sum": "15"}
	post /money/transfer = transfer money from one wallet to another
		Content-Type=application/json
		body: {"sourceUserId": 1, "sourceWalletId":1, "targetUserId": 2, "targetWalletId":2, "sum": "30"}


for any case simple commands for db/user/schema creation for postgre

CREATE ROLE java_user WITH
	LOGIN
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'ju123';

CREATE DATABASE "testDb"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

CREATE SCHEMA "testschema"
    AUTHORIZATION postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA "testschema"

GRANT ALL ON DATABASE "testDb" TO java_user;
GRANT ALL ON SCHEMA testschema TO java_user;
GRANT ALL ON TABLES TO java_user;