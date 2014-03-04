# --- !Ups
update member set dtype = 'Sponsor', logoUrl = "/public/images/logo-mozilla.png", level = 'BRONZE', publicProfile = true where lower(login) = 'mozilla';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit14' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('mozilla');


# --- !Downs
delete from sponsor_events
where events = 'mixit14' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('mozilla')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) = 'mozilla';
