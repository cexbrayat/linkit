# --- !Ups
ALTER TABLE Session add column benefits longtext;
ALTER TABLE Session add column format varchar(10) not null default 'Talk';
ALTER TABLE Session add column maxAttendees integer;
ALTER TABLE Session add column level varchar(20) not null default 'Experienced';
ALTER TABLE Session add column comment longtext;

# --- !Downs

ALTER TABLE Session drop column comment;
ALTER TABLE Session drop column level;
ALTER TABLE Session drop column maxAttendees;
ALTER TABLE Session drop column format;
ALTER TABLE Session drop column benefits;
