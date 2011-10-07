/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import oauth.manager.ThirdPartyAccessService;
import oauth.service.UserAccounts;

import org.apache.cxf.rs.security.oauth.filters.OAuthRequestFilter;

/*
 * Class that can be used (instead of XML-based configuration) to inform the JAX-RS 
 * runtime about the resources and providers it is supposed to deploy.  See the 
 * ApplicationServer class for more information.  
 */
@ApplicationPath("/thirdpartyAccess")
public class ThirdPartyAccessApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<Object>();
        
        UserAccounts accounts = new UserAccounts();
        
        ThirdPartyAccessService thirdPartyAccessService = new ThirdPartyAccessService();
        thirdPartyAccessService.setAccounts(accounts);
        
        classes.add(thirdPartyAccessService);
        
        classes.add(new OAuthRequestFilter());
        
        return classes;
    }
}
