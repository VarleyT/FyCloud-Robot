create table if not exists group_message
(
    id          int auto_increment comment '序号'
        primary key,
    group_code  bigint(15)   not null comment '群号',
    group_name  varchar(255) not null comment '群名',
    sender_code bigint(15)   not null comment '发送人号码',
    sender_name varchar(255) not null comment '发送人名字',
    send_time   varchar(255) not null comment '发送时间',
    msg_content text         not null comment '消息正文'
)
    comment '群消息存储' collate = utf8mb4_bin;


