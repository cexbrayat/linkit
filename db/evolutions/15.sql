# --- !Ups
update Member m set m.DTYPE = 'Member' where m.login = 'vincent.daviet';

# --- !Downs
# No use