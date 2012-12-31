# --- !Ups
ALTER TABLE Session add column event varchar(10) not null default 'mixit12';

# Badges : Attendee -> PreviousAttendee
update Member_Badges mb set mb.badges = 26 where mb.badges = 4;
commit;
# Ensure registered 2012 attendees have Mix-IT 2012 badge
insert into member_badges(Member_id, badges)
select m.id, 26
from member m
where m.ticketingRegistered = true
and not exists (select mb.badges from member_badges mb where mb.Member_id = m.id and mb.badges = 26);

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