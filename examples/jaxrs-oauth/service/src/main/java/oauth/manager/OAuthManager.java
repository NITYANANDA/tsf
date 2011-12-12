/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.manager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth.data.AccessToken;
import org.apache.cxf.rs.security.oauth.data.Client;
import org.apache.cxf.rs.security.oauth.data.OAuthPermission;
import org.apache.cxf.rs.security.oauth.data.RequestToken;
import org.apache.cxf.rs.security.oauth.data.RequestTokenRegistration;
import org.apache.cxf.rs.security.oauth.data.Token;
import org.apache.cxf.rs.security.oauth.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth.provider.OAuthServiceException;

public class OAuthManager implements OAuthDataProvider {

	private Client client;
	private RequestToken rt;
	private AccessToken at;
	
	public void registerClient(Client c) {
	    this.client = c;
	}
	
	public AccessToken createAccessToken(RequestToken rt) throws OAuthServiceException {
		String tokenId = UUID.randomUUID().toString();
		String tokenSecret = UUID.randomUUID().toString();
		at = new AccessToken(rt.getClient(), tokenId, tokenSecret);
		at.setSubject(rt.getSubject());
		rt = null;
		return at;
	}

	public RequestToken createRequestToken(RequestTokenRegistration reg)
			throws OAuthServiceException {
		String tokenId = UUID.randomUUID().toString();
		String tokenSecret = UUID.randomUUID().toString();
		rt = new RequestToken(reg.getClient(), tokenId, tokenSecret);
		rt.setCallback(reg.getCallback());
		return rt;
	}

	public String setRequestTokenVerifier(RequestToken rt)
			throws OAuthServiceException {
		String verifier = UUID.randomUUID().toString();
		rt.setVerifier(verifier);
		return verifier;
	}

	public AccessToken getAccessToken(String tokenId) throws OAuthServiceException {
		return at == null || !at.getTokenKey().equals(tokenId) ? null : at;
	}

	public Client getClient(String clientId) throws OAuthServiceException {
		return client == null || !client.getConsumerKey().equals(clientId) ? null : client;
	}

	public List<OAuthPermission> getPermissionsInfo(List<String> permissions) {
		return Collections.emptyList();
	}

	public RequestToken getRequestToken(String tokenId)
			throws OAuthServiceException {
		return rt == null || !rt.getTokenKey().equals(tokenId) ? null : rt;
	}

	public void removeToken(Token token) throws OAuthServiceException {
	    if (token instanceof RequestToken) {
		    rt = null;
	    } else {
		    at = null;
	    }
	}

}
