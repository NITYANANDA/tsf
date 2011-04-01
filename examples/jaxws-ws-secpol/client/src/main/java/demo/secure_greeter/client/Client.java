/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package demo.secure_greeter.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.talend.examples.secure_greeter.SecureGreeterPortType;
import com.talend.examples.secure_greeter.SecureGreeterService;


public final class Client {

    private static final QName SERVICE_NAME =
        new QName("http://talend.com/examples/secure-greeter", "SecureGreeterService");
    private static final QName PORT_NAME =
        new QName("http://talend.com/examples/secure-greeter", "GreeterPort");

    
    URL wsdl;
    SecureGreeterPortType greeter;
    
    public Client(SecureGreeterPortType g) throws Exception {
        greeter = g;
        doWork();
    }
    public Client() throws Exception {
        this(new String[0]);
    }
    
    public Client(String args[]) throws Exception {

        if (args.length == 0) {
            wsdl = Client.class.getResource("/ws-secpol-wsdl/greeter.wsdl");
        }
        doWork();
    }
    public final void doWork() {
        System.out.println("Invoking sayHi...");
        System.out.println("server responded with: " + greeter.sayHi());
        System.out.println();

        System.out.println("Invoking greetMe...");
        System.out.println("server responded with: " + greeter.greetMe(System.getProperty("user.name")));
        System.out.println();

        System.out.println("Invoking greetMeOneWay...");
        greeter.greetMeOneWay(System.getProperty("user.name"));
        System.out.println("No response from server as method is OneWay");
        System.out.println();
    }

    public void setGreeter(SecureGreeterPortType g) {
        greeter = g;
    }
    public SecureGreeterPortType getGreeter() {
        if (greeter == null) {
            SecureGreeterService service = new SecureGreeterService(wsdl, SERVICE_NAME);
            SecureGreeterPortType greeter = service.getPort(PORT_NAME, SecureGreeterPortType.class);

            ((BindingProvider)greeter).getRequestContext()
                .put("ws-security.username", "abcd");
            ((BindingProvider)greeter).getRequestContext()
                .put("ws-security.callback-handler", 
                     "com.talend.examples.secure_greeter.PasswordCallback");
            ((BindingProvider)greeter).getRequestContext()
                .put("ws-security.encryption.properties", "/ws-secpol-wsdl/bob.properties");
        }
        return greeter;
    }
    
    public static void main(String[] args) throws Exception {
        new Client(args);
        System.exit(0);
    }
}
