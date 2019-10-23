package com.learn.hibernate.curd;

import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import com.learn.hibernate.utils.ClassUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 基本增删改查的基类
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/9/28.
 */
public interface BaseService<T, D> {

    /**
     * 获得当前实体类的class
     *
     * @return
     */
    Class<T> getTClass();


    /**
     * 获得当前登录人
     *
     * @return
     */
    default CurrentUserDto getCurrentUser() {
        return CurrentUser.getCurrentUser();
    }

    /**
     * 获得当前登录人ID
     *
     * @return
     */
    default Integer getCurrentUserId() {
        if (null == CurrentUser.getCurrentUser()) {
            return null;
        }
        return CurrentUser.getCurrentUser().getId();
    }

    /**
     * 初始化创建人
     *
     * @param t
     */
    default void createdInit(T t) {
        try {
            if (ClassUtils.hasCloums(t, "createdUserId")) {
                Integer currentUserId;
                String employeeName;
                try {
                    CurrentUserDto currentUser = getCurrentUser();
                    if (currentUser == null) {
                        currentUser = new CurrentUserDto();
                        currentUser.setId(0);
                        currentUser.setEmployeeName("system");
                    }
                    currentUserId = currentUser.getId();
                    employeeName = currentUser.getEmployeeName();
                } catch (Exception e) { //此处为系统自动操作
                    currentUserId = 0;
                    employeeName = "system";
                }
                ClassUtils.setProperty(t, "createdUserId", currentUserId);
                ClassUtils.setProperty(t, "createdUserName", employeeName);
                ClassUtils.setProperty(t, "createdDateTime", new Date().getTime());
            }
            if (ClassUtils.hasCloums(t, "isDeleted")) {
                ClassUtils.setProperty(t, "isDeleted", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化创建人出现异常");
        }
        lastUpdateInit(t);
    }

    /**
     * 初始化最后修改人
     *
     * @param t
     */
    default void lastUpdateInit(T t) {
        try {
            if (ClassUtils.hasCloums(t, "lastUpdateUserId")) {
                Integer currentUserId1 = null;
                String employeeName = null;
                try {
                    CurrentUserDto currentUser = getCurrentUser();
                    if (currentUser == null) {
                        currentUser = new CurrentUserDto();
                        currentUser.setId(0);
                        currentUser.setEmployeeName("system");
                    }
                    currentUserId1 = currentUser.getId();
                    employeeName = currentUser.getEmployeeName();
                } catch (Exception e) { //此处为系统自动操作
                    currentUserId1 = 0;
                    employeeName = "system";
                }
                ClassUtils.setProperty(t, "lastUpdateUserId", currentUserId1);
                ClassUtils.setProperty(t, "lastUpdateUserName", employeeName);
                ClassUtils.setProperty(t, "lastUpdateDateTime", new Date().getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化最后修改人出现异常");
        }
    }

    /**
     * 保存或修改
     *
     * @param t
     * @return
     */
    @Transactional
    default D saveOrUpdate(T t) {
        try {
            if (null == ClassUtils.getProperty(t, "id")) {
                createdInit(t);
            } else {
                lastUpdateInit(t);
                updateById(t);
            }

        } catch (Exception e) {
            throw new RuntimeException("获取当前实体类的主键出现异常");
        }
        return (D) LQuery.save(t);
    }

    /**
     * 批量保存
     *
     * @param tl
     * @return
     */
    @Transactional
    default boolean saveAll(List<T> tl) {
        for (T t : tl) {
            createdInit(t);
        }
        return LQuery.saveAll(tl);
    }

    /**
     * 根据ID删除
     *
     * @param id
     * @return
     */
    @Transactional
    default Integer deleteById(D id) {
        return LQuery
                .delete(getTClass())
                .where()
                .eq("id", id)
                .deleteExecution();
    }

    /**
     * 根据idList批量删除
     *
     * @param ids
     * @return
     */
    @Transactional
    default Integer deleteAll(List ids) {
        return LQuery
                .delete(getTClass())
                .where()
                .in("id", ids)
                .deleteExecution();
    }


    /**
     * 根据ID逻辑删除
     *
     * @param id
     * @return
     */
    @Transactional
    default Integer logicDelete(D id) {
        try {
            T t = (T) getTClass().newInstance();
            if (ClassUtils.hasCloums(t, "isDeleted")) {
                ClassUtils.setProperty(t, "id", id);
                ClassUtils.setProperty(t, "isDeleted", 1);
                return updateById(t);
            } else {
                throw new RuntimeException("该对象不支持逻辑删除");
            }
        } catch (Exception e) {
            throw new RuntimeException("logicDelete：逻辑删除中修改状态出现异常");
        }
    }

    @Transactional
    default Integer logicDeleteAll(List<D> ids) {
        Integer integer = -1;
        try {
            CurrentUserDto currentUser = getCurrentUser();
            T t = (T) getTClass().newInstance();
            if (ClassUtils.hasCloums(t, "isDeleted")) {
                integer = LQuery.update(getTClass())
                        .where()
                        .in("id", ids)
                        .asUpdate()
                        .set("isDeleted", 1)
                        .set("lastUpdateDateTime", new Date().getTime())
                        .set("lastUpdateUserId", currentUser == null ? 0 : currentUser.getId())
                        .set("lastUpdateUserName", currentUser == null ? "" : currentUser.getEmployeeName())
                        .updateExecution();
            } else {
                throw new RuntimeException("该对象不支持逻辑删除");
            }
        } catch (Exception e) {
            throw new RuntimeException("logicDelete：逻辑删除中修改状态出现异常");
        }
        return integer;
    }

    /**
     * 根据ID修改
     *
     * @param t
     * @return
     */
    @Transactional
    default Integer updateById(T t) {
        D id;
        try {
            id = (D) ClassUtils.getProperty(t, "id");

        } catch (Exception e) {
            throw new RuntimeException("updateById中获取实体类的id出现异常");
        }
        if (null == id) {
            throw new RuntimeException("主键不能为空");
        }
        lastUpdateInit(t);
        try {
            return LQuery
                    .update(getTClass())
                    .asUpdate()
                    .set(t)
                    .where()
                    .eq("id", id)
                    .updateExecution();
        } catch (Exception e) {
            throw new RuntimeException("updateById中修改出现异常");
        }
    }


    /**
     * 查询列表，其结果不能直接用于前端的返回
     *
     * @param pageData
     * @return
     */
    @Transactional
    default List<T> selectTList(PageData pageData) {
        return LQuery.find(getTClass())
                .where(pageData)
                .asDto(getTClass())
                .findList();
    }

    /**
     * 查找DTO列表，其结果可以作为前端数据的返回
     *
     * @param pageData
     * @param dto
     * @param <DTO>
     * @return
     */
    @Transactional
    default <DTO> List<DTO> selectDTOList(PageData pageData, Class<DTO> dto) {
        return LQuery.find(getTClass())
                .where(pageData)
                .asDto(dto)
                .findList();
    }

    /**
     * 根据ID查找,其结果不能直接用于前端的返回
     *
     * @param id
     * @return
     */
    @Transactional
    default T selectTById(D id) {
        return (T) LQuery.find(getTClass())
                .eq("id", id)
                .asDto(getTClass())
                .findOne();
    }

    /**
     * 根据ID查找,其结果可以作为前端数据的返回
     *
     * @param id
     * @param dto
     * @param <DTO>
     * @return
     */
    @Transactional
    default <DTO> DTO selectDTOById(D id, Class<DTO> dto) {
        return (DTO) LQuery.find(getTClass())
                .eq("id", id)
                .asDto(dto)
                .findOne();
    }

    /**
     * 查询分页
     *
     * @param pageData
     * @param dto
     * @param <DTO>
     * @return
     */
    @Transactional
    default <DTO> PageInfo selectPage(PageData pageData, Class<DTO> dto) {
        return LQuery.find(getTClass())
                .where(pageData)
                .asDto(dto)
                .findPage();
    }
}
