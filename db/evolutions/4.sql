# --- !Ups
ALTER TABLE Session add column event varchar(10) not null;

# Badges : Attendee -> PreviousAttendee
update Member_Badges mb set mb.badges = 26 where mb.badges = 4;
commit;

# --- !Downs
ALTER TABLE Session drop column event;