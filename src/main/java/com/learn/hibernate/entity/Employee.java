package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.common.entity.BaseModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "employee")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Employee extends BaseModel {

    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 1, nullable = false)
    private Integer sex;

    @Column(length = 50, nullable = false)
    @Nojoin
    private String userNo;

    @Column(length = 10, nullable = false)
    @Nojoin
    private Integer state;

    @Column(length = 30)
    @Nojoin
    private String phoneNumber;

    @Column(length = 36, nullable = false)
    @Nojoin
    private String departmentId;

    @Column(length = 36, nullable = false)
    @Nojoin
    private String organizationId;

    @Column
    @Nojoin
    private Date startDate;

    @Column(length = 80)
    @Nojoin
    private String email;

    @Column(length =1, nullable = false)
    @Nojoin
    private Integer isFingerprints;

    @Column(length =1, nullable = false,columnDefinition = "int default 0 comment '是否录入过人脸'")
    @Nojoin
    private Integer isFace;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Organization organization;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Organization department;

    @OneToOne(mappedBy = "employee")
    @Ignore
    private Card card;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<CardFingerprintManagement> cardFingerprintManagements;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Face> faces;



}
