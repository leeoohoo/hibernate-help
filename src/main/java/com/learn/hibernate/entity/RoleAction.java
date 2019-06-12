package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "role_action")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class RoleAction {


    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String roleId;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String actionId;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String menuId;

    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "actionId",referencedColumnName = "id",updatable = false, insertable = false)
    @Ignore
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId",referencedColumnName = "id",updatable = false, insertable = false)
    @Ignore
    private Role role;

}
