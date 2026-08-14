<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 28/04/2026
  Time: 08:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liste</title>
</head>
<body>
<%
    List<String> fruits = (List<String>) request.getAttribute("fruits");
    for (String fruit : fruits) {
%>
    <p><%= fruit %></p>
<%
    }
%>
</body>
</html>
