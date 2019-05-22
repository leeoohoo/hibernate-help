package com.learn.hibernate.domian;

import lombok.Data;

import javax.persistence.criteria.Root;

/**
 * @author lee
 */
@Data
public class JoinRoot {

    Root root;

    String filed;
}
