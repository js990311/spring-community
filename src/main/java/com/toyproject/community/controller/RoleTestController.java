package com.toyproject.community.controller;

import com.toyproject.community.domain.role.RoleName;
import com.toyproject.community.service.role.RoleService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Deprecated
@RequiredArgsConstructor
@Controller
public class RoleTestController {

    private final RoleService roleService;

    @GetMapping("/role_admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String roleAdmin(){
        return "admin";
    }

    @GetMapping("/role_user")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public String roleUser(){
        return "user";
    }

    @GetMapping("/role_manager")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseBody
    public String roleManager(){
        return "manager";
    }
}
