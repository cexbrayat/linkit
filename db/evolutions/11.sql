# --- !Ups

alter table Member
  add column publicProfile bit default false;
update Member m set m.publicProfile = true where m.DTYPE IN ('Staff', 'Sponsor');
update member m set m.publicProfile = true where m.id IN (
    select distinct sm.speakers_id
    from session_member sm, session s
    where s.id = sm.sessions_id
    and s.valid = true
);
COMMIT;

# --- !Downs
alter table member
  drop column publicProfile;
