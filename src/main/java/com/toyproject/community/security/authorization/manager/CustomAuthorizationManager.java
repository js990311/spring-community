package com.toyproject.community.security.authorization.manager;

import com.toyproject.community.service.role.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthoritiesAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final RoleService roleService;
    private final RoleHierarchy roleHierarchy;
    private final AuthoritiesAuthorizationManager delegate = new AuthoritiesAuthorizationManager();

    @Autowired
    public CustomAuthorizationManager(RoleService roleService, RoleHierarchy roleHierarchy) {
        this.roleService = roleService;
        this.roleHierarchy = roleHierarchy;
        delegate.setRoleHierarchy(this.roleHierarchy);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        HttpServletRequest request = object.getRequest();
        List<String> resourceRole = findResourceRole(request);
        if(resourceRole!=null){
            return delegate.check(authentication, resourceRole);
        }
        return null;
    }

    private List<String> findResourceRole(HttpServletRequest request){
        LinkedHashMap<RequestMatcher, List<String>> resourceMap = roleService.getResourceMap();;

        if(resourceMap!= null){
            for(Map.Entry<RequestMatcher, List<String>> entry:resourceMap.entrySet()){
                RequestMatcher matcher = entry.getKey();
                if(matcher.matches(request)){
                    return entry.getValue();
                }
            }
        }

        return null;
    }
}
