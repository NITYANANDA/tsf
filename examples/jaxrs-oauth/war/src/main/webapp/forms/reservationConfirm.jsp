<%@ page import="javax.servlet.http.HttpServletRequest, oauth.common.ReservationConfirmation" %>

<%
    ReservationConfirmation reserve = (ReservationConfirmation) request.getAttribute("confirm");
    String basePath = request.getContextPath() + request.getServletPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Restaurant Reservation Confirmation</title>
</head>
<body>
<h1>Restaurant Reservation Confirmation</h1>
<em></em>
<p>
Here are the reservation details:
</p>
<table>
<tr>
<td>Address:</td>
<td><%= reserve.getAddress() %></td>
<tr>
<td>Time:</td>
<td><%= reserve.getHour() %> p.m</td>
<tr>
</table>
<p>
Back to <a href="<%= basePath %>forms/reservation.jsp">reservations</a>. 
</p>

</body>
</html>
