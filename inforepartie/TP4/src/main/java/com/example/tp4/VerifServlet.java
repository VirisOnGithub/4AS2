package com.example.tp4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;


@WebServlet("/checkLogin")
public class VerifServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (checkLoginDatabase(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("login", username);
            session.setMaxInactiveInterval(10);

            response.sendRedirect("./menu");
        } else {
            response.sendRedirect("./login.html");
        }
    }

    private boolean checkLoginDatabase(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/webdb", "web_user", "motdepasse");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT login FROM Connexion WHERE login = ? AND mdp = digest(?, 'sha256')")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return false;
        }
    }
}
