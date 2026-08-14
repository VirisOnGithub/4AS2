<%@ page import="metier.Joueur" %>
<%@ page import="dao.JoueurDAO" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 22/04/2026
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modifier un joueur</title>
</head>
<body>
<% Joueur joueur = (Joueur) request.getAttribute("joueur"); %>
<h1>Modifier un joueur</h1>
<form method="post" action="modify-player">
    <label for="jno">Jno</label>
    <input type="number" readonly name="jno" id="jno" value="<%= joueur.getJno() %>"/>
    <label for="pseudo">Pseudo</label>
    <input type="text" name="pseudo" id="pseudo" value="<%= joueur.getPseudo() %>"/>
    <label for="email">Email</label>
    <input type="email" name="email" id="email" value="<%= joueur.getEmail() %>"/>
    <label for="password">Mot de passe</label>
    <input type="password" name="password" id="password" value=""/>
    <label for="elo">Elo</label>
    <input type="number" name="elo" id="elo" value="<%= joueur.getElo() %>"/>
    <button type="submit">Modifier</button>
</form>
</body>
</html>