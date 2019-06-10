package com.learn.hibernate.domian;

import com.learn.hibernate.base.MyBQR;
import lombok.Data;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Tuple;
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

    private Integer pageTotal = 0;

    private Integer count;

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
            this.count = 0;
        } else {
            var count = 0;
            if (isGroup) {
                count = result.size();
            } else {
                var r = result.get(0).get(0);
                count = Integer.parseInt(r.toString());
            }
            this.count = count;
        }
        var pageTotal = 0;
        if (this.getCount() % this.getPageSize() > 0) {
            pageTotal = this.getCount() / this.getPageSize() + 1;
        } else {
            pageTotal = this.getCount() / this.getPageSize();
        }
        this.pageTotal = pageTotal;
    }

    private void getCount(NativeQuery query, boolean isGroup) {
        List result = query.getResultList();
        if (result == null || result.size() <= 0) {
            this.count = 0;
        } else {
            var count = 0;
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
        var pageTotal = 0;
        if (this.getCount() % this.getPageSize() > 0) {
            pageTotal = this.getCount() / this.getPageSize() + 1;
        } else {
            pageTotal = this.getCount() / this.getPageSize();
        }
        this.pageTotal = pageTotal;
    }


}
