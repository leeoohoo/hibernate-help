package com.learn.hibernate.common.entity;


import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)
    private String name;

    @Nojoin
    private Long organId;

    @Nojoin
    private Long dempartmentId;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "organId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Organ organ;


    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "dempartmentId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Organ dempatment;

}
