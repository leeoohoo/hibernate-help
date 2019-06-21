package com.learn.hibernate.mongodb.base;

import com.learn.hibernate.domian.PageData;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Data
public class SelectQuery {

    private MongoQuery mq;

    public SelectQuery(MongoQuery mongoQuery) {
        this.mq = mongoQuery;
    }


    public SelectQuery order(Sort.Direction sd, String fileds) {
        this.mq.setSort(Sort.by(sd, fileds));
        if (null != this.mq.getPageData()) {
            this.mq.setPageable(
                    PageRequest.of(
                            this.mq.getPageData().getPageIndex(),
                            this.mq.getPageData().getMaxRows(),
                            this.mq.getSort())
            );
        }
        return this;
    }

    public SelectQuery page(PageData pageData) {
        this.mq.setPageData(pageData);
        if (null != this.mq.getSort()) {
            this.mq.setPageable(
                    PageRequest.of(
                            this.mq.getPageData().getPageIndex(),
                            this.mq.getPageData().getMaxRows(),
                            this.mq.getSort()
                    )
            );
        } else {
            this.mq.setPageable(
                    PageRequest.of(
                    this.mq.getPageData().getPageIndex(),
                    this.mq.getPageData().getMaxRows()
                    )
            );
        }
        return this;
    }


    public SelectQuery select(String fileds) {
        String[] strings = null;
        if (fileds != null && !"".equals(fileds)) {
            strings = fileds.split(",");
        }
        Arrays.asList(strings).forEach(
                f -> {
                    this.mq.setFieldsObject(this.mq.getFieldsObject().append(f, 1));
                }
        );
        return this;
    }



    public WhereQuery where(PageData pageData) {
        this.mq.setPageData(pageData);
        return new WhereQuery(this.mq);
    }


    public WhereQuery where() {
        return new WhereQuery(this.mq);
    }

}
