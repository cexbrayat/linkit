# --- !Ups
create table `speakerpreferences` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` varchar(10) not null,
  `speaker_id` bigint(20) not null,
  `additionalDetails` longtext DEFAULT NULL,
  `additionalHotelDetails` longtext DEFAULT NULL,
  `transportationType` varchar(10) DEFAULT NULL,
  `arrivalTime` datetime DEFAULT NULL,
  `departureTime` datetime DEFAULT NULL,
  `arrivalPlace` varchar(255) DEFAULT NULL,
  `departurePlace` varchar(255) DEFAULT NULL,
  `pickup` bit default false,
  `eveningBefore` bit default false,
  `eveningDuring` bit default false,
  `eveningAfter` bit default false,
  `hotelNightBefore` bit default false,
  `hotelNightDuring` bit default false,
  `hotelNightAfter` bit default false,
  `presenceFirstAM` bit default false,
  `presenceFirstPM` bit default false,
  `presenceSecondAM` bit default false,
  `presenceSecondPM` bit default false,
  PRIMARY KEY (`id`),
  UNIQUE KEY `speaker_event_UK` (`event`,`speaker_id`),
  KEY `speaker_FK` (`speaker_id`),
  CONSTRAINT `member_FK2` FOREIGN KEY (`speaker_id`) REFERENCES `member` (`id`)
);

# --- !Downs
DROP TABLE speakerpreferences;