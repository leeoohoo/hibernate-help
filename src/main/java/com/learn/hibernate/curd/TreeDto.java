package com.learn.hibernate.curd;

import com.learn.hibernate.annotation.Ignore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树形的输出类
 * email leeoohoo@gmail.com
 * system
 * Created by lee on 2019/10/11.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TreeDto {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer lay;

    private Integer hasChild;

    @Ignore
    private List<TreeDto> children;

}
