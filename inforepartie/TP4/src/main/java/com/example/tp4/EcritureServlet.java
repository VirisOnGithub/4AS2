package com.example.tp4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/edit")
public class EcritureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            res.sendRedirect("./login.html");
            return;
        }

        String login = loginObj.toString();

        String errorMessage = req.getParameter("error") != null ? req.getParameter("error") : "";

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter out = res.getWriter();

        out.println(("""
                <!doctype html>
                <html lang="fr">
                <head>
                <meta charset="utf-8">
                <title>Éditer un utilisateur</title>
                </head>
                <body>
                <form method="post" action="./edit">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" value="%s"><br><br>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" placeholder="cacatoutmou"><br><br>
                <input type="submit" value="Submit">
                </form>
                </p>
                <p style="color:red;">%s</p>
                </body>
                </html>
                """).formatted(login, errorMessage)
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newUsername = req.getParameter("username");
        String newPassword = req.getParameter("password");
        String currentLogin = req.getSession().getAttribute("login").toString();

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("UPDATE Connexion SET login = ?, mdp = digest(?, 'sha256') WHERE login = ?")) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setString(3, currentLogin);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                HttpSession session = req.getSession();
                session.setAttribute("login", newUsername);
                resp.sendRedirect("./status");
            } else {
                resp.sendRedirect("./edit?error=No+rows+updated");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            resp.sendRedirect("./edit");
        }
    }
}
