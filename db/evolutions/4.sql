# --- !Ups
ALTER TABLE Session add column event varchar(10) not null;

# Badges : Attendee -> PreviousAttendee
update Member_Badges mb set mb.badges = 26 where mb.badges = 4;
commit;

# Member.ticketingRegistered = false
update Member m set m.ticketingRegistered = false;

# --- !Downs
ALTER TABLE Session drop column event;