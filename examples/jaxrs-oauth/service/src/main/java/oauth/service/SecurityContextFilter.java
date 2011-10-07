package oauth.service;

import java.security.Principal;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.security.SecurityContext;

public class SecurityContextFilter implements RequestHandler {

	@Context
	private HttpHeaders headers;
	
    private UserAccounts accounts;
	
	public void setAccounts(UserAccounts accounts) {
		this.accounts = accounts;
	}
	
	public Response handleRequest(Message message, ClassResourceInfo cri) {
		List<String> authValues = headers.getRequestHeader("Authorization");
		if (authValues.size() < 2 || !"Basic".equals(authValues.get(0))) {
			return createFaultResponse();
		}
		
		String decodedValue = null;
		try {
			decodedValue = new String(Base64Utility.decode(authValues.get(1)));
		} catch (Base64Exception ex) {
			return createFaultResponse();
		}
		String[] namePassword = decodedValue.split(":");
		if (namePassword.length != 2) {
			return createFaultResponse();
		}
		final UserAccount account = accounts.getAccount(namePassword[0]);
		if (account == null || !account.getPassword().equals(namePassword[1])) {
			return createFaultResponse();
		}
		final SecurityContext sc = new SecurityContext() {

			public Principal getUserPrincipal() {
				return new SimplePrincipal(account.getName());
			}

			public boolean isUserInRole(String arg0) {
				return false;
			}
			
		};
		message.put(SecurityContext.class, sc);
		return null;
	}

	private Response createFaultResponse() {
		return Response.status(401).header("WWW-Authenticate", "Basic").build();
	}
}
