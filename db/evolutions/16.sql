# --- !Ups
alter table Talk add column ideaForNow int(11);

# --- !Downs
alter table Talk drop column ideaForNow;
