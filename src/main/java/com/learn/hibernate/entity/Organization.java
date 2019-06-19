package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.common.entity.BaseModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "organization")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Organization extends BaseModel {

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 36, nullable = false)
    private String parentId;

    @Column(length = 255, nullable = false)
    private String path;

    @Column(length = 1, nullable = false)
    @Nojoin
    private Integer hasChild;

    @Column(length = 1, nullable = false)
    private Integer type;

    @Column(length = 1, nullable = false)
    private Integer state;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Employee> employees;

}
