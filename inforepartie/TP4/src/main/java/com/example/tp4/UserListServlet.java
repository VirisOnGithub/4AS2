package com.example.tp4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/list")
public class UserListServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        Object loginObj = session.getAttribute("login");

        if (loginObj == null) {
            res.sendRedirect("./login.html");
            return;
        }

        String login = loginObj.toString();

        PrintWriter out = res.getWriter();

        out.println(("""
                <!doctype html>
                <html lang="fr">
                <head>
                <meta charset="utf-8">
                <title>Jean-Paul ROUVE</title>
                </head>
                <body>
                <table border='1'>
                <thead>
                <td>Login</td>
                <td>Creation Date</td>
                <td>Role</td>
                </thead>
                %s
                </table>
                </body>
                </html>
                """).formatted(displayUsers(login))
        );
    }

    private String displayUsers(String login) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT c.login, c.date_creation, r.role FROM Connexion c, Role r WHERE c.login = r.login")) {
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    String dbLogin = rs.getString("login");
                    String dbRole = rs.getString("role");

                    if (login.equals(dbLogin) && !dbRole.equals("admin")) throw new IllegalArgumentException("You ain't an admin get out of here dumbass");

                    sb.append("<tr><td>")
                            .append(dbLogin)
                            .append("</td><td>")
                            .append(rs.getString("date_creation"))
                            .append("</td><td>")
                            .append(dbRole)
                            .append("</td><tr/>");
                }
                return sb.toString();
            }
        } catch (Exception e) {
            return "<tr><td colspan=\"2\">Error fetching data: " + e.getMessage() + "</td></tr>";
        }
    }
}
