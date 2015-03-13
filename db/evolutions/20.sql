# --- !Ups
update member set dtype = 'Sponsor', logoUrl = "/public/images/logo-steria.png", level = 'SILVER', publicProfile = true where lower(login) = 'steria';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit14' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('steria');


# --- !Downs
delete from sponsor_events
where events = 'mixit14' and sponsor_id = (
    select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('steria')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) = 'steria';
