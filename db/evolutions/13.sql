# --- !Ups
update Member m set m.DTYPE = 'Staff' where m.login = 'vincent.daviet';

# --- !Downs
update Member m set m.DTYPE = 'Member' where m.login = 'vincent.daviet';
