package com.learn.hibernate.base;


import com.learn.hibernate.domian.PageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.List;

/**
 * @author lee
 */
@Data
@AllArgsConstructor
@Scope(scopeName = "prototype")
public class MyBQR {

    private CriteriaBuilder cb;

    private CriteriaQuery cq;

    private Root root;



}
