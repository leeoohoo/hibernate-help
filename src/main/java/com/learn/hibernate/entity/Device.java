package com.learn.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;

/**
 * 设备
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
@Table(name = "device")
public class Device extends BaseModel {
    @Column(nullable = false,columnDefinition = "varchar(50) default '' comment '设备名称'")
    private String name;

    @Column(length = 36,nullable = false,columnDefinition = "varchar(36) default '' comment '设备组id'")
    @Nojoin
    private String groupId;

    @Column(length = 50,nullable = false,columnDefinition = "varchar(50) default '' comment 'ip'")
    private String ip;

    @Column(length = 6,nullable = false,columnDefinition = "int(6) default 0 comment '端口'")
    private Integer port;

    @Column(length = 50,nullable = false,columnDefinition = "varchar(50) default '' comment '设备型号'")
    @Nojoin
    private String tmodel;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '设备类型（0指纹考勤机/1人脸指纹考勤机）'")
    @Nojoin
    private Integer type;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '设备品牌'")
    @Nojoin
    private Integer brand;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '状态(0有效，1无效)'")
    private Integer state;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '设备锁'")
    @Nojoin
    private Integer tlock;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment '是否直连(1直连/0不直连)'")
    @Nojoin
    private Integer isDirect;

    @Column(length = 22,nullable = false,columnDefinition = "bigint(22) default 0 comment '最后采集时间'")
    @Nojoin
    private Long endTime;

    @Column(length = 255,nullable = false,columnDefinition = "varchar(255) default '' comment '设备描述'")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devicegroupId",referencedColumnName = "id",insertable = false,updatable = false)
    @Ignore
    @JsonIgnore
    private Devicegroup devicegroup;


    @OneToMany(mappedBy = "device", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Attendance> attendances;
}
