# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'XebiaLabs', logoUrl = "/public/images/logo-xebialabs.png", level = 'SILVER', publicProfile = true where lower(login) = 'xebialabs';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Alfene', logoUrl = "/public/images/logo-alfene.png", level = 'BRONZE', publicProfile = true where lower(login) = 'alfene';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('xebialabs','alfene');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('xebialabs','alfene')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('xebialabs','alfene');
