package com.mastercard.pts.pv.issuer.ui.services.security.authorization;

import com.mastercard.pts.pv.issuer.ui.services.security.dao.AuthorizationDetailsDao;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

@Slf4j
@Component
public class LocalPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object entitlement) {
        return true;
    }
    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return true;
    }
}
