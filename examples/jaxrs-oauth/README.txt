JAX-RS OAuth Example 
===========================

Introduction
---------------------------------------

The demo demonstrates the complete OAuth 1.0 flow as described at [1].
Here is the shorter and slightly modified version of the Abstract section [1]:

"OAuth provides a method for clients to access server resources on
 behalf of an end-user. It also provides a process for end-users to authorize third-
 party access to their server resources without sharing their
 credentials (typically, a username and password pair), using user-
 agent redirections." 

This demo show a simple Social.com social application which mantains a calendar for every registered user. Effectively, a user's calendar is the private resource which only this user can access.

Social.com has a partner, Restaraunt Reservations, which offers an online service to Social.com users which can be used to book a dinner at a specific hour. In order to be able to complete the reservation, this third-party service needs to check a user calendar to make sure that the user is actually free at the requested hour at the moment of making the booking. 

Social.com is OAuth-protected and thus the user has to explicitly authorize this service for it to be able to read the calendar. After the third-party service gets the confirmation it can access the user's calendar and interact with its own partner, Restaurant service, in order to make the booking.

Please see the "Demo Description" section below for more information. 

[1] http://tools.ietf.org/html/rfc5849 

Building the Demo
---------------------------------------

This sample consists of 3 parts:
common/   - This directory contains the code that is common
            to both the client and the server. 
            
service/  - This module contains the code for Social.com, Restaurant Registration and Restaurant services.
             
war/      - This module creates a WAR archive containing the code from common and service modules.   

client/   - This is a sample client application which emulates the typical OAuth flow with the end user confirmation encoded in the code. 


From the base directory of this sample (i.e., where this README file is
located), the maven pom.xml file can be used to build and run the demo. 


Using either Linux or Windows:

    mvn install

Running this command will build the demo and create a WAR archive and an OSGi bundle 
for deploying the service either to servlet or OSGi containers.

Usage
===============================================================================
Note: Please follow the parent README.txt first for common build and container setup instructions.

Starting the service
---------------------------------------
 * In the servlet container

    cd war; mvn jetty:run
    
Running the client
---------------------------------------
 
* From the command line
   - cd client
   - mvn exec:java

By default, the client will use the http port 8080 for constructing the URIs.
This port value is set during the build in the client.properties resource file. If the server 
is listening on an alternative port then you can use an 'http.port' system property during the build:
   
- mvn install -Dhttp.port=8181

* From the browser

- Go to "http://localhost:8080/services/forms/registerApp.jsp". It is a Consumer Application Registration Form. Press "Register Your Application" button.
- Follow the link in the bottom of the returned Consumer Application Registration Confirmation page in order to register a user with Social.com.
- The Social.com User Registration Form asks for a user name and password. At the moment only a user name "barry@social.com" with the "1234" password is supported - press "Register With Social.com" to complete the reservation.
- Follow the link in the bottom of the returned User Registration Confirmation page in order to try the online Restaurant Reservations service.
- The Restaurant Reservations Form offers an option to book a restaurant table at a specific hour, press Reserve to start the process. 
- When asked please authenticate with the service using the "barry@social.com" and "1234" pair.
See the demo description for more information about this authentication step.
- The Third Party Authorization Form will ask if the Restaurant Reservations can read the calendar of its owner, "barry@social.com".
- Press "Deny", and after receiving the Restaurant Failure Report page, please follow the link at the bottom of the page to start the reservation again.
- Press Reserve at The Restaurant Reservations Form and this time choose "Allow" at the The Third Party Authorization Form.
- The Restaurant Reservation Confirmation form will be returned confirming the reservation at the required hour.


Demo Desciption
---------------

The description of how to interact with the demo application using a browser in the previous section provides an overview of a typical complete OAuth 1.0 flow.

For the third-party Restaurant Reservations service be able to request an access to a Social.com user's calendar, it has to register itself first with the OAuth server which protects Social.com. Typically this is done out of band and is only demonstrated here to highlight the fact that the third-party must register to be able to participate in OAuth flows. When the registration is complete, the third-party service gets back a consumer id and password pair which it will use later on, when signing OAuth requests. oauth.manager.ThirdPartyRegistrationService is used to emulate this process.

Social.com application keeps a list of registered users and their calendars. Two JAX-RS services are used to implement it, oauth.service.UserRegistrationService which manages the registration requests and oauth.service.SocialService which lets registered users access or update their private calendars.

// TODO: more to follow

  




