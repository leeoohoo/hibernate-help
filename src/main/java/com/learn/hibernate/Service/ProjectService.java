package com.learn.hibernate.Service;

import com.learn.hibernate.base.BaseDao;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.common.entity.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final BaseDao baseDao;

    public ProjectService(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional
    public List<Project> getList() throws ClassNotFoundException {
        baseDao.init("Project");
        var result = baseDao.getDtoOrTList(new PageData(), true).getTList();
        return result;
    }

}
