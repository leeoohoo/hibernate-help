package com.learn.hibernate.common.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25)
    private String name;

    private Integer type;

    @Nojoin
    private Long createdUserId;

    @OneToMany(mappedBy = "id",cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Plan> plans;


}
