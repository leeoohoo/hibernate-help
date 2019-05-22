package com.learn.hibernate.base;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Data
@AllArgsConstructor
public class MyBQR {

    CriteriaBuilder cb;

    CriteriaQuery cq;

    Root root;


}
