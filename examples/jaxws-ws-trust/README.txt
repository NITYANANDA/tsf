jaxws-ws-trust:

Provides an example of a CXF SOAP client (WSC) accessing a Metro STS for a SAML assertion and then subsequently
making a call to a CXF web service provider (WSP).  Two WSC->STS calls are made, one using UsernameToken 
authentication and the other X.509 authentication, but in both cases the same SAML assertion is provided to
the WSP.  Sample keystores and truststores for the WSC, WSP, and STS are provided in this project but are of course
not meant for production use.

How to Deploy:

1.) The STS and WSP runs on standalone Tomcat (Version 6, Tomcat 7 has a problem with the Maven Tomcat plugin
at the time of writing).  If not already done, configure Maven to be able to install and uninstall 
the WSP and the STS by following this section: http://www.jroller.com/gmazza/entry/web_service_tutorial#maventomcat

2.) The path location for the STS keystore in the STS WSDL 
(sts-war/src/main/webapp/WEB-INF/wsdl/DoubleItSTSService.wsdl)
are hardcoded to /tsfexampledir/jaxws-ws-trust/sts-war/stsstore.jks, where tsfexampledir is 
a symbolic link to whereever you installed the TSF examples. For Linux, you can create a symbolic link 
FROM THE ROOT DIRECTORY ("/") as follows:

user@machine:/$ sudo ln -s /path/to/tsf/examples tsfexampledir

If you don't wish to use symbolic links, or for Windows, you will need to update the keystore and 
truststore location information in the STS WSDL.  Just search on stsstore.jks in the WSDL 
and update the four places it occurs with the location of the stsstore.jks file on your
filesystem.

3.) From the sts-war folder, run mvn clean install [tomcat:undeploy] tomcat:deploy.  This will deploy the STS.
Make sure you can view the Metro STS WSDL located at: http://localhost:8080/DoubleItSTS/DoubleItSTSService
before proceeding.

4.) From the service-war folder, run the same command as above to install the WSP.  Make sure you can view 
the WSP WSDL located at: http://localhost:8080/doubleit/services/doubleit?wsdl before proceeding.

5.) Finally, navigate to the client folder and run mvn clean install exec:exec.  You should see the results
of two web service calls, with the client using UsernameToken in one call and X.509 in the other to get the
SAML Assertion.

For DEBUGGING:

1.) Activate client-side logging by uncommenting the logging feature in the client's resources/cxf.xml file.
The log4j.properties file in the same folder can be used to adjust the amount of logging received.

2.) Activate service-side logging by editing the log4j.properties file in the service's resources folder.

3.) Check the logs directory under your Tomcat folder (cataling.log, catalina(date).log in particular) for
any errors reported by the WSP or the STS.

4.) Use Wireshark to view messages:
http://www.jroller.com/gmazza/entry/soap_calls_over_wireshark


