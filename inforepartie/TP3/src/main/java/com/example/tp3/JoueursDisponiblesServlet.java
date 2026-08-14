package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

@WebServlet("/joueursDisponibles")
public class JoueursDisponiblesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        Set<Integer> selectedPlayers = (Set<Integer>) session.getAttribute("selectedPlayers");
        if (selectedPlayers == null) {
            selectedPlayers = Set.of();
        }

        String poste = req.getParameter("poste");
        if (poste == null || poste.isEmpty()) {
            poste = "ATT";
        }

        Map<String, String> positions = Map.of(
                "ATT", "Attaquant",
                "DEF", "Défenseur",
                "MIL", "Milieu",
                "GAR", "Gardien"
        );

        String links = positions.entrySet().stream()
                .map(entry -> String.format("<a href='?poste=%s'>%s</a>", entry.getKey(), entry.getValue()))
                .reduce((a, b) -> a + " | " + b)
                .orElse("");

        PrintWriter out = resp.getWriter();
        out.println(String.format("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Joueurs Disponibles</title>
                </head>
                <body><h1>Joueurs Disponibles</h1>
                %s
                <table border='1'>
                <tr><th>Nom</th><th>Numéro de maillot</th><th>Position</th><th></th></tr>
                %s
                </table>
                </body>
                </html>
                """, links, showPlayers(poste, selectedPlayers)));
    }

    private String showPlayers(String poste, Set<Integer> selectedPlayers) {
        StringBuilder sb = new StringBuilder();
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        try {
            StringBuilder sql = new StringBuilder("SELECT num_joueur, nom_joueur, maillot, poste FROM joueurs WHERE poste = ?");
            if (!selectedPlayers.isEmpty()) {
                sql.append(" and num_joueur NOT IN (");
                sql.append("?,".repeat(selectedPlayers.size()));
                sql.setLength(sql.length() - 1); // retire la dernière virgule
                sql.append(")");
            }
            try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(sql.toString())) {
                int idx = 1;
                stmt.setString(idx++, poste);
                for (Integer num : selectedPlayers) {
                    stmt.setInt(idx++, num);
                }
                var rs = stmt.executeQuery();
                String request = stmt.toString();
                System.out.println("Requête SQL exécutée : " + request);
                while (rs.next()) {
                    sb.append(String.format("<tr><td>%s</td><td>%d</td><td>%s</td><td><form method='post' action='./joueursChoisis'><input type='hidden' name='num_joueur' value='%s'/><button type='submit'>Ajouter à la liste</button></form></td></tr>",
                            rs.getString("nom_joueur"), rs.getInt("maillot"), rs.getString("poste"), rs.getInt("num_joueur")));
                }
                return sb.toString();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des joueurs : " + e.getMessage(), e);
        }

    }
}
