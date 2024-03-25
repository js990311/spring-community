package com.toyproject.community.security.authorization.provider;

import org.springframework.security.core.Authentication;

public interface MethodCrudAuthorizationProvider {
    public boolean support(String objectType);

    public boolean create(Long objectId, Authentication authentication);
    public boolean read(Long objectId, Authentication authentication);
    public boolean update(Long objectId, Authentication authentication);
    public boolean delete(Long objectId, Authentication authentication);

}
