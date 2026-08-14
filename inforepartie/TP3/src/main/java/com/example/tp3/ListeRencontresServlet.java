package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/listeRencontres")
public class ListeRencontresServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();


        if (req.getMethod() == "POST") {
            try {
                handleDbInput(req);
            } catch (SQLException e) {
                out.println("<p style='color:red'>Erreur lors de l'insertion : " + e.getMessage() + "</p>");
            }
        }


        res.setContentType("text/html;charset=UTF-8");
        out.println("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Joueurs</title>
                </head>
                <body><h1>Liste des Joueurs</h1>
                """ + displayTab(req.getParameter("tri"), req.getParameter("order"), req.getCookies() != null ? java.util.Arrays.stream(req.getCookies()).filter(c -> c.getName().equals("teamId")).findFirst().map(Cookie::getValue).orElse(null) : null) + """
                </body>
                </html>
                """);
    }

    private void handleDbInput(HttpServletRequest req) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement stmt = conn.prepareStatement("INSERT INTO rencontres (jour, eq1, sc1, eq2, sc2) VALUES (?, ?, ?, ?, ?)")) {
            int eq1 = Integer.parseInt(req.getParameter("eq1"));
            int sc1 = Integer.parseInt(req.getParameter("sc1"));
            int eq2 = Integer.parseInt(req.getParameter("eq2"));
            int sc2 = Integer.parseInt(req.getParameter("sc2"));
            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setInt(2, eq1);
            stmt.setInt(3, sc1);
            stmt.setInt(4, eq2);
            stmt.setInt(5, sc2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'insertion : " + e.getMessage(), e);
        }
    }

    public String displayTab(String tri, String order, String teamIdCookie) {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        StringBuilder sb = new StringBuilder();
        String orderBy = switch (tri) {
            case "jour" -> "jour";
            case "eq1" -> "nom_eq1";
            case "sc1" -> "sc1";
            case "eq2" -> "nom_eq2";
            case "sc2" -> "sc2";
            case null,default -> "num_match";
        };
        String SQLReq =
                "SELECT r.*, e1.nom_equipe as nom_eq1, e2.nom_equipe as nom_eq2 FROM rencontres r, equipes e1, equipes e2 where r.eq1 = e1.num_equipe and r.eq2 = e2.num_equipe" + (teamIdCookie != null ? " and (eq1 = " + teamIdCookie + " or eq2 = " + teamIdCookie + ")" : "") + " ORDER BY " + orderBy + (order != null && order.equals("desc") ? " DESC" : " ASC");
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SQLReq)) {
            sb.append("<table border='1'>");
            sb.append(String.format("""
                    <tr>
                    <th><a href="./listeRencontres%s">ID Match</a></th>
                    <th><a href="./listeRencontres%s">Jour</a></th>
                    <th><a href="./listeRencontres%s">Equipe 1</a></th>
                    <th><a href="./listeRencontres%s">Score Equipe 1</a></th>
                    <th><a href="./listeRencontres%s">Equipe 2</a></th>
                    <th><a href="./listeRencontres%s">Score Equipe 2</a></th>
                    <th></th>
                    <th></th>
                    </tr>
                    """, handleSort(tri, "num_match", order), handleSort(tri, "jour", order), handleSort(tri, "eq1", order), handleSort(tri, "sc1", order), handleSort(tri, "eq2", order), handleSort(tri, "sc2", order)));

            while (rs.next()) {
                int id = rs.getInt("num_match");
                var day = rs.getDate("jour");
                String nomEq1 = rs.getString("nom_eq1");
                String nomEq2 = rs.getString("nom_eq2");
                int scoreEq1 = rs.getInt("sc1");
                int scoreEq2 = rs.getInt("sc2");


                sb.append("<tr><td>")
                        .append(id)
                        .append("</td><td>")
                        .append(day)
                        .append("</td><td>")
                        .append(nomEq1)
                        .append("</td><td>")
                        .append(scoreEq1)
                        .append("</td><td>")
                        .append(nomEq2)
                        .append("</td><td>")
                        .append(scoreEq2)
                        .append("</td><td><form method='POST' action='./supprimerRencontre'><input type='hidden' name='id' value='")
                        .append(id)
                        .append("'><button type='submit'>Supprimer</button></form></td><td><form method='GET' action='./modifierRencontre'><input type='hidden' name='id' value='")
                        .append(id)
                        .append("'><button type='submit'>Modifier</button></form></td>")
                        .append("</tr>");

            }

            sb.append("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            sb.append("<p style='color:red'>Erreur SQL : ").append(e.getMessage()).append("</p><br><p>Requête SQL : ").append(SQLReq).append("</p>");
        }
        return sb.toString();
    }

    private String handleSort(String actualSort, String wantedSort, String direction) {
        String orderParam = "";
        if (wantedSort.equals(actualSort) && direction != null && !direction.equals("desc")) {
            orderParam = "&order=desc";
        } else {
            orderParam = "&order=asc";
        }
        return "?tri=" + wantedSort + orderParam;
    }
}
