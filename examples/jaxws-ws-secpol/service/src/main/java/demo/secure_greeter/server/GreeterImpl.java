/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package demo.secure_greeter.server;

import java.util.logging.Logger;

import com.talend.examples.secure_greeter.SecureGreeterPortType;

@javax.jws.WebService(portName = "GreeterPort", 
                      serviceName = "SecureGreeterService",
                      targetNamespace = "http://talend.com/examples/secure-greeter",
                      endpointInterface = "com.talend.examples.secure_greeter.SecureGreeterPortType", 
                      wsdlLocation = "classpath:/ws-secpol-wsdl/greeter.wsdl")
public class GreeterImpl implements SecureGreeterPortType {

    private static final Logger LOG = Logger.getLogger(GreeterImpl.class.getPackage().getName());

    public String greetMe(String me) {
        LOG.info("Executing operation greetMe");
        System.out.println("Executing operation greetMe");
        System.out.println("Message received: " + me + "\n");
        return "Hello " + me;
    }

    public String sayHi() {
        LOG.info("Executing operation sayHi");
        System.out.println("Executing operation sayHi" + "\n");
        return "Bonjour";
    }

    public void greetMeOneWay(String me) {
        LOG.info("Executing operation greetMeOneWay");
        System.out.println("Executing operation greetMeOneWay\n");
        System.out.println("Hello there " + me);
    }
}
