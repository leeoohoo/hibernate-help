package com.learn.hibernate.common.entity;

import com.learn.hibernate.annotation.Ignore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class Organ {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25,nullable = false)
    private String name;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Employee> employees;

}
