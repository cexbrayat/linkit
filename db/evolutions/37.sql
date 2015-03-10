# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'Stormshield', logoUrl = "/public/images/logo-stormshield.jpg", level = 'SILVER', publicProfile = true where lower(login) = 'stormshield';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('stormshield');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('stormshield')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('stormshield');
