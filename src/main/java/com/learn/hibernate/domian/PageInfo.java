package com.learn.hibernate.domian;

import com.learn.hibernate.base.MyBQR;
import lombok.Data;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
import javax.persistence.criteria.Expression;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author lee
 */
@Data
public class PageInfo {

    private Integer pageSize = 10;

    private Integer pageIndex = 0;

    private Integer currentPage = 0;

    private Long pageTotal = 0L;

    private Long count;

    private List list = new ArrayList();

    public PageInfo(PageData pageData, MyBQR myBQR, Function<MyBQR, Query> function, boolean isGroup) {
        this.pageIndex = pageData.getPageIndex();
        this.pageSize = pageData.getRows();
        this.currentPage = pageData.getPageIndex();
        myBQR.getCq().multiselect(myBQR.getCb().count(myBQR.getRoot()));
        var query = function.apply(myBQR);
        getCount(query, isGroup);
    }

    public PageInfo(PageData pageData, NativeQuery query, boolean isGroup) {
        this.pageIndex = pageData.getPageIndex();
        this.pageSize = pageData.getRows();
        this.currentPage = pageData.getPageIndex();
        getCount(query, isGroup);
    }

    public PageInfo() {

    }

    private void getCount(Query query, boolean isGroup) {
        List<Tuple> result = query.getResultList();
        if (result == null || result.size() <= 0) {
            this.count = 0L;
        } else {
            var count = 0L;
            if (isGroup) {
                count = result.size();
            } else {
                var r = result.get(0).get(0);
                count = Integer.parseInt(r.toString());
            }
            this.count = count;
        }
        var pageTotal = 0L;
        if (this.getCount() % this.getPageSize() > 0) {
            pageTotal = this.getCount() / this.getPageSize() + 1L;
        } else {
            pageTotal = this.getCount() / this.getPageSize();
        }
        this.pageTotal = pageTotal;
    }

    private void getCount(NativeQuery query, boolean isGroup) {
        List result = query.getResultList();
        if (result == null || result.size() <= 0) {
            this.count = 0L;
        } else {
            var count = 0L;
            if (isGroup) {
                count = result.size();
            } else {
                var r = result.get(0);
                if(r instanceof BigInteger) {
                    count = ((BigInteger) r).intValue();
                }else {
                    count = 0;
                }
            }
            this.count = count;
        }
        var pageTotal = 0L;
        if (this.getCount() % this.getPageSize() > 0) {
            pageTotal = this.getCount() / this.getPageSize() + 1L;
        } else {
            pageTotal = this.getCount() / this.getPageSize();
        }
        this.pageTotal = pageTotal;
    }

    public void init(PageData pageData) {
        this.pageSize = pageData.getRows();
        if(pageData.getPageIndex() == 0) {
            this.pageIndex = pageData.getPageIndex()+1;
            this.currentPage = pageData.getPageIndex()+1;
        }
        var pageTotal = 0L;
        if (this.getCount() % this.getPageSize() > 0) {
            pageTotal = this.getCount() / this.getPageSize() + 1L;
        } else {
            pageTotal = this.getCount() / this.getPageSize();
        }
        this.pageTotal = pageTotal;
    }


}
