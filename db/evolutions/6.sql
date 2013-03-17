# --- !Ups
ALTER TABLE Session add column lang varchar(2);

# --- !Downs

ALTER TABLE Session drop column lang;
