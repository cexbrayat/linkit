# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'Linky ERDF', logoUrl = "/public/images/logo-erdf-linky.png", level = 'BRONZE', publicProfile = true where lower(login) = 'erdf-linky';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('erdf-linky');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('erdf-linky')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('erdf-linky');
