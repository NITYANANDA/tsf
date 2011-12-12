/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import oauth.common.Calendar;
import oauth.service.UserAccounts;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth.data.OAuthContext;

@Path("/calendar")
public class ThirdPartyAccessService {

    @Context 
    private MessageContext mc;
    private UserAccounts accounts;
	
	public void setAccounts(UserAccounts accounts) {
		this.accounts = accounts;
	}
	
	@GET
	public Calendar getUserCalendar() {
	    OAuthContext oauth = mc.getContent(OAuthContext.class);
	    if (oauth == null || oauth.getSubject() == null || oauth.getSubject().getLogin() == null) {
	        throw new WebApplicationException(403);
	    }
	    String userName = oauth.getSubject().getLogin();
		return accounts.getAccount(userName).getCalendar();
	}
	
}
