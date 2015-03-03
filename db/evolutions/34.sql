# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'Enalean', logoUrl = "/public/images/logo-enalean.png", level = 'SILVER', publicProfile = true where lower(login) = 'enalean';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('enalean');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('enalean')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('enalean');
