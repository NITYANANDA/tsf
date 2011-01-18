JAX-RS Attachments Example 
===========================



Building the Demo
---------------------------------------

This sample consists of 3 parts:
common/   - This directory contains the code that is common
            to both the client and the server. 
            
service/  - This is the JAX-RS service with multiple root resources packaged as an OSGi bundle.
             
war/      - This module creates a WAR archive containing the code from common and service modules.   

client/   - This is a sample client application that uses
            the CXF JAX-RS API to create HTTP-centric and proxy clients and
	    makes several calls with them.


From the base directory of this sample (i.e., where this README file is
located), the maven pom.xml file can be used to build and run the demo. 


Using either UNIX or Windows:

    mvn install

Running this command will build the demo and create a WAR archive and an OSGi bundle 
for deploying the service either to servlet or OSGi containers.

Starting the service
---------------------------------------
 * In the servlet container

    cd war; mvn jetty:run

 * From within the Talend Service Factory OSGi container:

    From the OSGi command line, run:
	install mvn:com.talend.sf.examples.jaxrs-attachments-example/jaxrs-attachments-common/1.0
    install mvn:com.talend.sf.examples.jaxrs-attachments-example/jaxrs-attachments-service/1.0
     That should print out the bundle IDs for the common and server bundles.  From 
     the OSGi command line, then start the installed bundles, for example
        start 115
     where 115 is the bundle ID number that was printed during install.
 * From the command line :
   cd service; mvn -Pserver
    
Running the client
---------------------------------------
 
* From the command line
   - cd client
   - mvn exec:java

By default, the client will use the http port 8080 for constructing the URIs.
This port value is set during the build in the client.properties resource file. If the server is listening on an alternative port then you can use an 'http.port' system property during the build :
   
- mvn install -Dhttp.port=8181

Demo Desciption
---------------

