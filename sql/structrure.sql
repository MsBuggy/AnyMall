use anymall;
drop table if EXISTS am_core_goods;
create table am_core_goods (
       goods_id  int(11)  not null AUTO_INCREMENT   COMMENT '商品ID'  
       ,category_id int(6)  not null comment '商品所属类目'       
       ,goods_title varchar(160) not null comment '商品标题'       
       ,goods_subtitle varchar(100) not null comment '商品副标题'       
       ,sort_order int(5) not null default 0 comment '排序优先级'       
       ,goods_price decimal(10,2) not null default 0 comment '商品标价'       
       ,unit varchar(10) not null default '件' comment '商品单位，默认：件'
       ,primary key(goods_id)
) ENGINE=InnoDB charset=utf8 COMMENT '商品表';
insert into am_core_goods(category_id,goods_title,goods_subtitle,goods_price)
       values(0,'晨光签字笔-HG0992','超细签字笔',99.9);

drop table if EXISTS am_core_category;
create table am_core_category(
       category_id int(6)  not null AUTO_INCREMENT comment '类目ID'       
       ,parent_id  int(6)  not null default 0 comment '父类目ID，若无父类目则为0'  
       ,name varchar(50) not null comment '类目名字'       
       ,description varchar(255)  comment '类目描述信息'       
       ,icon_img varchar(255)  comment '类目图标'
       ,keywords varchar(255) comment '类目关键字，用于搜索'
       ,sort_order  int(5)  not null default 0 comment '类目显示优先级排序'       
       ,show_in_nav tinyint(1)  not null default 1 comment '是否显示在导航栏，0--否，1--是，默认1'
       ,primary key(category_id)
) ENGINE=InnoDB charset=utf8 comment='商品类目表';