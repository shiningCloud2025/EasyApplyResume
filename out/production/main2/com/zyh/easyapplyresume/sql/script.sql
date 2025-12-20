create table admin_admin
(
    admin_id         int auto_increment comment '管理员id'
        primary key,
    admin_username   varchar(15)               null comment '管理员名称',
    admin_email      varchar(25)               null comment '管理员邮箱',
    admin_phone      varchar(11)               null comment '管理员手机',
    admin_password   varchar(30)               null comment '管理员密码',
    admin_image      varchar(255)              null comment '管理员头像',
    admin_introduce  varchar(200)              null comment '管理员介绍',
    admin_state      tinyint(1) default 1      null comment '管理员状态',
    admin_login_time datetime                  null comment '管理员最近登录时间',
    deleted          int(11) unsigned zerofill null comment '逻辑删除0存在，1删除'
)
    collate = utf8mb4_unicode_ci;

create table admin_admin_role
(
    adminId int not null comment '管理员id',
    roleId  int not null comment '角色id',
    primary key (adminId, roleId)
)
    collate = utf8mb4_unicode_ci;

create table admin_permission
(
    permission_id   int auto_increment comment '权限id'
        primary key,
    permission_name varchar(20)   null comment '权限名称',
    permission_url  varchar(50)   null comment '权限url',
    deleted         int                          null comment '逻辑删除'
)
    collate = utf8mb4_unicode_ci;

create table admin_role
(
    role_id        int auto_increment comment '角色id'
        primary key,
    role_name      varchar(12)   null comment '角色名称',
    role_introduce varchar(30)   null comment '角色介绍',
    deleted        int(11) unsigned zerofill    null comment '逻辑删除0存在1删除'
)
    collate = utf8mb4_unicode_ci;

create table admin_role_permission
(
    roleId       int not null comment '角色id',
    permissionId int not null comment '权限id',
    primary key (roleId, permissionId)
)
    collate = utf8mb4_unicode_ci;

create table general_industryMap
(
    industryMap_industryCode int auto_increment comment '行业编码（与简历模板表单的resumeTemplateIndustry字段关联）'
        primary key,
    industryMap_industryName varchar(35) charset utf8mb4 not null comment '行业名称（前端下拉框展示用）',
    constraint idx_industry_name
        unique (industryMap_industryName)
)
    comment '通用行业映射表：存储全系统可用的行业编码与名称关联关系' collate = utf8mb4_unicode_ci;

create table general_jobAdviceArticle
(
    jobAdviceArticle_id              int auto_increment comment '求职攻略文章id'
        primary key,
    jobAdviceArticle_title           varchar(30)   null comment '求职攻略文章title',
    jobAdviceArticle_content         mediumtext    null comment '求职攻略文章正文',
    jobAdviceArticle_category        varchar(30)   null comment '求职攻略文章分类',
    jobAdviceArticle_tags            varchar(30)   null comment '求职攻略文章标签',
    jobAdviceArticle_authorName      varchar(30)   null comment '求职攻略文章作者名称',
    jobAdviceArticle_publishedStatus tinyint(1)                   null comment '求职攻略状态',
    jobAdviceArticle_publishedTime   date                         null comment '求职攻略文章发布时间',
    jobAdviceArticle_updatedTime     date                         null comment '求职攻略文章最后更新时间'
)
    collate = utf8mb4_unicode_ci;

create table general_provinceMap
(
    provinceMap_pid   int auto_increment comment '省份主键ID（自增）'
        primary key,
    provinceMap_pname varchar(50) charset utf8mb4 not null comment '省份名称（如：北京市、四川省）',
    constraint uk_pname
        unique (provinceMap_pname) comment '省份名称唯一约束，避免重复'
)
    comment '通用省份映射表：存储全国省份信息' collate = utf8mb4_unicode_ci;

create table general_cityMap
(
    cityMap_cid   int auto_increment comment '城市主键ID（自增）'
        primary key,
    cityMap_cname varchar(50) charset utf8mb4 not null comment '城市名称（如：北京市、成都市）',
    cityMap_pid   int                         not null comment '关联省份ID（外键）',
    constraint uk_cname_pid
        unique (cityMap_cname, cityMap_pid) comment '同一省份下城市名称唯一',
    constraint fk_city_province
        foreign key (cityMap_pid) references general_provinceMap (provinceMap_pid)
            on update cascade
)
    comment '通用城市映射表：存储城市信息，关联省份表' collate = utf8mb4_unicode_ci;

create table general_areaMap
(
    areaMap_aid   int auto_increment comment '区县主键ID（自增）'
        primary key,
    areaMap_aname varchar(50) charset utf8mb4 not null comment '区县名称（如：朝阳区、武侯区）',
    areaMap_cid   int                         not null comment '关联城市ID（外键）',
    constraint uk_aname_cid
        unique (areaMap_aname, areaMap_cid) comment '同一城市下区县名称唯一',
    constraint fk_area_city
        foreign key (areaMap_cid) references general_cityMap (cityMap_cid)
            on update cascade
)
    comment '通用区县映射表：存储区县信息，关联城市表' collate = utf8mb4_unicode_ci;

create index idx_cid
    on general_areaMap (areaMap_cid)
    comment '优化城市关联查询性能';

create index idx_pid
    on general_cityMap (cityMap_pid)
    comment '优化省份关联查询性能';

create table general_recruitPosition
(
    recruitPosition_id             int auto_increment comment '招聘岗位主键ID'
        primary key,
    recruitPosition_name           varchar(30) charset utf8mb4  not null comment '招聘岗位名称（如：Java开发工程师、产品经理）',
    recruitPosition_createdTime    date                         not null comment '岗位创建时间（代码手动设置）',
    recruitPosition_updatedTime    date                         not null comment '岗位更新时间（代码手动设置）',
    recruitPosition_industryCode   int                          not null comment '行业编码（关联通用行业映射表，逻辑外键）',
    recruitPosition_minMonthSalary decimal(10, 2)               not null comment '最低月薪（单位：元，保留2位小数）',
    recruitPosition_maxMonthSalary decimal(10, 2)               not null comment '最高月薪（单位：元，保留2位小数）',
    recruitPosition_weekWorkDayNum tinyint                      not null comment '每周工作天数（1-7，如：5=双休，6=大小周）',
    recruitPosition_goodWelfare    varchar(200) charset utf8mb4 null comment '福利待遇（如：五险一金、年终奖、弹性工作，多个用逗号分隔）'
)
    comment '招聘岗位表：存储各类招聘岗位的基础信息，关联行业表实现按行业分类' collate = utf8mb4_unicode_ci;

create table general_employmentInformation
(
    employmentInformation_id                      int auto_increment comment '投递信息id'
        primary key,
    employmentInformation_code                    int                            null comment '招聘信息编号(查询、删除等)',
    employmentInformation_companyName             varchar(30)     null comment '公司名称',
    employmentInformation_industryCategories      int                            null comment '行业大类',
    employmentInformation_companyType             int                            null comment '企业性质',
    employmentInformation_batch                   int                            null comment '批次',
    employmentInformation_recruitPosition         int                            null comment '招聘岗位',
    employmentInformation_recruitObject           int                            null comment '招聘对象',
    employmentInformation_recruitLocationFirst    int                            null comment '招聘地址(省级)',
    employmentInformation_recruitLocationSecond   int                            null comment '招聘地址(市级)',
    employmentInformation_recruitLocationDetail   varchar(255)    null comment '详细招聘地址',
    employmentInformation_startTime               date                           null comment '创建时间',
    employmentInformation_stopTime                date                           null comment '截止时间',
    employmentInformation_updatedTime             date                           null comment '更新时间',
    employmentInformation_onlineApplicationStatus varchar(30)     null comment '网申状态',
    employmentInformation_officialAnnouncement    varchar(1024)   null comment '官方公告',
    employmentInformation_submissionWay           varchar(1024)   null comment '投递方式',
    employmentInformation_employeeReferralCode    varchar(255)    null comment '内推码',
    deleted                                       int                            null comment '逻辑删除',
    constraint fk_emp_recruit_position
        foreign key (employmentInformation_recruitPosition) references general_recruitPosition (recruitPosition_id)
            on update cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_emp_recruit_position
    on general_employmentInformation (employmentInformation_recruitPosition);

create index idx_industry_code
    on general_recruitPosition (recruitPosition_industryCode);

create index idx_industry_name
    on general_recruitPosition (recruitPosition_industryCode, recruitPosition_name);

create table general_resumeTemplate
(
    resumeTemplate_id           int auto_increment comment '简历模板id'
        primary key,
    resumeTemplate_name         varchar(25)   null comment '简历名称',
    resumeTemplate_reactCode    mediumtext    null comment '简历代码(react)',
    resumeTemplate_industry     int                          null comment '简历行业',
    resumeTemplate_isActive     tinyint(1)                   null comment '是否启用',
    resumeTemplate_createdStart datetime                     null comment '简历创建时间',
    resumeTemplate_updatedStart datetime                     null comment '简历修改时间',
    deleted                     int                          null comment '逻辑删除'
)
    collate = utf8mb4_unicode_ci;

create table general_streetMap
(
    streetMap_sid   int auto_increment comment '街道主键ID（自增）'
        primary key,
    streetMap_sname varchar(50) charset utf8mb4 not null comment '街道名称（如：中关村街道、春熙路街道）',
    streetMap_aid   int                         not null comment '关联区县ID（外键）',
    constraint uk_sname_aid
        unique (streetMap_sname, streetMap_aid) comment '同一区县下街道名称唯一',
    constraint fk_street_area
        foreign key (streetMap_aid) references general_areaMap (areaMap_aid)
            on update cascade
)
    comment '通用街道映射表：存储街道信息，关联区县表' collate = utf8mb4_unicode_ci;

create index idx_aid
    on general_streetMap (streetMap_aid)
    comment '优化区县关联查询性能';

create table user_chatMessage
(
    chatMessage_id             int auto_increment comment '对话记录id'
        primary key,
    chatMessage_conversationId varchar(255)   null comment '对话记录的会话id',
    chatMessage_content        mediumtext     null comment '对话记录的文本内容',
    chatMessage_createdTime    date                          null comment '对话记忆的创建时间',
    chatMessage_messageType    varchar(255)   null comment '消息类型'
)
    collate = utf8mb4_unicode_ci;

create table user_chatMessageContentText
(
    chatMessage_id             int auto_increment comment '对话记录id'
        primary key,
    chatMessage_conversationId varchar(255)   null comment '对话记录的会话id',
    chatMessage_content        mediumtext     null comment '对话记录的文本内容',
    chatMessage_createdTime    date                          null comment '对话记忆的创建时间',
    chatMessage_messageType    varchar(255)   null comment '消息类型'
)
    collate = utf8mb4_unicode_ci;

