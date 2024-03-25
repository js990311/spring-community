package com.toyproject.community.security.authorization.manager;

import com.toyproject.community.security.authorization.provider.MethodCrudAuthorizationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("authz")
public class DelegatingMethodCRUDAuthorizationManager {

    private final List<MethodCrudAuthorizationProvider> delegates;

    @Autowired
    public DelegatingMethodCRUDAuthorizationManager(ApplicationContext applicationContext){
        Map<String, MethodCrudAuthorizationProvider> providerMap = applicationContext.getBeansOfType(MethodCrudAuthorizationProvider.class);
        this.delegates = providerMap.values().stream().toList();
    }

    public MethodCrudAuthorizationProvider searchDelegate(String objectType){
        for(MethodCrudAuthorizationProvider provider : delegates){
            if(provider.support(objectType)){
                return provider;
            }
        }
        return null;
    }

    public boolean create(String objectType, Long objectId, Authentication authentication){
        MethodCrudAuthorizationProvider delegate = searchDelegate(objectType);
        if(delegate == null)
            return true;

        return delegate.create(objectId,authentication);
    }

    public boolean read(String objectType, Long objectId, Authentication authentication){
        MethodCrudAuthorizationProvider delegate = searchDelegate(objectType);
        if(delegate == null)
            return true;

        return delegate.read(objectId,authentication);
    }


    public boolean update(String objectType, Long objectId, Authentication authentication){
        MethodCrudAuthorizationProvider delegate = searchDelegate(objectType);
        if(delegate == null)
            return true;

        return delegate.update(objectId,authentication);
    }

    public boolean delete(String objectType, Long objectId, Authentication authentication){
        MethodCrudAuthorizationProvider delegate = searchDelegate(objectType);
        if(delegate == null)
            return true;

        return delegate.delete(objectId,authentication);
    }
}
