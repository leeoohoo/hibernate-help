package com.learn.hibernate.common.entity;

import com.learn.hibernate.annotation.Nojoin;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
public class BaseModel implements Serializable {


    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 15, nullable = false)
    @Nojoin
    private Long createdDateTime;

    @Column(length = 15, nullable = false)
    @Nojoin
    private Long lastUpdateDateTime;

    @Column(length = 36, nullable = false)
    @Nojoin
    private String createdUserId;

    @Column(length = 25, nullable = false)
    @Nojoin
    private String createdUserName;

    @Column(length = 36, nullable = false)
    @Nojoin
    private String lastUpdateUserId;

    @Column(length = 25, nullable = false)
    @Nojoin
    private String lastUpdateUserName;

    @Column(length = 2, nullable = false,columnDefinition = "int default 0")
    @Nojoin
    private Integer isDeleted;


}
