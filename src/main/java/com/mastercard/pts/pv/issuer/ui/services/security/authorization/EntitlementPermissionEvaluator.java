package com.mastercard.pts.pv.issuer.ui.services.security.authorization;

import java.io.Serializable;
import java.util.Arrays;

import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.mastercard.pts.pv.issuer.ui.services.security.dao.AuthorizationDetailsDao;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "pv",name = "config.security.enabled", matchIfMissing = true)
public class EntitlementPermissionEvaluator implements PermissionEvaluator {
    private static final String ENTITLEMENT_PREFIX = "/Entitlements/";


    AuthorizationDetailsDao authorizationDetailsDao;

    public EntitlementPermissionEvaluator(AuthorizationDetailsDao authorizationDetailsDao) {
        this.authorizationDetailsDao = authorizationDetailsDao;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object entitlement) {
        log.info("the Authentication is "+authentication);
        log.info("the Authentication from security context holder is  "+ SecurityContextHolder.getContext().getAuthentication());
        log.info("the targetDomainObject is "+targetDomainObject);

        if ((authentication == null) || (targetDomainObject == null) || !(entitlement instanceof String)) {
            return false;
        }
        UserIdentity userIdentity = (UserIdentity)authentication.getDetails();
        String entitlementSent = ENTITLEMENT_PREFIX + entitlement;
        log.info("the Roles are  "+userIdentity.getRoles());
        log.info("the username is  "+userIdentity.getAlias());
        boolean value =authorizationDetailsDao.findEntitlements(Arrays.asList(userIdentity.getRoles()), userIdentity.getAlias())
                .stream()
                .anyMatch(entitlement1 -> entitlementSent.equals(entitlement1.getEntitlement()));
        return value;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
