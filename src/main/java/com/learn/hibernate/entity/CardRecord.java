package com.learn.hibernate.entity;

import com.learn.hibernate.annotation.Ignore;
import com.learn.hibernate.annotation.Nojoin;
import com.learn.hibernate.common.entity.BaseModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "card_record")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CardRecord extends BaseModel {

    @Column(length = 36, nullable = false)
    @Nojoin
    private String cardId;

    @Column
    private Integer type;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "cardId", referencedColumnName = "id", insertable = false, updatable = false)
    @Ignore
    private Card card;


}
