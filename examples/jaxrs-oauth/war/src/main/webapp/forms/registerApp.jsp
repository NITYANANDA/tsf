<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Consumer Application Registration Form</title>
</head>
<body>
<h1>Consumer Application Registration Form</h1>
<em></em>
<p>
 <table>
     <form action="/services/oauth/registerProvider" method="POST">
        <tr>
            <td>Application Name:</td>
            <td>
              <input type="text" name="appName" value="Restaurant Reservations"/>
            </td>
        </tr>
        <tr>
            <td>Application Domain:</td>
            <td>
              <input type="text" name="appURI" value="http://localhost:8080/reservations"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Register Your Application"/>
            </td>
        </tr>
  </form>
 </table> 
</body>
</html>
