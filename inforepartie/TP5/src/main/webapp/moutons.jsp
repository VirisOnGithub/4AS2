<%@ page import="tpchess.CasseTete" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%! CasseTete ct = new CasseTete(); %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Casse tête</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>Démo plateau casse-tête</h1>
<%
    if (request.getParameter("again") != null) {
        ct = new CasseTete();
    }
    if (request.getParameter("move") != null) {
        int move = Integer.parseInt(request.getParameter("move"));
        ct.jouer(move);
    }
%>
<div class="plateau moutons petit">
<% for (int i = 0; i < 7; i++) {%>
    <p class="<%= (i % 2 == 0) ? "blanc" : "noir" %>">
        <a href="moutons.jsp?move=<%= i %>">
        <% if (ct.get(i) == 'N') { %>
            <i class="pion noir"></i>
        <% } else if (ct.get(i) == 'B') { %>
            <i class="pion blanc"></i>
        <% } %>
        </a>
    </p>
<% } %>
</div>
<% if (ct.estFini()) {%>
    <div style="border: black">
    <%if (ct.gagne()) {%>
        <h2>Gagné !</h2>
    <%} else { %>
        <h2>Perdu !</h2>
    <%} %>
    </div>
<%}%>
<a href="moutons.jsp?again=1">Recommencer</a>
</body>
</html>