# --- !Ups
update member set dtype = 'Sponsor', logoUrl = "/public/images/logo-serli.png", level = 'BRONZE', publicProfile = true where lower(login) = 'serlifr';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit14' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('serlifr');


# --- !Downs
delete from sponsor_events
where events = 'mixit14' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('serlifr')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) = 'serlifr';
