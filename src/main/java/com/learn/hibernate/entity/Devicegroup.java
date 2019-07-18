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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 设备组
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class Devicegroup extends BaseModel {
    @Column(length = 50,nullable = false,columnDefinition = "varchar(50) default '' comment'设备组名称'")
    private String name;

    @Column(length = 36,nullable = false,columnDefinition = "varchar(36) default '00000000000000000000000000000000' comment'父Id'")
    @Nojoin
    private String parentId;

    @Column(nullable = false,columnDefinition = "varchar(255) default '' comment'描述'")
    private String description;

    @Column(length = 50,nullable = false,columnDefinition = "varchar(50) default '' comment'路径'")
    private String path;

    @Column(length = 2,nullable = false,columnDefinition = "int(2) default 0 comment'是否有子集（0没有，1有）'")
    @Nojoin
    private Integer hasChild;

    @Column(length = 36, nullable = false,columnDefinition = "varchar(36) default '' comment '所属服务id（根据系统参数id查询ip）'")
    @Nojoin
    private String serverId;

    @OneToMany(mappedBy = "devicegroup")
    @Ignore
    @JsonIgnore
    private List<Device> devices;

    @OneToMany(mappedBy = "devicegroup")
    @Ignore
    @JsonIgnore
    private List<RoleDeviceGroup> roleDeviceGroups;
}
