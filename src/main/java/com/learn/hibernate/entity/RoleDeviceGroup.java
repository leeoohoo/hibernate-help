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

/**
 * 角色和设备组关联表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class RoleDeviceGroup extends BaseModel {
    @Column(length = 36,nullable = false,columnDefinition = "varchar(36) default '' comment '设备组Id'")
    @Nojoin
    private String devicesgroupId;

    @Column(length = 36,nullable = false,columnDefinition = "varchar(36) default '' comment '角色id'")
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Ignore
    @JoinColumn(name = "roleId",updatable = false,insertable = false,referencedColumnName = "id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Ignore
    @JoinColumn(name="devicesgroupId",updatable = false,insertable = false,referencedColumnName = "id")
    private Devicegroup devicegroup;

}
