package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.common.entity.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 考勤源数据表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
@Table(name = "attendance")
public class Attendance extends BaseModel {
    @Column(length = 36, nullable = false,columnDefinition = "varchar(36) default '' comment '员工工号'")
    @Nojoin
    private String userNo;

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment '员工姓名'")
    @Nojoin
    private String userName;

    @Column(length = 36, nullable = false,columnDefinition = "varchar(36) default '' comment '组织单位id'")
    @Nojoin
    private String organizationId;//组织单位id

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment '组织单位名称'")
    @Nojoin
    private String organizationName;//组织单位名称

    @Column(length = 36,nullable = false,columnDefinition = "varchar(36) default '' comment '部门id'")
    @Nojoin
    private String departmentId;//部门id

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment '部门名称'")
    @Nojoin
    private String departmentName;//部门名称

    @Column(length = 36, nullable = false,columnDefinition = "varchar(36) default '' comment '设备id'")
    @Nojoin
    private String deviceId;//设备id

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment '设备型号'")
    @Nojoin
    private String deviceModel;//设备型号

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment 'ip'")
    private String ip;//ip

    @Column(length = 5, nullable = false,columnDefinition = "int(5) default 0 comment '端口'")
    private Integer port;//端口

    @Column(length = 50, nullable = false,columnDefinition = "varchar(50) default '' comment '签名'")
    private String signature;//签名

    @Column(length = 2, nullable = false,columnDefinition = "int(2) default 0 comment '是否同步(0同步,1不同步)'")
    @Nojoin
    private Integer isSync;//是否同步

    @Column(length = 22, nullable = false,columnDefinition = "bigint(22) default 0 comment '考勤时间'")
    private Long time;//考勤时间

    @Column(length = 22, nullable = false,columnDefinition = "bigint(22) default 0 comment '最后采集时间'")
    @Nojoin
    private Long endTime;//最后采集时间

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId",referencedColumnName = "id",updatable = false,insertable = false)
    @Ignore
    private Device device;

}