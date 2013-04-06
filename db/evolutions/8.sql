# --- !Ups
alter table PlanedSlot modify id BIGINT NOT NULL AUTO_INCREMENT;

# --- !Downs
alter table PlanedSlot modify id BIGINT NOT NULL;
