package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "role_menu")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class RoleMenu {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String roleId;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String menuId;

    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId",referencedColumnName = "id",updatable = false, insertable = false)
    @Ignore
    private Menu menu;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId",referencedColumnName = "id",updatable = false, insertable = false)
    @Ignore
    private Role role;

}
