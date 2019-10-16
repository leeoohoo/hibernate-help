package com.learn.hibernate.curd;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 树形结构的基类
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/9/17.
 */
@Data
@MappedSuperclass
public class TreeModel extends BaseModel{
    @Column(length = 11,nullable = false,columnDefinition = "int(11) default 0 comment '父级ID'")
    private Integer parentId;

    @Column(length = 55,nullable = false,columnDefinition = "varchar(55) default '' comment '名字'")
    private String name;

    @Column(length = 1000,nullable = false,columnDefinition = "varchar(1000) default '' comment '树形地址'")
    private String path;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '树形层级 第一级为0'")
    private Integer lay;

    @Column(length = 1,nullable = false,columnDefinition = "int(1) default 0 comment '是否有子集 0-没有 1-有'")
    private Integer hasChild;

}
