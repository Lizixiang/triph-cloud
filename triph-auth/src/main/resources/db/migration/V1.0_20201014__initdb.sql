CREATE TABLE h_user_auth_rel
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`               bigint(20)      NOT NULL COMMENT '用户id',
    `auth_id`               bigint(20)      NOT NULL COMMENT '权限id',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='用户权限关系表';