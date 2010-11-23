package service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import common.Person;
import common.PersonCollection;

/**
 * JAX-RS SerachService root resource
 *
 */
@Path("search")
public class SearchService {

	private PersonInfoStorage storage;
	
	public SearchService() {
	}
	
	public void setStorage(PersonInfoStorage storage) {
		this.storage = storage;
	}
	
	@GET
	@Produces({"application/xml", "application/json" })
	public PersonCollection findPersons(@QueryParam("name") List<String> names) {
		PersonCollection collection = new PersonCollection();
		for (String name : names) {
			for (Person p : storage.getAll()) {
				if (p.getName().equalsIgnoreCase(name)) {
					collection.addPerson(p);
				}
			}
		}
		return collection;
	}
	
	
}
