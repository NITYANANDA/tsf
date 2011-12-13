<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%
    String basePath = request.getContextPath() + request.getServletPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>User Registration Confirmation</title>
</head>
<body>
<h1>User Registration Confirmation</h1>
<em></em>
<p>
Congratulations! You have successfully registered with Social.com</p>
</p>
<p>
Our partner, Restaurant Reservations, is offering an online service which you can use to reserve a dinner at your favourite restaurant.
</p>
<br/>
<p>
Please follow this <a href="<%= basePath %>forms/reservation.jsp">link</a> to find out more.
</p>
</body>
</html>
