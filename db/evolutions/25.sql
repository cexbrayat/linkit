# --- !Ups
update member set lastname = CONCAT(firstname, ' ', lastname), firstname = null where DTYPE = 'Sponsor' and length(firstname) > 0;

# --- !Downs
# No down possible, Up script is destructive