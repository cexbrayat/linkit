# --- !Ups
alter table Comment
  add column private bit default false;

# --- !Downs
alter table Comment
  drop column private;
