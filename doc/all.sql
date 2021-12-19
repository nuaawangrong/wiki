DROP TABLE IF EXISTS `test`;
create table `test` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) comment '名称',
                        `password` varchar(50) comment '密码',
                        primary key (`id`)
)engine=innodb default char set=utf8mb4 comment='测试';

INSERT INTO `test` (id,name,password) VALUES(1,'测试','password');
