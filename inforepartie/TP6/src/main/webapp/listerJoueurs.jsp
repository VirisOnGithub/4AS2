<%@ page import="metier.Joueur" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.JoueurDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liste des Joueurs</title>
</head>
<body>
<h1>Liste des Joueurs</h1>
<table border="1">
    <tr>
        <th>Numéro</th>
        <th>Pseudo</th>
        <th>Email</th>
        <th>ELO</th>
        <th>Action</th>
    </tr>
    <%
        List<Joueur> joueurs = JoueurDAO.findAll();
        for (Joueur joueur : joueurs) {
    %>
    <tr>
        <td><%= joueur.getJno() %></td>
        <td><%= joueur.getPseudo() %></td>
        <td><%= joueur.getEmail() %></td>
        <td><%= joueur.getElo() %></td>
        <td>
            <a href="modify-player?jno=<%= joueur.getJno() %>">Modifier</a>
            <a href="delete-player?jno=<%= joueur.getJno() %>">Supprimer</a>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>