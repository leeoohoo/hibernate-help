package com.learn.hibernate.Contraller;

import com.learn.hibernate.Service.PlanService;
import com.learn.hibernate.Service.ProjectService;
import com.learn.hibernate.common.entity.Project;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectContraller {

    private final ProjectService projectService;
    private final PlanService planService;

    public ProjectContraller(ProjectService projectService, PlanService planService) {
        this.projectService = projectService;
        this.planService = planService;
    }


    @GetMapping("list")
    public Object list() throws ClassNotFoundException {
        return planService.getList();
    }

}
