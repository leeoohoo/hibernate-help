package com.learn.hibernate.Contraller;

import com.learn.hibernate.Service.EmployeeService;
import com.learn.hibernate.Service.PlanService;
import com.learn.hibernate.Service.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("project")
public class ProjectContraller {

    private final ProjectService projectService;
    private final PlanService planService;
    private final EmployeeService employeeService;

    public ProjectContraller(ProjectService projectService, PlanService planService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.planService = planService;
        this.employeeService = employeeService;
    }


    @GetMapping("list")
    public Object list() throws ClassNotFoundException {
        return planService.getList();
    }

    @GetMapping("save")
    public Object save() throws ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return employeeService.get();
    }
//
//    @GetMapping("delete")
//    public Object delete() throws ClassNotFoundException, IntrospectionException, IllegalAccessException, InvocationTargetException {
//        return planService.delete();
//    }

}
