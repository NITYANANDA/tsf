/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import common.Person;
import common.PersonCollection;
import common.PersonService;

/**
 * Example showing the interaction between HTTP-centric and proxy based RESTful
 * clients and JAX-RS server providing multiple services (PersonService and
 * SearchService)
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

    /**
     * PersonService provides information about all the persons it knows about,
     * about individual persons and their relatives : - ancestors - parents,
     * grandparents, etc - descendants - children, etc - partners Additionally
     * it can help with adding the information about new children to existing
     * persons and update the age of the current Person
     */
    public void usePersonService() throws Exception {

        System.out.println("Using a Web Client...");

        // A single web client will be used to retrieve all the information
        final String personServiceURI = "http://localhost:" + port + "/services/personservice/main";
        WebClient wc = WebClient.create(personServiceURI);

        // Get the list of all persons
        System.out.println("Getting the XML collection of all persons in a type-safe way...");
        wc.accept(MediaType.APPLICATION_XML);
        List<Person> persons = getPersons(wc);

        // Get individual persons using JSON
        System.out.println("Getting individual persons using JSON...");
        wc.reset().accept(MediaType.APPLICATION_JSON);
        for (Person person : persons) {
            // Move forward, for example, given that web client is currently
            // positioned at
            // personServiceURI and a current person id such as 4, wc.path(id)
            // will point
            // the client to "http://localhost:8080/personservice/main/4"
            wc.path(person.getId());

            // Read the stream
            InputStream is = wc.get(InputStream.class);
            System.out.println(IOUtils.toString(is));

            // Move only one path segment back, to
            // "http://localhost:8080/personservice/main"
            // Note that if web client moved few segments forward from the base
            // personServiceURI
            // then wc.back(true) would bring the client back to the baseURI
            wc.back(false);
        }

        // Get Person with id 4 :
        System.out.println("Getting info about Fred...");

        // wc.reset() insures the current path is reset to the base
        // personServiceURI and
        // the headers which may have been set during the previous invocations
        // are also reset.
        wc.reset().accept(MediaType.APPLICATION_XML);
        wc.path("4");
        getPerson(wc);

        System.out.println("Getting info about Fred's mother");
        wc.path("mother");
        getPerson(wc);

        System.out.println("Getting info about Fred's father");
        wc.back(false).path("father");
        getPerson(wc);

        System.out.println("Getting info about Fred's partner");
        wc.back(false).path("partner");
        getPerson(wc);

        // Get info about Fred's ancestors, descendants and children
        wc.reset().accept(MediaType.APPLICATION_XML);
        wc.path("4");

        System.out.println("Getting info about Fred's ancestors");
        wc.path("ancestors");
        getPersons(wc);

        System.out.println("Getting info about Fred's descendants");
        wc.back(false).path("descendants");
        getPersons(wc);

        System.out.println("Getting info about Fred's children");
        wc.back(false).path("children");
        getPersons(wc);

        System.out.println("Fred and Catherine now have a child, adding a child info to PersonService");
        Person child = new Person("Harry", 1);
        Response response = wc.reset().path("4").path("children").post(child);

        // 201 status and the Location header pointing to
        // a newly created (child) Person resource is expected
        if (response.getStatus() != 201) {
            throw new RuntimeException("No child resource has been created");
        }

        // Follow the Location header pointing to a new child resource
        // and get the information, confirm it is Harry
        String location = response.getMetadata().getFirst("Location").toString();
        getPerson(WebClient.create(location));

        System.out.println("Getting an up to date info about Fred's children");
        getPersons(wc);

        System.out.println("Fred has become 40, updating his age");
        // WebClient is currently pointing to personServiceURI + "/4" +
        // "/children"
        wc.back(false);
        // Back to personServiceURI + "/4"
        wc.path("age").type("text/plain");

        // Trying to update the age to the wrong value by mistake
        Response rc = wc.put(20);
        // HTTP Bad Request status is expected
        if (rc.getStatus() != 400) {
            throw new RuntimeException("Fred has become older, not younger");
        }

        rc = wc.put(40);
        // 204 (No Content) is a typical HTTP PUT response
        if (rc.getStatus() != 204) {
            throw new RuntimeException("Impossible to update Fred's age");
        }

        System.out.println("Getting up to date info about Fred");
        // WebClient is currently pointing to personServiceURI + "/4" + "/age"
        wc.back(false);
        // Back to personServiceURI + "/4"
        getPerson(wc);

    }

    /**
     * SearchService is a simple service which shares the information about
     * Persons with the PersonService. It lets users search for individual
     * people by specifying one or more names as query parameters. The
     * interaction with this service also verifies that the JAX-RS server is
     * capable of supporting multiple root resource classes
     */
    private void useSearchService() throws Exception {

        System.out.println("Searching...");

        WebClient wc = WebClient.create("http://localhost:" + port + "/services/personservice/search");
        wc.accept(MediaType.APPLICATION_XML);
        wc.query("name", "Fred", "Lorraine");
        PersonCollection persons = wc.get(PersonCollection.class);

        for (Person person : persons.getList()) {
            System.out.println("Found : " + person.getName());
        }

    }

    /**
     * This function uses a proxy which is capable transforming typed
     * invocations into proper HTTP calls which will be understood by RESTful
     * services. This works for subresources as well. Interfaces and concrete
     * classes can be proxified, in the latter case a CGLIB runtime dependency
     * is needed. CXF JAX-RS proxies can be configured the same way as
     * HTTP-centric WebClients and response status and headers can also be
     * checked. HTTP response errors can be converted into typed exceptions.
     */
    public void useSimpleProxy() {

        System.out.println("Using a simple JAX-RS proxy to get all the persons...");

        String webAppAddress = "http://localhost:" + port + "/services/personservice";
        PersonService proxy = JAXRSClientFactory.create(webAppAddress, PersonService.class);

        // getPersons(a, b): a is zero-based start index, b is number of records
        // to return (-1 for all)
        Collection<Person> persons = proxy.getPersons(0, -1);
        for (Iterator<Person> it = persons.iterator(); it.hasNext();) {
            Person person = it.next();
            System.out.println("ID " + person.getId() + " : " + person.getName() + ", age : "
                               + person.getAge());
        }
    }

    private Person getPerson(WebClient wc) {
        Person person = wc.get(Person.class);
        System.out.println("ID " + person.getId() + " : " + person.getName() + ", age : " + person.getAge());
        return person;
    }

    private List<Person> getPersons(WebClient wc) {
        // Can limit rows returned with 0-based start index and size (number of
        // records to return, -1 for all)
        // default (0, -1) returns everything
        // wc.query("start", "0");
        // wc.query("size", "2");
        List<Person> persons = new ArrayList<Person>(wc.getCollection(Person.class));
        for (Person person : persons) {
            System.out.println("ID " + person.getId() + " : " + person.getName() + ", age : "
                               + person.getAge());
        }
        return persons;
    }

    public static void main(String[] args) throws Exception {

        RESTClient client = new RESTClient();

        // uses CXF JAX-RS WebClient
        client.usePersonService();
        // uses CXF JAX-RS WebClient
        client.useSearchService();
        // uses a basic proxy
        client.useSimpleProxy();
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
