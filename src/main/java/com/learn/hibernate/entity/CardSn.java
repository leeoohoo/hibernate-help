package com.learn.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "card_sn")
public class CardSn {
    @Id
    @JsonProperty
    @Column(name = "cardSn")
    private Integer cardSn;
}
