WS-Trust (JAX-WS CXF STS Advanced sample)
=================================

This sample illustrates some advanced concepts involving the STS. The basic scenario is the same as the
jaxws-cxf-sts sample, where a CXF SOAP Client (WSC) invokes on a CXF web service provider (WSP). The more
advanced concepts are given as follows:

1.) The WSDL of the WSP (service/src/main/resources/DoubleIt.wsdl) defines an IssuedToken policy that the 
WSC uses to obtain a security token from the STS to access the WSP. The IssuedToken policy is defined
as a "SignedEncryptedSupportingTokens", meaning that the security token issued from the STS must be
signed and encrypted by the WSC, using the Symmetric binding given. Secondly, the TokenType of the 
IssuedToken requires a wsse UsernameToken to access the WSP.

2.) The WSC uses the Asymmetric binding to communicate with the STS, as defined in the WSDL of the STS 
(sts-war/src/main/webapp/WEB-INF/wsdl/DoubleItSTSService.wsdl). The second advanced concept is that the STS
does not ship with a TokenProvider to issue UsernameTokens, as required by the WSP. A UsernameTokenProvider
is provided and installed in the list of TokenProviders of the STS. It uses the subject DN of the WSC
certificate as the username of the token, and a CallbackHandler provides the corresponding password.

3.) Once the WSP receives the WSC request, and decrypts and verifies the UsernameToken, it dispatches it
for validation to the STS. It does this 

Provides an example of a CXF SOAP client (WSC) accessing a CXF STS for a SAML assertion and then subsequently
making a call to a CXF web service provider (WSP).  X.509 authentication is used for the WSC->STS call.
Sample keystores and truststores for the WSC, WSP, and STS are provided in this project but are of course
not meant for production use.

Important Note:  By default, this example uses strong encryption which is 
recommended for use in production systems.  To run this example "out of the
box", you MUST have the "Java(TM) Cryptography Extension (JCE) Unlimited 
Strength Jurisdiction Policy Files" installed into your JRE.  See your
JRE provider for more information. (For Oracle JDK6, the download is available here:
http://www.oracle.com/technetwork/java/javase/downloads/index.html, see the README
file from the download for installation instructions.)
   
Alternatively, you can change to using a lower end encyption algorithm by editing
the security policies in:

client/src/main/resources/DoubleItSTSService.wsdl 
service/src/main/resources/DoubleIt.wsdl
service/src/main/resources/DoubleItSTSService.wsdl
sts-war/src/main/webapp/WEB-INF/wsdl/DoubleItSTSService.wsdl 

to change from "Basic256" to "Basic128".   If you receive an error like 
"Illegal key length" when running the demo, you need to change to Basic128 or
install the Unlimited Strength encryption libraries.

How to Deploy:

1.) The STS and WSP run on either Tomcat 7.x (default) or Tomcat 6.x.  If not already done, 
configure Maven to be able to install 
and uninstall the WSP and the STS by following this section: 
http://www.jroller.com/gmazza/entry/web_service_tutorial#maventomcat.  Also start up Tomcat.

Note: If you wish to use Tomcat 6, use the -PTomcat6 flag when running the mvn tomcat commands 
(tomcat:deploy, tomcat:redeploy, tomcat:undeploy).  (-PTomcat7 is active by default so
does not need to be explicitly specified.)

2.) From the root jaxws-cxf-sts-advanced folder, run "mvn clean install".  If no errors, can then 
run "mvn tomcat:deploy" (or tomcat:undeploy or tomcat:redeploy on subsequent runs as appropriate),
either from the same folder (to deploy the STS and WSP at the same time) or separately, one at a time,
from the war and sts folders.

Before proceeding to the next step, make sure you can view the following WSDLs:
CXF STS WSDL located at: http://localhost:8080/DoubleItSTS/X509?wsdl
CXF WSP: http://localhost:8080/doubleit/services/doubleitUT?wsdl

4.) Navigate to the client folder:

 * To run the client in a standalone manner, run mvn clean install exec:exec.

You should see the results of the web service call. 

For DEBUGGING:

1.) Check the logs directory under your Tomcat folder (cataling.log, catalina(date).log in particular) for
any errors reported by the WSP or the STS.

2.) Use Wireshark to view messages:
http://www.jroller.com/gmazza/entry/soap_calls_over_wireshark

