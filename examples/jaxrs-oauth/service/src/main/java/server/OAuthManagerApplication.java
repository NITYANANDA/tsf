/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import oauth.manager.OAuthManager;
import oauth.manager.ThirdPartyRegistrationService;

import org.apache.cxf.rs.security.oauth.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth.services.AuthorizationRequestService;
import org.apache.cxf.rs.security.oauth.services.RequestTokenService;

/*
 * Class that can be used (instead of XML-based configuration) to inform the JAX-RS 
 * runtime about the resources and providers it is supposed to deploy.  See the 
 * ApplicationServer class for more information.  
 */
@ApplicationPath("/oauth")
public class OAuthManagerApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<Object>();
        
        OAuthManager manager = new OAuthManager();
        ThirdPartyRegistrationService thirdPartyService = new ThirdPartyRegistrationService();
        thirdPartyService.setManager(manager);
        
        RequestTokenService rts = new RequestTokenService(); 
        rts.setDataProvider(manager);
        
        AccessTokenService ats = new AccessTokenService();
        ats.setDataProvider(manager);
        
        AuthorizationRequestService authService = new AuthorizationRequestService();
        authService.setDataProvider(manager);
        
        classes.add(thirdPartyService);
        classes.add(rts);
        classes.add(ats);
        classes.add(authService);
        return classes;
    }
}
