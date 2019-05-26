package com.learn.hibernate.Contraller;

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

    public ProjectContraller(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("list")
    public List<Project> list() throws ClassNotFoundException {
        return projectService.getList();
    }

}
