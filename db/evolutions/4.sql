# --- !Ups
ALTER TABLE Session add column event varchar(10) not null;

# --- !Downs
ALTER TABLE Session drop column event;