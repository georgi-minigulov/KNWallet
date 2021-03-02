CREATE TABLE IF NOT EXISTS testschema.t_users(
	id serial PRIMARY KEY, 
	login VARCHAR(255) UNIQUE, 
	name VARCHAR(255), 
	created timestamp DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS testschema.t_wallets(
	id serial PRIMARY KEY, 
	user_id integer not null,
	balance decimal(10, 2), 
	currency_code VARCHAR(20), 
	created timestamp DEFAULT current_timestamp,
	UNIQUE(user_id, currency_code),
	CONSTRAINT fk_user
      FOREIGN KEY(user_id) 
	  REFERENCES testschema.t_users(id)
);

CREATE TABLE IF NOT EXISTS testschema.t_history(
	id serial PRIMARY KEY, 
	action_type varchar(50),
	source_name varchar(50),
	source_id integer,
	target_name varchar(50),
	target_id integer,
	message varchar(255),
	value varchar(50),
	created timestamp DEFAULT current_timestamp
);