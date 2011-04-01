/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package demo.secure_greeter.server;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

public class Server {

    protected Server() throws Exception {
        System.out.println("Starting Server");
        Object implementor = new GreeterImpl();
        String address = "http://localhost:9000/SecureGreeter";
        Endpoint ep = Endpoint.create(SOAPBinding.SOAP11HTTP_BINDING, implementor);
        Map<String, Object> properties = new HashMap<String, Object>();
        
        properties.put("ws-security.callback-handler",
                       "com.talend.examples.secure_greeter.PasswordCallback");
        properties.put("ws-security.encryption.properties",
                       "/ws-secpol-wsdl/bob.properties");
        ep.setProperties(properties);
        ep.publish(address);
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println("Server ready...");

        Thread.sleep(125 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}
