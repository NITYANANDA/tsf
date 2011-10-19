<%@ page import="javax.servlet.http.HttpServletRequest, oauth.common.ConsumerRegistration" %>

<%
    ConsumerRegistration reg = (ConsumerRegistration)request.getAttribute("newClient");
    String basePath = request.getContextPath() + request.getServletPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Consumer Application Registration Confirmation</title>
</head>
<body>
<h1>Consumer Application Registration Confirmation</h1>
<em></em>
<p>
Please use the provided Consumer Key and Secret when requesting
Calendar resources of Social.com users as part of OAuth flows.
</p>
<table>
        <tr>
            <td>Consumer Key:</td>
            <td><%= reg.getId() %></td>
        </tr>
        <tr>
            <td>Consumer Secret:</td>
            <td><%= reg.getSecret() %></td>
        </tr> 
</table> 
<p>
Calendar resources of individual users can be accessed at
<%= basePath %>thirdparty/calendar?user=username, where username is the name of the registered
Social.com user. Only HTTP GET verbs can be used.
</p>

<p>
Please follow this <a href="<%= basePath %>forms/registerUser.jsp">link</a> to get a user registered with Social.com 
</p>

</body>
</html>
