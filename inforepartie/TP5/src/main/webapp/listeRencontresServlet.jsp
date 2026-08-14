<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 09/04/2026
  Time: 09:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Num Match</th>
        <th>Equipe 1</th>
        <th>Equipe 2</th>
        <th>Jour</th>
        <th>Score 1</th>
        <th>Score 2</th>
    </tr>
    <%
        boolean hasRows = false;
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rencontres");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hasRows = true;
    %>
    <tr>
        <td><%= rs.getInt("num_match") %></td>
        <td><%= rs.getString("eq1") %></td>
        <td><%= rs.getString("eq2") %></td>
        <td><%= rs.getDate("jour") %></td>
        <td><%= rs.getInt("sc1") %></td>
        <td><%= rs.getInt("sc2") %></td>
    </tr>
    <%
            }
            if (!hasRows) {
    %>
    <tr>
        <td colspan="6">Aucune rencontre.</td>
    </tr>
    <%
            }
        } catch (Exception e) {
            application.log("Erreur lors de la lecture des rencontres", e);
    %>
    <tr>
        <td colspan="6">Impossible de charger les rencontres pour le moment.</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
