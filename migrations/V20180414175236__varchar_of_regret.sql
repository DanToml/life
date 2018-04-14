-- varchar_of_regret
--
-- Initial Database Migration

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- ^ this line needs to be run by a db superuser

CREATE TABLE providers (
  "id" uuid PRIMARY KEY DEFAULT (uuid_generate_v4()),
  "name" text UNIQUE NOT NULL,
)

CREATE TABLE visits (
  "id" uuid PRIMARY KEY DEFAULT (uuid_generate_v4()),
  "remote_id" text,
  "provider_id" uuid REFERENCES providers (id) NOT NULL,

  UNIQUE ("provider_id", "remote_id"),

  "display_name" text NOT NULL,
  "coordinates" point NOT NULL,
  "image_uri" text,
  "originated_at" timestamp NOT NULL,

  "data" jsonb,

  "created_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);
