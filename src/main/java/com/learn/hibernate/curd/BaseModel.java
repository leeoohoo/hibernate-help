package com.learn.hibernate.curd;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 11,columnDefinition = "int(11) comment '主键id'")
    private Integer id;

    @Column(length = 15, nullable = false,columnDefinition = "bigint(20) default 0 comment '创建时间'")
    private Long createdDateTime;

    @Column(length = 15, nullable = false,columnDefinition = "bigint(20) default 0 comment '最后更新时间'")
    private Long lastUpdateDateTime;

    @Column(length = 11, nullable = false,columnDefinition = "int(11) default 0 comment '创建人id'")
    private Integer createdUserId;

    @Column(length = 55, nullable = false,columnDefinition = "varchar(55) default '' comment '创建人姓名'")
    private String createdUserName;

    @Column(length = 11, nullable = false,columnDefinition = "int(11) default 0 comment '最后更新人id'")
    private Integer lastUpdateUserId;

    @Column(length = 25, nullable = false,columnDefinition = "varchar(25) default '' comment '最后更新人姓名'")
    private String lastUpdateUserName;

    @Column(length = 1, nullable = false,columnDefinition = "int(1) default 0 comment '是否逻辑删除'")
    private Integer isDeleted = 0;


}
