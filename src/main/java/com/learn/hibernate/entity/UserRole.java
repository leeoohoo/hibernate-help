package com.learn.hibernate.entity;

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
@Table(name = "user_role")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UserRole {

    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "jpa-uuid")
    private String id;


    @Column(length = 55,nullable = false)
    private String username;

    @Column(length = 36,nullable = false)
    @Nojoin
    private String roleId;


    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId",referencedColumnName = "id",insertable = false,updatable = false)
    private Role role;




}
