package com.mastercard.pts.pv.issuer.ui.services.security.config;

import com.mastercard.pts.pv.issuer.ui.services.security.authorization.EntitlementPermissionEvaluator;
import com.mastercard.pts.pv.issuer.ui.services.security.authorization.LocalPermissionEvaluator;
import com.mastercard.pts.pv.issuer.ui.services.security.dao.AuthorizationDetailsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "pv",name = "config.security.enabled", havingValue = "false")
public class PvLocalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
     private EntitlementPermissionEvaluator entitlementPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new LocalPermissionEvaluator());
        return expressionHandler;
    }




}