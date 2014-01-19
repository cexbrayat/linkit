# --- !Ups
alter table SESSION add column ideaForNow longtext;

# --- !Downs
alter table SESSION drop column ideaForNow;
