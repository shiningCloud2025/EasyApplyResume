-- 1. 省份表（父表：无外键依赖，先创建）
-- auto-generated definition
create table general_provinceMap
(
    provinceMap_pid   int auto_increment comment '省份主键ID（自增）'
        primary key,
    provinceMap_pname varchar(50) not null comment '省份名称（如：北京市、四川省）',
    constraint uk_pname
        unique (provinceMap_pname) comment '省份名称唯一约束，避免重复'
)
    comment '通用省份映射表：存储全国省份信息';


-- 2. 城市表（子表：关联省份表，次创建）
create table general_cityMap
(
    cityMap_cid   int auto_increment comment '城市主键ID（自增）'
        primary key,
    cityMap_cname varchar(50) not null comment '城市名称（如：北京市、成都市）',
    cityMap_pid   int         not null comment '关联省份ID（外键）',
    constraint uk_cname_pid
        unique (cityMap_cname, cityMap_pid) comment '同一省份下城市名称唯一',
    constraint fk_city_province
        foreign key (cityMap_pid) references general_provinceMap (provinceMap_pid)
            on update cascade
)
    comment '通用城市映射表：存储城市信息，关联省份表';

create index idx_pid
    on general_cityMap (cityMap_pid)
    comment '优化省份关联查询性能';


-- 3. 区县表（子表：关联城市表，再次创建）
create table general_areaMap
(
    areaMap_aid   int auto_increment comment '区县主键ID（自增）'
        primary key,
    areaMap_aname varchar(50) not null comment '区县名称（如：朝阳区、武侯区）',
    areaMap_cid   int         not null comment '关联城市ID（外键）',
    constraint uk_aname_cid
        unique (areaMap_aname, areaMap_cid) comment '同一城市下区县名称唯一',
    constraint fk_area_city
        foreign key (areaMap_cid) references general_cityMap (cityMap_cid)
            on update cascade
)
    comment '通用区县映射表：存储区县信息，关联城市表';

create index idx_cid
    on general_areaMap (areaMap_cid)
    comment '优化城市关联查询性能';


-- 4. 街道表（子表：关联区县表，最后创建）
create table general_streetMap
(
    streetMap_sid   int auto_increment comment '街道主键ID（自增）'
        primary key,
    streetMap_sname varchar(50) not null comment '街道名称（如：中关村街道、春熙路街道）',
    streetMap_aid   int         not null comment '关联区县ID（外键）',
    constraint uk_sname_aid
        unique (streetMap_sname, streetMap_aid) comment '同一区县下街道名称唯一',
    constraint fk_street_area
        foreign key (streetMap_aid) references general_areaMap (areaMap_aid)
            on update cascade
)
    comment '通用街道映射表：存储街道信息，关联区县表';

create index idx_aid
    on general_streetMap (streetMap_aid)
    comment '优化区县关联查询性能';
