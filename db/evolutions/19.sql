# --- !Ups
insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit14' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('zenika','objetdirect','woonoz');

# --- !Downs
delete from sponsor_events
where events = 'mixit14' and sponsor_id = (
    select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('zenika','objetdirect','woonoz')
);
