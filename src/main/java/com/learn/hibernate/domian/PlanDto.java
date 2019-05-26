package com.learn.hibernate.domian;

import lombok.Data;

@Data
public class PlanDto {

    private Long id;

    private String name;

    private Long projectId;

    private String projectName;
}
