# --- !Ups
ALTER TABLE Session add column event varchar(10) not null default 'mixit12';

# Badges : Attendee -> PreviousAttendee
update Member_Badges mb set mb.badges = 26 where mb.badges = 4;
commit;

# Member.ticketingRegistered = false
update Member m set m.ticketingRegistered = false;
commit;

# Sponsors

create table sponsor_events (
  `sponsor_id` bigint(20) NOT NULL,
  `events` varchar(255) NOT NULL,
  KEY `sponsor_FK` (`sponsor_id`),
  CONSTRAINT `sponsor_FK` FOREIGN KEY (`sponsor_id`) REFERENCES `member` (`id`)
);

insert into sponsor_events(sponsor_id, events)
select s.id, 'mixit12'
from member s
where s.DTYPE = 'Sponsor';

# --- !Downs
drop table sponsor_events;

update Member_Badges mb set mb.badges = 4 where mb.badges = 26;
commit;

ALTER TABLE Session drop column event;