package com.learn.hibernate.curd;

import com.learn.hibernate.base.LQuery;
import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.utils.ClassUtils;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形service基类
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/10/10.
 */
public interface BaseTreeService<T, D, TREE> extends BaseService<T, D> {

    Class<TREE> getTreeClass();

    @Transactional
    default D saveOrUpdate(T t) {
        try {
            D id = (D) ClassUtils.getProperty(t, "id");
            if (null == id) {
                createdInit(t);
                D parentId = (D) ClassUtils.getProperty(t, "parentId");//获得未更改的parentId
                Class<T> tClass = getTClass();
                T parent = selectTById(parentId);
                if(null != parent) {
                    Integer lay = (Integer) ClassUtils.getProperty(parent, "lay");
                    ClassUtils.setProperty(t, "lay", lay + 1);
                    ClassUtils.setProperty(parent,"hasChild",1);
                    saveOrUpdate(parent);
                }else {
                    ClassUtils.setProperty(t, "lay", 0);
                }
                ClassUtils.setProperty(t, "hasChild", 0);
            } else {
                update(t);
                return id;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("操作异常");
        }
        return (D) LQuery.save(t);
    }

    @Transactional
    default void update(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        lastUpdateInit(t);
        D id = (D) ClassUtils.getProperty(t, "id");
        T itself = selectTById(id);//获取还未更改的自身
        D updateAfterParentId = (D) ClassUtils.getProperty(t, "parentId");//获得本次更改后的parentId
        D parentId = (D) ClassUtils.getProperty(itself, "parentId");//获得未更改的parentId

        if (parentId.equals(updateAfterParentId)) {//表示没有更改子父关系
            Integer currentThreadVariable = CurrentThreadVariable.getCurrentThreadVariable();

            if (currentThreadVariable != null) {
                currentThreadVariable += 1;
                ClassUtils.setProperty(t, "lay", currentThreadVariable);
                updateById(t);//直接保存'

                CurrentThreadVariable.setCurrentThreadVariable(currentThreadVariable);
                List<T> subsetOfitself = selectTList(new PageData("parentId_eq", id));//查找本身的子集
                if (subsetOfitself.size() > 0) {//如果有子集需要走自身递归，更改lay
                    for (T sub : subsetOfitself) {
                        update(sub);
                    }
                }
            }

            return;
        }

        //如果更改了子父的关系
        //1。查看原来父级是否有其他子集
        List<T> subsetOfOriginalParents = selectTList(new PageData("parentId_eq", parentId));
        if (null == subsetOfOriginalParents || subsetOfOriginalParents.isEmpty()) {
            //根据原来父级id 未找到子集，说明树形结构出现异常
            throw new RuntimeException("树形结构出现异常");
        }
        if (subsetOfOriginalParents.size() < 1) {
            //说明原来父级没有子集，需要更改原来父级的hasChild
            LQuery.update(getTClass())
                    .where()
                    .eq("id", parentId)
                    .asUpdate()
                    .set("hasChild", 0)
                    .updateExecution();
        }

        //2. 查看更改后父级
        T updateAfterParent = selectTById(updateAfterParentId);

        Integer lay = updateAfterParent == null ? -1 : (Integer) ClassUtils.getProperty(updateAfterParent, "lay");
        ClassUtils.setProperty(t, "lay", lay + 1);
        updateById(t);
        CurrentThreadVariable.setCurrentThreadVariable(lay + 1);
        //2.1 查找本身的子集
        List<T> subsetOfitself = selectTList(new PageData("parentId_eq", id));
        if (subsetOfitself.size() > 0) {//如果有子集需要走自身递归，更改lay
            for (T sub : subsetOfitself) {
                update(sub);
            }
        }
    }

    @Transactional
    default int treeDelete(D id) {
        List<T> sub = selectTList(new PageData("parentId_eq", id));
        if (!sub.isEmpty()) {
            return -1;//有子类不允许删除
        }

        T itself = selectTById(id);//获取还未删除的自身
        try {
            T parent = selectTById((D) ClassUtils.getProperty(itself, "parentId"));
            if (null != parent) {
                sub = selectTList(new PageData("parentId", ClassUtils.getProperty(parent, "id")));
                if (sub.size() <= 1) {
                    ClassUtils.setProperty(parent, "hasChild", 0);
                    saveOrUpdate(parent);
                }
            }
            deleteById(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除出现异常");
        }

    }


    @Transactional
    default List<TREE> findTree(PageData pageData) {
        List<TREE> trees = selectDTOList(pageData, getTreeClass());
        if (null == trees || trees.isEmpty()) {
            return null;
        }
        try {
            Map<Integer, List<TREE>> lay = trees.stream().distinct().collect(Collectors.groupingBy(t -> {
                try {
                    return (Integer) ClassUtils.getProperty(t, "lay");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("列表出现异常");
                }
            }));

            List<Integer> collect = lay.keySet().stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            lay = trees.stream().distinct().collect(Collectors.groupingBy(t -> {
                try {
                    return (Integer) ClassUtils.getProperty(t, "parentId");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("列表发生异常");
                }
            }));
            return setChild(lay, lay.get(collect.get(0)));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    default List<TREE> findTree(PageData pageData) {
         = selectDTOList(pageData, getTreeClass());
        if (null == trees || trees.isEmpty()) {
            return null;
        }
        try {
            Map<Integer, List<TREE>> lay = trees.stream().distinct().collect(Collectors.groupingBy(t -> {
                try {
                    return (Integer) ClassUtils.getProperty(t, "lay");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("列表出现异常");
                }
            }));

            List<Integer> collect = lay.keySet().stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            lay = trees.stream().distinct().collect(Collectors.groupingBy(t -> {
                try {
                    return (Integer) ClassUtils.getProperty(t, "parentId");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("列表发生异常");
                }
            }));
            return setChild(lay, lay.get(collect.get(0)));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private List<TREE> setChild(Map<Integer, List<TREE>> lay, List<TREE> trees) {
        trees.forEach(
                tree -> {
                    try {
                        Integer id = (Integer) ClassUtils.getProperty(tree, "id");
                        List<TREE> trees1 = lay.get(id);
                        ClassUtils.setProperty(tree, "children", trees1);
                        if (null != trees1 && trees1.size() > 0) {
                            setChild(lay, trees1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("列表出现异常");
                    }
                }
        );
        return trees;
    }


}
