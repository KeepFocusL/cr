drop table if exists `station`;
create table `station`
(
    `id`          bigint      not null comment 'id',
    `name`        varchar(20) not null comment '站名',
    `name_pinyin` varchar(50) not null comment '站名拼音',
    `name_py`     varchar(50) not null comment '站名拼音首字母',
    `created_at` datetime(3) null comment '创建时间', -- 精度为 3 位小数秒，表示可以存储到毫秒级别。
    `updated_at` datetime(3) null comment '更新时间', -- 例如，2025-01-01 12:34:56.789
    primary key (`id`),
    unique key `name_unique` (`name`)
) comment ='车站';