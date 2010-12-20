/**
 * Copyright (C) 2010 Talend Inc. - www.talend.com
 */
package client;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.client.WebClient;

import common.Person;

public final class RESTClient {
    private static String urlStem = "http://localhost:8080/services/members/";

    public static void main(String[] args) throws Exception {
        Person p = getMember(1);

        System.out.println("Updating person name using PUT and .../members/1/name URL:");
        WebClient wc = WebClient.create(urlStem);
        wc.path("1");
        wc.path("name").type("text/plain");
        Response resp = wc.put("George".equals(p.getName()) ? "Sam" : "George");
        // for PUTS, resp.getStatus() returns 204 if success, 404 if ID couldn't
        // be found
        p = getMember(1);

        System.out.println("Updating multiple fields of the person using PUT and .../members/1 URL:");
        p.setName("Bob");
        p.setAge(p.getAge() == 40 ? 30 : 40);
        resp = wc.reset().path("1").put(p);
        p = getMember(1);

        System.out.println("Creating a new member using POST and .../members/1 URL:");
        Person newMember = new Person();
        newMember.setName("Harry");
        newMember.setAge(30);
        resp = wc.reset().post(newMember);

        if (resp.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new RuntimeException("Could not add new member.");
        }

        // POSTS (creates) return the new item's URL (containing the
        // server-generated ID)
        // in the HTTP Location header
        String location = resp.getMetadata().getFirst("Location").toString();
        System.out.println("New Member location returned from POST: " + location);
        System.out.println("Requerying newly added data using above URL:");
        int maxID = new Integer(location.substring(location.lastIndexOf("/") + 1)).intValue();
        getMember(location);

        // GET with the .../members/ URI retrieves all members
        getAllMembers(wc);

        if (maxID > -1) {
            System.out.println("Removing member with ID of " + maxID);
            // resp.getStatus() returns 410 returned on successful DELETE, 404 if item not found           
            resp = wc.path(new Integer(maxID).toString()).delete();
        }

        // reprint of list with latest member removed
        wc.reset();
        getAllMembers(wc);
        System.out.println("\n");
        System.exit(0);
    }

    private static Person getMember(int memberNo) throws Exception {
        WebClient wc = WebClient.create(urlStem);
        wc.path(memberNo);
        Person p = wc.get(Person.class);
        System.out.println("person ID/Name/Age = " + p.getId() + " / " + p.getName() + " / " + p.getAge());
        return p;
    }

    private static Person getMember(String locationURL) throws Exception {
        WebClient wc = WebClient.create(locationURL);
        Person p = wc.get(Person.class);
        System.out.println("person ID/Name/Age = " + p.getId() + " / " + p.getName() + " / " + p.getAge());
        return p;
    }

    private static void getAllMembers(WebClient webClient) {
        System.out.println("Retrieving list of all members:");
        List<Person> persons = new ArrayList<Person>(webClient.getCollection(Person.class));
        for (Person person : persons) {
            System.out
                .println("ID " + person.getId() + ": " + person.getName() + ", age: " + person.getAge());
        }
    }
}
