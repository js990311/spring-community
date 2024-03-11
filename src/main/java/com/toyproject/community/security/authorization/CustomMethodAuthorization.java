package com.toyproject.community.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component("authz")
public class CustomMethodAuthorization {
    public boolean decide(MethodSecurityExpressionOperations operations) {
        // ... authorization logic
        log.warn("custom authorization bean");
        return true;
    }
}
