<%@page import="com.csc380.API.Utility"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Server Status Page</title>
    </head>
    <body>
        <h1>SERVER RUNNING!</h1>
        <br><br>If this page is displayed, the MTA BUS INFO SERVLET IS DEPLOYED
        <br><br>Initialized = <%=Utility.initialized%>
    </body>
</html>
