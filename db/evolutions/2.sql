# --- !Ups

create table Setting (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `member_id` bigint(20) default null,
    `notificationOption` varchar(255),
    `timezone` varchar(255),
    PRIMARY KEY (`id`),
    KEY `member_FK` (`member_id`),
    CONSTRAINT `member_FK` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);

insert into Setting(member_id, notificationOption, timezone)
select id, notificationOption, 'Europe/Paris'
from Member;
COMMIT;

alter table Member
drop column notificationOption;

# --- !Downs

alter table Member
add column notificationOption varchar(255);

update Member m set m.notificationOption = (select notificationOption from Setting s where s.member_id = m.id);
COMMIT;

drop table Setting;