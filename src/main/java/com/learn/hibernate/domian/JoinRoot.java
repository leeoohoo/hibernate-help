package com.learn.hibernate.domian;

import lombok.Data;

import javax.persistence.criteria.Root;

@Data
public class JoinRoot {

    Root root;

    String filed;
}
