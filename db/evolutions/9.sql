# --- !Ups
alter table Mailing add email varchar(255);

# --- !Downs
alter table Mailing drop column email;
