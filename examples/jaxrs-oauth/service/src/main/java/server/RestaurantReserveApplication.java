/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import oauth.thirdparty.OAuthClientManager;
import oauth.thirdparty.RestaurantReservationService;
import oauth.thirdparty.SecurityContextFilter;

import org.apache.cxf.jaxrs.client.WebClient;

/*
 * Class that can be used (instead of XML-based configuration) to inform the JAX-RS 
 * runtime about the resources and providers it is supposed to deploy.  See the 
 * ApplicationServer class for more information.  
 */
@ApplicationPath("/reservations")
public class RestaurantReserveApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<Object>();
        RestaurantReservationService service = 
        	new RestaurantReservationService();
        OAuthClientManager manager = new OAuthClientManager();
        manager.setAuthorizationURI("http://localhost:8080/services/social/authorize");
        WebClient rts = 
        	WebClient.create("http://localhost:8080/services/oauth/initiate");
        manager.setRequestTokenService(rts);
        WebClient ats = 
        	WebClient.create("http://localhost:8080/services/oauth/token");
        manager.setRequestTokenService(ats);
        
        service.setOAuthClientManager(manager);
        
        SecurityContextFilter filter = new SecurityContextFilter();
        filter.setUsers(Collections.singletonMap("barry@social.com", "1234"));
        
        classes.add(service);
        classes.add(filter);
        
        return classes;
    }
}
