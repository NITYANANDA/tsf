/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package client;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;

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

    public void reserveTable() throws Exception {
    	WebClient rs = WebClient.create("http://localhost:" + port + "/reservation/reserve");
    	Response r = rs.form(new Form().set("name", "Barry")
    			                       .set("phone", "12345678")
    			                       .set("from", "7")
    			                       .set("to", "9"));
    	if (200 == r.getStatus()) {
    		// now, update the calendar at Social
    		System.out.println("Address: " + IOUtils.readStringFromStream((InputStream)r.getEntity()));
    	} else {
    		System.out.println("Reservation failed");
    	}
    }
    
    public static void main(String[] args) throws Exception {

        RESTClient client = new RESTClient();
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
