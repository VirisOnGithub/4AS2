package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/monEquipe")
public class MonEquipeServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        if (req.getMethod().equals("POST")) {
            String clear = req.getParameter("clear");
            if (clear != null && clear.equals("true")) {
                Cookie teamCookie = new Cookie("teamId", "");
                teamCookie.setMaxAge(0); // Supprimer le cookie
                res.addCookie(teamCookie);
                res.sendRedirect("./monEquipe");
                return;
            }
            String teamId = req.getParameter("equipe");

            Cookie teamCookie = new Cookie("teamId", teamId);
            teamCookie.setMaxAge(60 * 60); // 1 heure
            res.addCookie(teamCookie);
            res.sendRedirect("./listeRencontres");
        }

        res.setContentType("text/html;charset=UTF-8");
        out.println(String.format("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Mon Equipe</title>
                </head>
                <body><h1>Mon Equipe</h1>
                <form method="POST">
                    <select name="equipe">
                        %s
                    </select>
                    <button type="submit">Montrer les matchs de mon équipe</button>
                </form>
                <form method="POST">
                    <button type="submit" name="clear" value="true">Effacer mon équipe</button>
                </form>
                </body>
                </html>
                """, String.join("\n", getAllTeams().stream().map(e -> String.format("<option value=%d>%s</option>", e.numEquipe, e.nomEquipe)).toList())));
    }

    private List<EquipeTuple> getAllTeams() {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        try (var conn = java.sql.DriverManager.getConnection(url); var stmt = conn.createStatement(); var rs = stmt.executeQuery("select num_equipe, nom_equipe from equipes;")) {
            List<EquipeTuple> equipes = new ArrayList<>();
            while (rs.next()) {
                equipes.add(new EquipeTuple(rs.getInt("num_equipe"), rs.getString("nom_equipe")));
            }
            return equipes;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
