package com.mastercard.pts.pv.issuer.ui.services.security.web;

import com.mastercard.pts.pv.issuer.ui.services.security.config.PvTestWebSecurityConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;


@Import({PvTestWebSecurityConfig.class})
//@TestPropertySource(locations = "classpth:application-test.yml")
//@SpringBootTest
@RunWith(SpringRunner.class)
public class WebSealSamlServiceTest {
    @Autowired
    WebSealSamlServiceMock webSealSamlServiceMock;
    
    @Ignore
    @Test
    public void testSamlService(){
        try {
            webSealSamlServiceMock.prepareSecurityContextFromSaml();
            Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication().getDetails());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
