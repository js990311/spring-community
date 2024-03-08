package com.toyproject.community.controller;

import com.toyproject.community.domain.role.RoleName;
import com.toyproject.community.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Deprecated
@RequiredArgsConstructor
@Controller
public class RoleTestController {

    private final RoleService roleService;

    @GetMapping("/setting")
    @ResponseBody
    public String setAll(){
//        roleService.addRole(RoleName.ROLE_USER);
//        roleService.addRole(RoleName.ROLE_MANAGER);
//        roleService.addRole(RoleName.ROLE_ADMIN);

        roleService.addResource("/role_admin",1);
        roleService.addResource("/role_user", 1);
        roleService.addResource("/role_manager", 1);

        roleService.setResourceRole("/role_admin", "ROLE_ADMIN");
        roleService.setResourceRole("/role_user", "ROLE_USER");
        roleService.setResourceRole("/role_manager", "ROLE_MANAGER");

        return "setting";
    }

    @GetMapping("/role_admin")
    @ResponseBody
    public String roleAdmin(){
        return "admin";
    }

    @GetMapping("/role_user")
    @ResponseBody
    public String roleUser(){
        return "user";
    }

    @GetMapping("/role_manager")
    @ResponseBody
    public String roleManager(){
        return "manager";
    }
}
