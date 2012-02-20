CREATE TABLE `session_fan` (
  `favs_id` bigint(20) NOT NULL,
  `fans_id` bigint(20) NOT NULL,
  PRIMARY KEY (`favs_id`,`fans_id`),
  KEY `FKFEAE01EA438CC8F4` (`favs_id`),
  KEY `FKFEAE01EA6F3188B8` (`fans_id`),
  CONSTRAINT `FKFEAE01EA6F3188B8` FOREIGN KEY (`fans_id`) REFERENCES `Member` (`id`),
  CONSTRAINT `FKFEAE01EA438CC8F4` FOREIGN KEY (`favs_id`) REFERENCES `Session` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$