# --- !Ups
update Member m set m.DTYPE = 'Member' where m.login in ('julien', 'mathieu');

# --- !Downs
update Member m set m.DTYPE = 'Staff' where m.login in ('julien', 'mathieu');
