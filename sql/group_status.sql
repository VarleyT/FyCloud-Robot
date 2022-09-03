create table if not exists group_status
(
    id         int auto_increment comment '序号'
        primary key,
    group_code bigint(15)           not null comment '群号',
    status     tinyint(1) default 1 not null comment '启动状态',
    constraint group_status_group_code_uindex
        unique (group_code),
    constraint group_status_id_uindex
        unique (id)
)
    comment '群组机器人启动状态';


