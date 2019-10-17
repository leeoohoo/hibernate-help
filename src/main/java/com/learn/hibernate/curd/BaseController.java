package com.learn.hibernate.curd;

import com.learn.hibernate.domian.PageData;
import com.learn.hibernate.domian.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * controller基类
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/10/10.
 */
@RestController
@Slf4j
public abstract class BaseController<T,D> {
    protected abstract BaseService<T,D> getService();

    protected abstract Class<T> getTClass();

    /**
     * 新增或修改
     * @param vtoMono
     * @param <VTO>
     * @return
     */
    protected   <VTO> Mono<Result> addOrUpdate(Mono<VTO> vtoMono) {
        return vtoMono.map(
                vto -> {
                    T t = null;
                    try {
                        t = getTClass().newInstance();
                        BeanUtils.copyProperties(vto,t);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("初始化实体类出现异常,当前实体类名：{}",getTClass().getName());
                        throw new RuntimeException("初始化实体类出现异常");
                    }
                    D d = getService().saveOrUpdate(t);
                    if(null == d) {
                        return 0;
                    }
                    return d;
                }
        ).map(
                id -> {
                    if(null != id && (Integer)id > 0) {
                        return Result.builder().message("操作成功").build();
                    }else {
                        throw new RuntimeException("操作失败");
                    }
                }
        );
    }

    /**
     * 根据idlist删除
     * @param ids
     * @return
     */
    public Mono<Result> delete(Mono<String> ids) {
        return ids.map(
                id -> {
                    String[] split = id.split(",");
                    return Arrays.asList(split);
                }
        ).map(
                id -> {
                    Integer integer = getService().deleteAll(id);
                    return integer;
                }
        ).map(
                integer -> {
                    if(null != integer && integer >= 0) {
                        return Result.builder().message("删除成功").build();
                    }else {
                        throw new RuntimeException("删除失败");
                    }
                }
        );
    }

    /**
     * 根据id逻辑删除
     * @param id
     * @return
     */
    public Mono<Result> logicDelete (Mono<D> id) {
        return id.map(
                i -> {
                    Integer integer = getService().logicDelete(i);
                    return integer;
                }
        ).map(
                integer -> {
                    if(null != integer && integer >= 0) {
                        return Result.builder().message("删除成功").build();
                    }else {
                        throw new RuntimeException("删除失败");
                    }
                }
        );
    }

    /**
     * 根据idList逻辑删除
     * @param ids
     * @return
     */
    public Mono<Result> logicDeleteAll (Mono<String> ids) {
        return ids.map(
                id -> {
                    String[] split = id.split(",");
                    return Arrays.asList(split);
                }
        ).map(
                id -> {
                    Integer integer = getService().logicDeleteAll((List<D>) id);
                    return integer;
                }
        ).map(
                integer -> {
                    if(null != integer && integer >= 0) {
                        return Result.builder().message("删除成功").build();
                    }else {
                        throw new RuntimeException("删除失败");
                    }
                }
        );
    }

    /**
     * 查询列表
     * @param pageData
     * @param dtoClass
     * @param <DTO>
     * @return
     */
    public <DTO> Mono<List<DTO>> getList(PageData pageData, Class<DTO> dtoClass) {
        List<DTO> dtos = getService().selectDTOList(pageData, dtoClass);
        return Mono.just(dtos);
    }

    /**
     * 查询分页列表
     * @param pageData
     * @param dtoClass
     * @param <DTO>
     * @return
     */
    public <DTO> Mono<PageInfo> getPage(PageData pageData, Class<DTO> dtoClass) {
        PageInfo pageInfo = getService().selectPage(pageData, dtoClass);
        return Mono.just(pageInfo);
    }


}
