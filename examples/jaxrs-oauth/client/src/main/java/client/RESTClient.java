/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package client;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import oauth.common.Calendar;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.rs.security.oauth.data.OAuthAuthorizationData;

/**
 * Example showing the interaction between HTTP-centric and proxy based RESTful clients and JAX-RS server
 * providing multiple services (PersonService and SearchService)
 */
public final class RESTClient {

    private static final String PORT_PROPERTY = "http.port";
    private static final int DEFAULT_PORT_VALUE = 8080;

    private static final String HTTP_PORT;
    static {
        Properties props = new Properties();
        try {
            props.load(RESTClient.class.getResourceAsStream("/client.properties"));
        } catch (Exception ex) {
            throw new RuntimeException("client.properties resource is not available");
        }
        HTTP_PORT = props.getProperty(PORT_PROPERTY);
    }

    int port;

    public RESTClient() {
        this(getPort());
    }

    public RESTClient(int port) {
        this.port = port;
    }

    public void registerClientApplication() throws Exception {
    	WebClient rs = WebClient.create("http://localhost:" + port + "/oauth/registerProvider");
    	WebClient.getConfig(rs).getHttpConduit().getClient().setReceiveTimeout(10000000L);
    	rs.form(new Form().set("appName", "Restaurant Reservations")
    			          .set("appURI", "http://localhost:" + port + "/reservations/reserve")
    			          .set("password", "987654321"));
    }
    
    public void createUserAccount() throws Exception {
    	WebClient rs = WebClient.create("http://localhost:" + port + "/social/registerUser");
    	WebClient.getConfig(rs).getHttpConduit().getClient().setReceiveTimeout(10000000L);
    	rs.form(new Form().set("user", "barry@social.com").set("password", "1234"));
    	
    	printUserCalendar();
    }
    
    private void printUserCalendar() {
    	WebClient client = createClient("http://localhost:" + port + "/social/accounts/calendar");
    	Calendar calendar = client.get(Calendar.class);
    	System.out.println(calendar.toString());
    }
    
    private void updateAndGetUserCalendar(int hour, String event) {
    	WebClient client = createClient("http://localhost:" + port + "/social/accounts/calendar");
    	Form form = new Form().set("hour", hour).set("event", event);
    	client.form(form);
    	printUserCalendar();
    }
    
    public void reserveTable() throws Exception {
    	WebClient rs = createClient("http://localhost:" + port + "/reservations/reserve/table");
    	Response r = rs.form(new Form().set("name", "Barry")
    			                       .set("phone", "12345678")
    			                       .set("hour", "7"));
    	
    	int status = r.getStatus();
    	Object locationHeader = r.getMetadata().getFirst("Location");
    	if (status != 302 || locationHeader == null) {
    		System.out.println("OAuth flow is broken");
    	}
    	WebClient authorizeClient = createClient(locationHeader.toString());
    	OAuthAuthorizationData data = authorizeClient.get(OAuthAuthorizationData.class);    	
    	Object authenticityCookie = authorizeClient.getResponse().getMetadata().getFirst("Cookie");
    	    	
    	Form authorizationResult = getAuthorizationResult(data);
    	authorizeClient.reset();
    	authorizeClient.to(data.getReplyTo(), false);
    	if (authenticityCookie != null) {
    		authorizeClient.cookie(Cookie.valueOf((String)authenticityCookie));
    	}
    	Response r2 = authorizeClient.form(authorizationResult);
    	
    	int status2 = r2.getStatus();
    	Object locationHeader2 = r.getMetadata().getFirst("Location");
    	if (status2 != 302 || locationHeader2 == null) {
    		System.out.println("OAuth flow is broken");
    	}
    	
    	WebClient finalClient = createClient(locationHeader2.toString());
    	Response finalResponse = finalClient.get();
    	
    	if (200 == finalResponse.getStatus()) {
    		String address = IOUtils.readStringFromStream((InputStream)r.getEntity());
    		// now, update the calendar at Social
    		updateAndGetUserCalendar(7, "Dinner at " + address);
    	} else {
    		System.out.println("Reservation failed");
    	}
    }
    
    private WebClient createClient(String address) {
    	JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
    	bean.setAddress(address);
    	bean.setUsername("barry@social.com");
    	bean.setPassword("1234");
    	WebClient wc = bean.createWebClient();
    	WebClient.getConfig(wc).getHttpConduit().getClient().setReceiveTimeout(10000000L);
    	return wc;
    }
    
    private Form getAuthorizationResult(OAuthAuthorizationData data) {
        Form form = new Form();
        form.set("oauth_token", data.getOauthToken());
        // TODO: get the user confirmation, using a popup window or a blocking cmd input
        form.set("oauthDecision", "allow");
        form.set("session.authenticity.token", data.getAuthenticityToken());
        return form;
    }
    
    public static void main(String[] args) throws Exception {

        RESTClient client = new RESTClient();
        client.registerClientApplication();
        client.createUserAccount();
        client.reserveTable();
    }

    private static int getPort() {
        try {
            return Integer.valueOf(HTTP_PORT);
        } catch (NumberFormatException ex) {
            // ignore
        }
        return DEFAULT_PORT_VALUE;
    }
}
