package com.toyproject.community.service.role;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.dto.MemberDto;
import com.toyproject.community.domain.role.Resource;
import com.toyproject.community.domain.role.RoleName;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.util.ResourceSet;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleServiceTest {
    @Autowired private RoleService roleService;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void createResourceRole(){
        roleService.addRole(RoleName.ROLE_MANAGER);
        roleService.addResource("/test",1);
        roleService.setResourceRole("/test",RoleName.ROLE_MANAGER.toString());
    }
    
    @Test
    public void createMemberRole(){
        roleService.addRole(RoleName.ROLE_USER);
//        Long memberId = memberService.registMember("test@com", "test", "nickname");
        roleService.addRole(RoleName.ROLE_ADMIN);
        roleService.setMemberRole("test@com", RoleName.ROLE_ADMIN.toString());
    }
}