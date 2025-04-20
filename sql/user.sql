-- 用户
drop table if exists `user`;
create table `user`
(
    `id`         bigint primary key,
    `name`       varchar(20) null COMMENT '用户名',
    `email`      varchar(50) null COMMENT '邮箱',
    `password`   varchar(50) null COMMENT '密码',
    `mobile`     varchar(11) not null comment '手机号',
    `created_at` datetime(3) null comment '创建时间', -- 精度为 3 位小数秒，表示可以存储到毫秒级别。
    `updated_at` datetime(3) null comment '更新时间', -- 例如，2025-01-01 12:34:56.789
    unique key `mobile_unique` (`mobile`)
) comment ='用户';


-- sb
drop table if exists `sb`;
create table `sb`
(
    `id`         bigint primary key,
    `name`       varchar(20) null COMMENT '傻逼用户名|searchable',
    `email`      varchar(50) null COMMENT '傻逼邮箱|searchable',
    `password`   varchar(50) null COMMENT '傻逼密码',
    `mobile`     varchar(11) not null comment '傻逼手机号',
    `created_at` datetime(3) null comment '傻逼创建时间', -- 精度为 3 位小数秒，表示可以存储到毫秒级别。
    `updated_at` datetime(3) null comment '傻逼更新时间', -- 例如，2025-01-01 12:34:56.789
    unique key `mobile_unique` (`mobile`)
) comment ='傻逼';



-- course
drop table if exists `course`;
create table `course`
(
    `id`         bigint primary key,
    `name`       varchar(20) null COMMENT '课程名|searchable',
    `description`      varchar(50) null COMMENT '课程描述|searchable',
    `created_at` datetime(3) null comment '课程创建时间', -- 精度为 3 位小数秒，表示可以存储到毫秒级别。
    `updated_at` datetime(3) null comment '课程更新时间' -- 例如，2025-01-01 12:34:56.789
) comment ='课程';

-- 乘车人/乘客
-- 乘车人不一定是网站的会员。比如你给爷爷奶奶等买票，他们只是单纯的乘车人身份
drop table if exists `passenger`;
create table `passenger`
(
    `id`          bigint      not null comment 'id',
    `user_id`     bigint      not null comment '会员id',
    `name`        varchar(20) not null comment '姓名|searchable',
    `id_card`     varchar(18) not null comment '身份证号',
    `mobile`      varchar(11) comment '手机号',
    `type`        char(1)     not null comment '乘客类型|枚举[PassengerType]',
    `created_at` datetime(3) not null comment '创建时间',
    `updated_at` datetime(3) not null comment '更新时间',
    primary key (`id`),
    key `index_user_id` (`user_id`)
) default charset = utf8mb4 comment ='乘车人';