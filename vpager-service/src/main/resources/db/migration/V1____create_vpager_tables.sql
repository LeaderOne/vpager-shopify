CREATE SCHEMA ticketing;
GRANT ALL PRIVILEGES ON SCHEMA ticketing TO vpagerowner;

CREATE ROLE standarduser;
CREATE USER vpageruser WITH PASSWORD 'vpageruser';

CREATE TABLE ticketing.merchant
(
  merchant_id SERIAL PRIMARY KEY,
  now_serving INT DEFAULT 0,
  shopify_shop_url TEXT UNIQUE
);

CREATE TABLE ticketing.ticket
(
  ticket_id SERIAL PRIMARY KEY,
  merchant_id INT REFERENCES ticketing.merchant(merchant_id) NOT NULL,
  shopify_order_id BIGINT UNIQUE,
  create_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE ticketing.ticket OWNER TO vpagerowner;
ALTER TABLE ticketing.merchant OWNER TO vpagerowner;

GRANT standarduser TO vpageruser;

GRANT SELECT, UPDATE, INSERT, DELETE ON ALL TABLES IN SCHEMA ticketing TO standarduser;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA ticketing TO standarduser;
GRANT USAGE ON SCHEMA ticketing TO standarduser;

GRANT CONNECT ON DATABASE vpager TO standarduser;