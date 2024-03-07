package com.toyproject.community.service.role;

import com.toyproject.community.domain.Member;
import com.toyproject.community.domain.role.MemberRole;
import com.toyproject.community.domain.role.Resource;
import com.toyproject.community.domain.role.ResourceRole;
import com.toyproject.community.domain.role.Role;
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

    @Transactional
    public void addResource(String url, Integer orderNum){
        resourceRepository.save(new Resource(url, orderNum));
    }

    @Transactional
    public void addRole(String roleName){
        roleRepository.save(new Role(roleName));
    }

    public void setMemberRole(String memberEmail, String roleName){
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(EntityNotFoundException::new);
    }

    public void setResourceRole(String resourceName, String roleName){
        Role role = roleRepository.findByRoleName(roleName).orElseThrow(EntityNotFoundException::new);
        Resource resource = resourceRepository.findByUrl(resourceName).orElseThrow(EntityNotFoundException::new);
        setResourceRole(resource, role);
    }

    @Transactional
    private void setMemberRole(Member member, Role role){
        em.persist(new MemberRole(member, role));
    }

    @Transactional
    private void setResourceRole(Resource resource, Role role){
        em.persist(new ResourceRole(resource, role));
    }


    @Transactional
    private LinkedHashMap<RequestMatcher, List<String >> setResourceMap(){
        List<ResourceRole> resourceRoles = resourceRoleRepository.findAll();
        List<Resource> resources = resourceRepository.findAllResource();
        LinkedHashMap<RequestMatcher, List<String>> ret = new LinkedHashMap<>();

        resources.forEach(resource -> {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(resource.getUrl());
            List<String> roles = new ArrayList<>();
            resource.getResourceRoles().forEach(rr ->{
                roles.add(
                        rr.getRole().getRoleName()
                );
            });
            ret.put(requestMatcher, roles);
        });

        this.resourceMap = ret;
        return this.resourceMap;

    }

    public LinkedHashMap<RequestMatcher, List<String >> getResourceMap(){
        if(resourceMap==null){
            setResourceMap();
        }
        return this.resourceMap;
    }
}
