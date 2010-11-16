package service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Person")
@Produces({"application/xml", "application/json" })
public class Person {
	private long id;
    private String name;
    private int age;
    
    private Person mother;
    private Person father;
    private Person partner;
    private Set<Person> children;  
    
    
    public Person() {
    	this("unknown", 50);
    }
    
    public Person(String name, int age) {
    	this.name = name;
    	this.age = age;
   	    children = new HashSet<Person>();
    }
    
    public Person(String name, int age, Person m, Person f, Person p) {
    	this.name = name;
    	this.age = age;
    	this.mother = m;
    	this.father = f;
    	this.partner = p;
    	this.children = new HashSet<Person>();
    }
    
    @GET
    public Person getState() {
    	return this;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
		this.age = age;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	public void addChild(Person child) {
		children.add(child);
	}
    
    @Path("mother")
    public Person getMother() {
        return mother;   	
    }
    
    @Path("father")
    public Person getFather() {
        return father;   	
    }
    
    @Path("partner")
    public Person getPartner() {
        return partner;   	
    }
    
    @GET
    @Path("children")
    public Set<Person> getChildren() {
    	return children;
    }
    
    @GET
    @Path("descendants")
    public Set<Person> getDescendants() {
        Set<Person> ds = new HashSet<Person>();
        addChildren(ds, this);
        return ds;
    }
    
    @GET
    @Path("ancestors")
    public Set<Person> getAncestors() {
    	Set<Person> as = new HashSet<Person>();
        addParents(as, this);
        return as;
    }
    
    @PUT
    @Consumes("text/plain")
    @Path("age")
    public Response updateAge(int newAge) throws PersonUpdateException {
    	if (age > newAge) {
    		throw new PersonUpdateException();
    	}
    	setAge(newAge);
    	return Response.ok().build();
    }
    
    private void addParents(Set<Person> list, Person p) {
        Person m = p.getMother();
        if (m != null) {
        	list.add(m);
        	addParents(list, m);
        }
        Person f = p.getFather();
    	if (f != null) {
    		list.add(f);
    		addParents(list, f);
    	}
    }
    
    private void addChildren(Set<Person> list, Person p) {
        for (Person ch : p.getChildren()) {
	        list.add(ch);
	        addChildren(list, ch);
	    }    
    }
    
    @Override
    public int hashCode() {
    	return name.hashCode() + age;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof Person) {
    	    Person other = (Person)o;
    	    return name.equals(other.name) && age == other.age;
    	} else {
    		return false;
    	}
    }
}

