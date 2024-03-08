package com.toyproject.community.service.role;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.role.*;
import com.toyproject.community.repository.MemberRepository;
import com.toyproject.community.repository.role.ResourceRepository;
import com.toyproject.community.repository.role.ResourceRoleRepository;
import com.toyproject.community.repository.role.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {
    private final EntityManager em;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceRoleRepository resourceRoleRepository;
    private final MemberRepository memberRepository;

    private LinkedHashMap<RequestMatcher, List<String >> resourceMap;

    public void addResource(String url, Integer orderNum){
        resourceRepository.save(new Resource(url, orderNum));
    }

    public void addRole(RoleName roleName){
        roleRepository.save(new Role(roleName));
    }

    public void registMember(Member member){
        Role role = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(EntityNotFoundException::new);
        setMemberRole(member, role);
    }

    public void setMemberRole(String memberEmail, String roleName){
        RoleName roleNameEnum = RoleName.valueOf(roleName);
        Role role = roleRepository.findByRoleName(roleNameEnum).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(EntityNotFoundException::new);
        setMemberRole(member,role);
    }

    public void setResourceRole(String resourceName, String roleName){
        RoleName roleNameEnum = RoleName.valueOf(roleName);
        Role role = roleRepository.findByRoleName(roleNameEnum).orElseThrow(EntityNotFoundException::new);
        Resource resource = resourceRepository.findByUrl(resourceName).orElseThrow(EntityNotFoundException::new);
        setResourceRole(resource, role);
    }

    private void setMemberRole(Member member, Role role){
        em.persist(new MemberRole(member, role));
    }

    private void setResourceRole(Resource resource, Role role){
        em.persist(new ResourceRole(resource, role));
    }


    /**
     * 리소스에 대한 권한 매핑 정보를 설정함
     * @return
     */
    private LinkedHashMap<RequestMatcher, List<String >> setResourceMap(){
        List<ResourceRole> resourceRoles = resourceRoleRepository.findAll();
        List<Resource> resources = resourceRepository.findAllResource();
        LinkedHashMap<RequestMatcher, List<String>> ret = new LinkedHashMap<>();

        resources.forEach(resource -> {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resource.getUrl());
            List<String> roles = new ArrayList<>();
            resource.getResourceRoles().forEach(rr ->{
                roles.add(
                        rr.getRole().getRoleName().toString()
                );
            });
            ret.put(requestMatcher, roles);
        });

        this.resourceMap = ret;
        return this.resourceMap;

    }

    /**
     * 리소스에 대한 권한 매핑 정보를 반환함
     * @return resource-role mapping
     */
    public LinkedHashMap<RequestMatcher, List<String >> getResourceMap(){
        if(resourceMap==null){
            setResourceMap();
        }
        return this.resourceMap;
    }

    public  String getRoleHierarchyInfo(){
        List<Role> roles = roleRepository.findChildRole();
        StringBuilder sb = new StringBuilder();
        for(Role childRole : roles){
            String childRoleName = childRole.getRoleName().toString();
            String parentRoleName = childRole.getParentRole().getRoleName().toString();

            /*
               Child가 Parent보다 더 많은 권한을 보유함
             */
            sb.append(childRoleName);
            sb.append(" > ");
            sb.append(parentRoleName);
            sb.append("\n");
        }
        return sb.toString();
    }
}
