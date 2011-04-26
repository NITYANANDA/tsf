WS-SecurityPolicy Demo
==================================================

This sample demonstrates a CXF SOAP client invoking on a CXF Web Service
provider (WSP), where the services are secured using WS-SecurityPolicy. In the
first invocation, authentication is performed via a wsse UsernameToken, which
is secured using a SymmetricBinding policy. In the second invocation, an
AsymmetricBinding policy is used to secure the message exchange, where a SAML
Assertion is also required by the WSP. 

This sample consists of 3 parts:
common/   - This directory contains the code that is common
            for both the client and the server.  It contains
	    the WSDL and the artifacts that are generated 
	    from that WSDL.  The wsdl contains the 
	    WS-SecurityPolicy descriptions that is used to
	    secure the messages. It also contains the certs and
	    properties files used for the encryption.

service/  - This is the service.   

client/   - This is a sample client application that uses
            the JAX-WS API's to create a proxy client and
	    makes several calls with it.



Building the Demo
---------------------------------------
  
Using either UNIX or Windows:

    mvn install



Starting the Service
---------------------------------------
  * From the command line:
     cd service ; mvn exec:java

  * From within the OSGi container
     From the OSGi command line, run:
	install mvn:com.talend.sf.examples.jaxws-ws-secpol/ws-secpol-common/1.0
        install mvn:com.talend.sf.examples.jaxws-ws-secpol/ws-secpol-server/1.0
     That should print out the bundle ID for the server bundle.  From 
     the OSGi command line, then run
        start 115
     where 115 is the bundle ID number that was printed during install.


Running the Client
---------------------------------------
  * From the command line:
     cd client ; mvn exec:java
  * From within the OSGi container
     From the OSGi command line, run:
	install mvn:com.talend.sf.examples.jaxws-ws-secpol/ws-secpol-common/1.0
        install mvn:com.talend.sf.examples.jaxws-ws-secpol/ws-secpol-client/1.0
     That should print out the bundle ID for the client bundle.  From 
     the OSGi command line, then run
        start 115
     where 115 is the bundle ID number that was printed during install.



Cleaning up
---------------------------------------
To remove the code generated from the WSDL file and the .class
files, run "mvn clean".



