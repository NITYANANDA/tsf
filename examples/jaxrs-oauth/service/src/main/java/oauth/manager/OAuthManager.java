package oauth.manager;

import java.util.List;

import org.apache.cxf.rs.security.oauth.data.AccessToken;
import org.apache.cxf.rs.security.oauth.data.Client;
import org.apache.cxf.rs.security.oauth.data.OAuthPermission;
import org.apache.cxf.rs.security.oauth.data.RequestToken;
import org.apache.cxf.rs.security.oauth.data.RequestTokenRegistration;
import org.apache.cxf.rs.security.oauth.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth.provider.OAuthServiceException;

public class OAuthManager implements OAuthDataProvider {

	public String registerClient(Client c) {
		return null;
	}
	
	public AccessToken createAccessToken(RequestToken arg0)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public RequestToken createRequestToken(RequestTokenRegistration arg0)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public String createRequestTokenVerifier(RequestToken arg0)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public AccessToken getAccessToken(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public Client getClient(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OAuthPermission> getPermissionsInfo(List<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public RequestToken getRequestToken(String arg0)
			throws OAuthServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeTokens(String arg0) throws OAuthServiceException {
		// TODO Auto-generated method stub

	}

}
