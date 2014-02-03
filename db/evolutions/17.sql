# --- !Ups
alter table SESSION add column feedback bit default false;

# --- !Downs
alter table SESSION drop column feedback;

