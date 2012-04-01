# --- !Ups

ALTER TABLE Member add column level varchar(50);

# --- !Downs
ALTER TABLE Member drop column level;