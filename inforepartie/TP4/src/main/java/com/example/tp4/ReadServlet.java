package com.example.tp4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/read")
public class ReadServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            res.sendRedirect("./login.html");
            return;
        }

        String login = loginObj.toString();

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter out = res.getWriter();

        out.println(("""
                <!doctype html>
                <html lang="fr">
                <head>
                <meta charset="utf-8">
                <title>Read Data</title>
                </head>
                <body>
                <table border="1">
                <thead>
                <td>Login</td>
                <td>Password</td>
                <td>Creation Date</td>
                </thead>
                %s
                </table>
                </body>
                </html>
                """).formatted(displayUserData(login))
        );
    }

    private String displayUserData(String login) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT login, mdp, date_creation FROM Connexion WHERE login = ?")) {
            stmt.setString(1, login);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("<tr><td>")
                            .append(rs.getString("login"))
                            .append("</td><td>")
                            .append(rs.getString("mdp"))
                            .append("</td><td>")
                            .append(rs.getDate("date_creation"))
                            .append("</td><tr/>");
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "<tr><td colspan=\"2\">Error fetching data: " + e.getMessage() + "</td></tr>";
        }
    }
}
