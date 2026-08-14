<%@ page import="model.Partie" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 29/04/2026
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liste des parties</title>
</head>
<body>
<% List<Partie> parties = (List<Partie>) request.getAttribute("parties"); %>
<h1>Liste des parties</h1>
<table border="1">
    <tr>
        <th>Numéro de partie</th>
        <th>Joueur 1</th>
        <th>Joueur 2</th>
        <th>Date</th>
        <th>Statut</th>
        <th>Temps (en minutes)</th>
        <th>Gagnant</th>
        <th>Action</th>
    </tr>
<%
    for (Partie partie : parties) {
%>
    <tr>
        <td><%= partie.getPno() %></td>
        <td><%= partie.getJno1() %></td>
        <td><%= partie.getJno2() %></td>
        <td><%= partie.getDate() %></td>
        <td><%= partie.getStatut() %></td>
        <td><%= partie.getTemps() %></td>
        <td><%= partie.getGagnant() %></td>
        <td><a href="partie?action=modfier&pno=<%= partie.getPno() %>" style="margin-right: 10px">Modifier</a><a href="partie?action=voir&pno=<%= partie.getPno() %>">Voir</a></td>
    </tr>
<%
    }
%>
</table>
</body>
</html>
