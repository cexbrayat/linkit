# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'SII Rhône-Alpes', logoUrl = "/public/images/logo-sii.png", level = 'BRONZE', publicProfile = true where lower(login) = 'sii_rhonealpes';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Sopra Steria', logoUrl = "/public/images/logo-soprasteria.png", level = 'BRONZE', publicProfile = true where lower(login) = 'sopra steria';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Google', logoUrl = "/public/images/logo-google.png", level = 'BRONZE', publicProfile = true where lower(login) = 'google';
update member set dtype = 'Sponsor', firstname = null, lastname = 'CGI', logoUrl = "/public/images/logo-cgi.jpg", level = 'BRONZE', publicProfile = true where lower(login) = 'cgi';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit15' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('sii_rhonealpes','sopra steria','google','cgi');


# --- !Downs
delete from sponsor_events
where events = 'mixit15' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('sii_rhonealpes','sopra steria','google','cgi')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('sii_rhonealpes','sopra steria','google','cgi');
