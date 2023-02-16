package com.mastercard.pts.pv.issuer.ui.services.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mastercard.pts.pv.issuer.ui.services.security.model.Entitlement;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAutheticationToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mastercard.pts.pv.issuer.ui.services.security.authorization.EntitlementPermissionEvaluator;
import com.mastercard.pts.pv.issuer.ui.services.security.dao.AuthorizationDetailsDao;


public class EntitlementPermissionEvaluatorTest {

    @Mock
    private AuthorizationDetailsDao authorizationDetailsDao;

    @InjectMocks
    private  EntitlementPermissionEvaluator entitlementPermissionEvaluator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @SuppressWarnings("unchecked")
	@Test
    public void hasPermissionTest(){
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(userIdentity()));
        Mockito.when(authorizationDetailsDao.findEntitlements(Arrays.asList(userIdentity().getRoles()),"alias"))
        .thenReturn(getEntitlements());
        Assert.assertEquals(true, entitlementPermissionEvaluator.hasPermission(getAuthentication(userIdentity()),"test","test"));

    }

    @Test
    public void hasNoPermissionTest(){
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(userIdentityNoAccess()));
        Mockito.when(authorizationDetailsDao.findEntitlements(Arrays.asList(userIdentityNoAccess().getRoles()),"alias"))
                .thenReturn(getEntitlements());
        Assert.assertEquals(false, entitlementPermissionEvaluator.hasPermission(getAuthentication(userIdentityNoAccess()),"test","test12"));

    }


   

    private List<Entitlement> getEntitlements(){
        List<Entitlement> entitlementList = new ArrayList<>();
        Entitlement entilement = new Entitlement() {

            @Override
            public String getEntitlement() {
                return "/Entitlements/test";
            }
        };
        entitlementList.add(entilement);
        return entitlementList;
    }

    private UserIdentity userIdentity(){
        String[] roles ={"test"};
        return UserIdentity.builder()
                .roles(roles)
                .alias("alias")
                .build();


    }
    private UserIdentity userIdentityNoAccess(){
        String[] roles ={"test12"};
        return UserIdentity.builder()
                .roles(roles)
                .alias("alias")
                .build();


    }

    private Authentication getAuthentication(UserIdentity userIdentity){

        return new SamlAutheticationToken(null,userIdentity);
    }

}