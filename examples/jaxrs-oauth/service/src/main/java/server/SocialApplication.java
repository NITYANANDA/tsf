/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import oauth.manager.ThirdPartyAccessService;
import oauth.service.SecurityContextFilter;
import oauth.service.SocialService;
import oauth.service.UserAccounts;
import oauth.service.UserRegistrationService;

/*
 * Class that can be used (instead of XML-based configuration) to inform the JAX-RS 
 * runtime about the resources and providers it is supposed to deploy.  See the 
 * ApplicationServer class for more information.  
 */
@ApplicationPath("/social")
public class SocialApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<Object>();
        
        UserAccounts accounts = new UserAccounts();
        
        SocialService socialService = new SocialService();
        socialService.setAccounts(accounts);
        
        UserRegistrationService userRegService = new UserRegistrationService();
        userRegService.setAccounts(accounts);
        
        SecurityContextFilter scFilter = new SecurityContextFilter();
        scFilter.setAccounts(accounts);
        
        ThirdPartyAccessService thirdPartyAccessService = new ThirdPartyAccessService();
        thirdPartyAccessService.setAccounts(accounts);
        
        classes.add(socialService);
        classes.add(userRegService);
        classes.add(scFilter);
        
        classes.add(thirdPartyAccessService);
        
        return classes;
    }
}
