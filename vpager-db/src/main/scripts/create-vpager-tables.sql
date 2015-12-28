CREATE USER vpagerowner WITH PASSWORD '1WBbg625PG%';
CREATE DATABASE vpager;

GRANT ALL PRIVILEGES ON DATABASE vpager TO vpagerowner;
REVOKE ALL ON DATABASE vpager FROM PUBLIC;

CREATE ROLE standarduser;
CREATE USER vpageruser WITH PASSWORD 'vpageruser';

\connect vpager
CREATE SCHEMA ticketing;
GRANT ALL PRIVILEGES ON SCHEMA ticketing TO vpagerowner;
ALTER SCHEMA ticketing OWNER TO vpagerowner;

CREATE TABLE ticketing.merchant
(
  merchant_id SERIAL PRIMARY KEY,
  now_serving INT
);

CREATE TABLE ticketing.ticket
(
  ticket_id SERIAL,
  merchant_id INT REFERENCES ticketing.merchant(merchant_id),
  create_ts TIMESTAMP,
  PRIMARY KEY (ticket_id, merchant_id)
);

ALTER TABLE ticketing.ticket OWNER TO vpagerowner;
ALTER TABLE ticketing.merchant OWNER TO vpagerowner;

GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA ticketing TO standarduser;
GRANT USAGE ON SCHEMA ticketing TO standarduser;

GRANT CONNECT ON DATABASE vpager TO standarduser;
GRANT standarduser TO vpageruser;