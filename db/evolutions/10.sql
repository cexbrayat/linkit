# --- !Ups
update Member m set m.DTYPE = 'Staff' where m.login = 'mpetitdant';

# --- !Downs
update Member m set m.DTYPE = 'Member' where m.login = 'mpetitdant';
