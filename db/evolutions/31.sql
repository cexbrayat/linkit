# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'Viseo', logoUrl = "/public/images/logo-viseo.png", level = 'SILVER', publicProfile = true where lower(login) = 'viseo';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Neosoft', logoUrl = "/public/images/logo-neosoft.png", level = 'SILVER', publicProfile = true where lower(login) = 'neosoftlyon';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Woonoz', logoUrl = "/public/images/logo-woonoz.png", level = 'BRONZE', publicProfile = true where lower(login) = 'woonoz';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Fiducial', logoUrl = "/public/images/logo-fiducial.png", level = 'BRONZE', publicProfile = true where lower(login) = 'fiducial';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('viseo','woonoz','neosoftlyon','fiducial');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('viseo','woonoz','neosoftlyon','fiducial')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('viseo','woonoz','neosoftlyon','fiducial');
