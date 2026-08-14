package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/modifierRencontre")
public class ModifierRencontreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html;charset=UTF-8");
        HashMap<String, String> rencontre = getRecontre(req.getParameter("id"));
        out.println(String.format("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Modifier Rencontre</title>
                </head>
                <body><h1>Modifier Rencontre</h1>
                <form method="post" action="./modifierRencontre">
                    <input type="hidden" name="id" value="%s">
                    <select name="equipe1">
                        %s
                    </select>
                    <select name="equipe2">
                        %s
                    </select>
                    <input type="date" name="date" value="%s">
                    <input type="sc1" name="score1" value="%s">
                    <input type="sc2" name="score2" value="%s">
                    <input type="submit" value="Modifier">
                </form>
                </body>
                </html>
                """, rencontre.get("num_match"), getEquipeOptions(rencontre.get("equipe1")), getEquipeOptions(rencontre.get("equipe2")), rencontre.get("date_match"), rencontre.get("score1"), rencontre.get("score2")));
    }

    private HashMap<String, String> getRecontre(String num_match) {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        HashMap<String, String> rencontre = new HashMap<>();
        try (var conn = java.sql.DriverManager.getConnection(url); var stmt = conn.prepareStatement("select * from rencontres where num_match = ?")) {
            stmt.setInt(1, Integer.parseInt(num_match));
            var rs = stmt.executeQuery();
            if (rs.next()) {
                rencontre.put("num_match", String.valueOf(rs.getInt("num_match")));
                rencontre.put("equipe1", rs.getString("eq1"));
                rencontre.put("equipe2", rs.getString("eq2"));
                rencontre.put("date_match", rs.getDate("jour").toString());
                rencontre.put("score1", String.valueOf(rs.getInt("sc1")));
                rencontre.put("score2", String.valueOf(rs.getInt("sc2")));
            } else {
                throw new RuntimeException("Rencontre non trouvée pour num_match = " + num_match);
            }
            return rencontre;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEquipeOptions(String selected) {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        StringBuilder options = new StringBuilder();
        try (var conn = java.sql.DriverManager.getConnection(url); var stmt = conn.prepareStatement("select num_equipe, nom_equipe from equipes;")) {
            var rs = stmt.executeQuery();
            while (rs.next()) {
                String nom = rs.getString("nom_equipe");
                String num = String.valueOf(rs.getInt("num_equipe"));
                options.append(String.format("<option value=%s %s>%s</option>", num, num.equals(selected) ? "selected" : "", nom));
            }
            return options.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String id = req.getParameter("id");
        String equipe1 = req.getParameter("equipe1");
        String equipe2 = req.getParameter("equipe2");
        String date = req.getParameter("date");
        String score1 = req.getParameter("score1");
        String score2 = req.getParameter("score2");

        if (id != null && equipe1 != null && equipe2 != null && date != null && score1 != null && score2 != null) {
            try {
                updateRencontre(Integer.parseInt(id), Integer.parseInt(equipe1), Integer.parseInt(equipe2), date, Integer.parseInt(score1), Integer.parseInt(score2));
                resp.sendRedirect("./listeRencontres");
            } catch (Exception e) {
                out.println("<p style='color:red'>Erreur lors de la modification : " + e.getMessage() + "</p>");
            }
        } else {
            out.println("<p style='color:red'>Tous les champs sont requis</p>");
        }
    }

    private void updateRencontre(int id, int eq1, int eq2, String date, int sc1, int sc2) {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        try (var conn = java.sql.DriverManager.getConnection(url); var stmt = conn.prepareStatement("UPDATE rencontres SET eq1 = ?, eq2 = ?, jour = ?, sc1 = ?, sc2 = ? WHERE num_match = ?")) {
            stmt.setInt(1, eq1);
            stmt.setInt(2, eq2);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            stmt.setInt(4, sc1);
            stmt.setInt(5, sc2);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification : " + e.getMessage(), e);
        }
    }


}
