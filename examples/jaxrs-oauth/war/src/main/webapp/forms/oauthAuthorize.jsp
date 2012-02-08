<%@ page import="javax.servlet.http.HttpServletRequest,org.apache.cxf.rs.security.oauth.data.OAuthAuthorizationData,org.apache.cxf.rs.security.oauth.data.Permission" %>

<%
    OAuthAuthorizationData data = (OAuthAuthorizationData)request.getAttribute("data");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Third Party Authorization Form</title>
</head>
<body>
<h1>Third Party Authorization Form</h1>
<em></em>
<table align="center">
       <tr align="center">
                <td>

                    <form action="<%= data.getReplyTo() %>" method="POST">
                        <input type="hidden" name="oauth_token"
                               value="<%= data.getOauthToken() %>"/>
                        <input type="hidden"
                               name="<%= org.apache.cxf.rs.security.oauth.utils.OAuthConstants
                                   .AUTHENTICITY_TOKEN %>"
                               value="<%= data.getAuthenticityToken() %>"/>

                        <p>The application <b><%= data.getApplicationName() %></b> would like to
                            <%
                               for (Permission perm : data.getPermissions()) {
                            %>
                               <p/><%= perm.getDescription() %><p/>
                            <%   
                               }
                            %> 
                            at Social.com
                            <br/></p>
                        <br/>
                        <button name="<%= org.apache.cxf.rs.security.oauth.utils.OAuthConstants
                            .AUTHORIZATION_DECISION_KEY %>"
                                type="submit"
                                value="<%= org.apache.cxf.rs.security.oauth.utils.OAuthConstants
                                    .AUTHORIZATION_DECISION_DENY %>">
                            Deny
                        </button>
                        <button name="<%= org.apache.cxf.rs.security.oauth.utils.OAuthConstants
                            .AUTHORIZATION_DECISION_KEY %>"
                                type="submit"
                                value="<%= org.apache.cxf.rs.security.oauth.utils.OAuthConstants
                                    .AUTHORIZATION_DECISION_ALLOW %>">
                            Allow
                        </button>
                    </form>
                </td>
            </tr>
        </table>
    
</body>
</html>
