# --- !Ups
alter table session add column guest bit default false;
update session set guest=true where id in (select distinct session_id
from comment where lower(comment.content) like '%guest speaker%');

# --- !Downs
alter table talk drop column guest;
