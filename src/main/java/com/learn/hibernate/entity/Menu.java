package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
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
@Table(name = "menu")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Menu  {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 36, nullable = false)
    private String parentId;

    @Column(length = 55, nullable = false)
    private String code;

    @Column(length = 255, nullable = false, columnDefinition = "varchar(255) default '[]'")
    private String path;

    @Column(length = 55, nullable = false)
    @Nojoin
    private String eName;

    @Column(length = 2, nullable = false, columnDefinition = "int default 0")
    @Nojoin
    private Integer hasChild;

    @Column(length = 255, nullable = false, columnDefinition = "varchar(255) default '[]'")
    private String description;

    @Column(length = 11, nullable = false, columnDefinition = "int default 0")
    @Nojoin
    private Integer disPlayOrderBy;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Ignore
    private List<Action> actions;


}
