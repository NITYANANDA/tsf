/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package server;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

/* 
 * This class is currently activated only if you use the mvn test -Pserver command
 * from the service folder (see this example's README.txt file).  It shows manual 
 * configuration of root resources, providers, etc. via the PersonApplication class.
 * 
 * For OSGi deployment the resources/META-INF/spring/beans.xml file is read and
 * configuration is performed from that file, and for standalone Tomcat or
 * embedded Jetty the beans.xml defined in the WAR submodule is used instead.
 */
public class ApplicationServer {

    private Server socialServer;
    private Server restaurantServer;
    private Server restaurantReserveServer;
    
    private Server thirdPartySocialServer;
    private Server oauthServer;
    
    public void start() throws Exception {
    	socialServer = startApplication(new SocialApplication());
    	restaurantReserveServer = startApplication(new RestaurantReserveApplication());
    	restaurantServer = startApplication(new RestaurantApplication());
    	
    	thirdPartySocialServer = startApplication(new ThirdPartyAccessApplication());
    	oauthServer = startApplication(new OAuthManagerApplication());
    }
    
    public void stop() throws Exception {
    	socialServer.stop();
    	socialServer.destroy();
    	
    	restaurantReserveServer.stop();
    	restaurantReserveServer.destroy();
    	
    	restaurantServer.stop();
    	restaurantServer.destroy();
    	
    	thirdPartySocialServer.stop();
    	thirdPartySocialServer.destroy();
    	
    	oauthServer.stop();
    	oauthServer.destroy();
    }
    
    private static Server startApplication(Application app) {
    	RuntimeDelegate delegate = RuntimeDelegate.getInstance();

        JAXRSServerFactoryBean bean = delegate.createEndpoint(app, JAXRSServerFactoryBean.class);
        bean.setAddress("http://localhost:8080/services" + bean.getAddress());
        Server server = bean.create();
        server.start();
        return server;
    }
    
    
    
    public static void main(String args[]) throws Exception {
    	ApplicationServer server = new ApplicationServer();
    	server.start();
        System.out.println("Server ready...");

        Thread.sleep(125 * 60 * 1000);
        System.out.println("Server exiting");
        server.stop();
        System.exit(0);
    }
}
