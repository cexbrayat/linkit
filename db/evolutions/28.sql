# --- !Ups
update Member m set m.DTYPE = 'Staff', m.publicProfile = true where m.login in ('sdeleuze', 'guillaumeehret', 'ph.charriere', 'gregalexandre');

# --- !Downs
update Member m set m.DTYPE = 'Member' where m.login in ('sdeleuze', 'guillaumeehret', 'ph.charriere', 'gregalexandre');
