package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "role")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Role  {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 105, nullable = false)
    private String details;

    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<RoleAction> roleActions;

    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<RoleMenu> roleMenus;


}
