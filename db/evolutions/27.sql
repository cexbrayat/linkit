# --- !Ups
update member set dtype = 'Sponsor', firstname = null, lastname = 'VersionOne', logoUrl = "/public/images/logo-versionone.jpg", level = 'SILVER', publicProfile = true where lower(login) = 'versionone';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Atlassian', logoUrl = "/public/images/logo-atlassian.png", level = 'SILVER', publicProfile = true where lower(login) = 'atlassian';
update member set dtype = 'Sponsor', firstname = null, lastname = 'Red Hat', logoUrl = "/public/images/logo-redhat.jpg", level = 'BRONZE', publicProfile = true where lower(login) = 'redhat';
update member set dtype = 'Sponsor', firstname = null, lastname = 'MailJet', logoUrl = "/public/images/logo-mailjet.png", level = 'BRONZE', publicProfile = true where lower(login) = 'mailjet';

insert into sponsor_events(sponsor_id, events)
  select m.id, 'mixit14' from member m
  where m.dtype = 'Sponsor' and lower(m.login) IN ('versionone','atlassian','redhat','mailjet');


# --- !Downs
delete from sponsor_events
where events = 'mixit14' and sponsor_id = (
  select m.id from member m where m.dtype = 'Sponsor' and lower(m.login) IN ('versionone','atlassian','redhat','mailjet')
);

update member set dtype = 'Member', logoUrl = NULL where lower(login) IN ('versionone','atlassian','redhat','mailjet');
