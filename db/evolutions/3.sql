# --- !Ups

ALTER TABLE Member add column level int(2);

# --- !Downs
ALTER TABLE Member drop column level;