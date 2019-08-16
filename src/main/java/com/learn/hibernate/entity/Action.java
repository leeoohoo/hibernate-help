package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "action")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Action  {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    public String id;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 105, nullable = false)
    private String description;

    @Column(length = 55, nullable = false)
    private String code;

    @Column(length = 36, nullable = false)
    @Nojoin
    private String menuId;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Menu menu;
}
