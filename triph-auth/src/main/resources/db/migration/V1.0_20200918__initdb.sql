CREATE TABLE h_partment
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `partment_name`         varchar(200)    NOT NULL COMMENT '组织名称',
    `partment_code`         varchar(50)     NOT NULL COMMENT '组织编码',
    `status`                char(1)         DEFAULT '0' COMMENT '状态 0:正常 1:禁用 默认是0',
    `parent_id`             bigint          DEFAULT 0 COMMENT '上级组织id',
    `type`                  int             DEFAULT NULL COMMENT '组织类型',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    `create_name`           varchar(64)     DEFAULT NULL COMMENT '创建人',
    `update_time`           timestamp       DEFAULT NULL COMMENT '修改时间',
    `update_name`           varchar(64)     DEFAULT NULL COMMENT '修改人',
    `del_flag`              char(1)         DEFAULT '0' COMMENT '删除标记 0:未删除 1:删除 默认0 ',
    PRIMARY KEY (`id`)
) COMMENT ='组织表';

CREATE TABLE h_user_partment_rel
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`               bigint(20)      NOT NULL COMMENT '用户id',
    `partment_id`           bigint(20)      NOT NULL COMMENT '组织id',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='用户组织关系表';

CREATE TABLE h_role
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_name`             varchar(200)    NOT NULL COMMENT '角色名称',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    `create_name`           varchar(64)     DEFAULT NULL COMMENT '创建人',
    `update_time`           timestamp       DEFAULT NULL COMMENT '修改时间',
    `update_name`           varchar(64)     DEFAULT NULL COMMENT '修改人',
    `del_flag`              char(1)         DEFAULT '0' COMMENT '删除标记 0:未删除 1:删除 默认0 ',
    PRIMARY KEY (`id`)
) COMMENT ='角色表';

CREATE TABLE h_dep_role_rel
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `dept_id`               bigint(20)      NOT NULL COMMENT '组织id',
    `role_id`               bigint(20)      NOT NULL COMMENT '角色id',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='组织角色关系表';

CREATE TABLE h_auth
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `auth_name`             varchar(200)    NOT NULL COMMENT '权限名称',
    `auth_url`              varchar(200)    DEFAULT NULL COMMENT '路径',
    `parent_id`             bigint          DEFAULT 0 COMMENT '上级权限id',
    `sort`                  int             DEFAULT 0 COMMENT '排序',
    `grade`                 char(1)         DEFAULT 0 COMMENT '菜单级别 0:系统菜单 1:一级菜单 2:二级菜单 以此类推',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    `create_name`           varchar(64)     DEFAULT NULL COMMENT '创建人',
    `update_time`           timestamp       DEFAULT NULL COMMENT '修改时间',
    `update_name`           varchar(64)     DEFAULT NULL COMMENT '修改人',
    `del_flag`              char(1)         DEFAULT '0' COMMENT '删除标记 0:未删除 1:删除 默认0 ',
    PRIMARY KEY (`id`)
) COMMENT ='权限表';

CREATE TABLE h_role_auth_rel
(
    `id`                    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`               bigint(20)      NOT NULL COMMENT '角色id',
    `auth_id`               bigint(20)      NOT NULL COMMENT '权限id',
    `create_time`           timestamp       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT ='角色权限关系表';