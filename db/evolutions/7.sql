# --- !Ups
CREATE TABLE planedslot
(
  id BIGINT PRIMARY KEY NOT NULL,
  event VARCHAR(255) NOT NULL,
  slot VARCHAR(255) NOT NULL,
  talk_id BIGINT NOT NULL,
  FOREIGN KEY ( talk_id ) REFERENCES session ( id ),
  UNIQUE KEY planedslot_UK1 (event, slot),
  UNIQUE KEY planedslot_UK2 (event, talk_id)
);

# --- !Downs
drop table planedslot;
