# --- !Ups

alter table activity add column level int(11);
alter table activity DROP column important;
update activity set level=2 where DTYPE like 'Comment%';
update activity set level=2 where DTYPE like 'Earn%';
update activity set level=3 where DTYPE like 'Link%';
update activity set level=4 where DTYPE like 'Look%';
update activity set level=1 where DTYPE like 'New%';
update activity set level=2 where DTYPE like 'NewVote%';
update activity set level=2 where DTYPE like 'Shared%';
update activity set level=2 where DTYPE like 'Earn%';
update activity set level=2 where DTYPE like 'Sign%';
update activity set level=3 where DTYPE like 'Status%';
update activity set level=3 where DTYPE like 'UpdateProfile%';
update activity set level=1 where DTYPE like 'UpdateSession%';
COMMIT;

create table GeneralParameter
add column entry varchar(255) not null,
add column entry varchar(255);

# --- !Downs

drop table GeneralParameter;

alter table activity DROP column level;
alter table activity add column important bit(1);

update activity set important=true where DTYPE like 'Comment%';
update activity set important=false where DTYPE like 'Look%';
COMMIT;
