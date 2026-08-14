package com.example.tp4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            res.sendRedirect("./login.html");
            return;
        }

        String login = loginObj.toString();

        var isAdmin = checkIfAdmin(login);

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter out = res.getWriter();

        out.println(("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>
                    <h1>Bienvenue %s</h1>
                    <a href="./read">Consulter mon profil</a>
                    <a href="./edit">Modifier mes coordonnées</a>
                    <a href="./disconnect">Me déconnecter</a>
                    %s
                </body>
                </html>
                """).formatted(login, isAdmin ? "<a href=\"./add_user\">Ajouter un utilisateur</a><a href=\"./list\">Afficher la liste des utilisateurs</a>" : "")
        );
    }

    public boolean checkIfAdmin(String login) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT role FROM Role WHERE login = ?")) {
            stmt.setString(1, login);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return "admin".equals(rs.getString("role"));
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}