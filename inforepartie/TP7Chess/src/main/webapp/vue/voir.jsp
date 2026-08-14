<%@ page import="model.Partie" %>
<%@ page import="model.Joueur" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 29/04/2026
  Time: 09:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Partie partie = (Partie)request.getAttribute("partie");
    Joueur joueur1 = (Joueur) request.getAttribute("joueur1");
    Joueur joueur2 = (Joueur) request.getAttribute("joueur2");
%>
<html>
<head>
    <title>Partie n°<%= partie.getPno() %></title>
</head>
<body>
<h1>Partie n°<%= partie.getPno() %></h1>
<p>Joueur 1 : <%= joueur1.getPseudo() %></p>
<p>Joueur 2 : <%= joueur2.getPseudo() %></p>
<p>Date : <%= partie.getDate() %></p>
<p>Statut de la partie : <%= partie.getStatut() %></p>
<p>Temps de la partie : <%= (partie.getTemps() <= 0 ? "Non indiqué" : partie.getTemps() + " minutes") %></p>
<p>Id du gagnant : <%= partie.getGagnant() %></p>
</body>
</html>
