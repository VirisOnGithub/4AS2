<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 29/04/2026
  Time: 09:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Partie" %>
<%@ page import="model.Joueur" %>
<%@ page import="java.util.List" %>
<%
    Partie partie = (Partie) request.getAttribute("partie");
%>
<html>
<head>
    <title>Modification de la partie n° <%= partie.getPno() %></title>
</head>
<body>
<h1>Modification de la partie n° <%= partie.getPno() %></h1>
<form action="modifierPartie" method="post">
    <input type="hidden" name="pno" value="<%= partie.getPno() %>"/>
    <label for="jno1">id du joueur 1</label>
    <select name="jno1" id="jno1">
        <%
            List<Joueur> joueurs = (List<Joueur>) request.getAttribute("joueurs");
            for (Joueur joueur : joueurs) {
        %>
            <option value="<%= joueur.getJno() %>" <%= partie.getJno1() == joueur.getJno() ? "selected" : "" %>><%= joueur.getPseudo() + "(" + joueur.getJno() + ")" %></option>
        <%
            }
        %>
    </select><br/>
    <label for="jno2">id du joueur 2</label>
    <select name="jno2" id="jno2">
        <%
            for (Joueur joueur : joueurs) {
        %>
        <option value="<%= joueur.getJno() %>" <%= partie.getJno2() == joueur.getJno() ? "selected" : "" %>><%= joueur.getPseudo() + "(" + joueur.getJno() + ")" %></option>
        <%
            }
        %>
    </select><br/>
    <label for="date">date de la partie</label>
    <input type="date" id="date" name="date" value="<%= partie.getDate() %>"/><br/>
    <label for="statut">statut de la partie</label>
    <select name="statut" id="statut">
        <option value="1" <%= partie.getStatut() == 1 ? "selected" : "" %>>Non commencée</option>
        <option value="2" <%= partie.getStatut() == 2 ? "selected" : "" %>>En cours</option>
        <option value="3" <%= partie.getStatut() == 3 ? "selected" : "" %>>Terminée</option>
    </select><br/>
    <label for="temps">temps de la partie (en minutes)</label>
    <input type="number" id="temps" name="temps" value="<%= partie.getTemps() %>"/><br/>
    <label name="gagnant">id du gagnant</label>
    <input type="number" name="gagnant" id="gagnant" value="<%= partie.getGagnant() %>"/><br/>
    <input type="submit" value="Modifier"/>
</form>
</body>
</html>
