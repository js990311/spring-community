package com.toyproject.community.service.role;

import com.toyproject.community.domain.role.RoleName;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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