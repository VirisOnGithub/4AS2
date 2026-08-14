package com.example.tp4;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/add_user")
public class AjouterUtilisateurServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            res.sendRedirect("./login.html");
            return;
        }

        if (checkIfAdmin(loginObj.toString())) {
            res.sendRedirect("./menu");
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
                <title>Ajouter un utilisateur</title>
                </head>
                <body>
                <form method="post" action="./add_user">
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newUsername = req.getParameter("username");
        String newPassword = req.getParameter("password");

        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            resp.sendRedirect("./login.html");
            return;
        }

        if (checkIfAdmin(loginObj.toString())) {
            resp.sendRedirect("./menu");
            return;
        }

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("INSERT INTO Connexion(login, mdp) VALUES (?, digest(?, 'sha256'))")) {
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                createRoleForUser(conn, newUsername);
                resp.sendRedirect("./menu");
            } else {
                resp.sendRedirect("./add_user?error=No+rows+updated");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            resp.sendRedirect("./add_user");
        }
    }

    private void createRoleForUser(Connection conn, String newUsername) {
        try (java.sql.PreparedStatement stmt = conn.prepareStatement("INSERT INTO Role(login, role) VALUES (?, 'user')")) {
            stmt.setString(1, newUsername);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error creating role for user: " + e.getMessage());
        }
    }
}
