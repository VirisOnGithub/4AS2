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

@WebServlet("/supprimerRencontre")
public class SupprimerRencontreServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String id = req.getParameter("id");
        if (id != null) {
            try {
                deleteRencontre(Integer.parseInt(id));
                resp.sendRedirect("./listeRencontres");
            } catch (Exception e) {
                out.println("<p style='color:red'>Erreur lors de la suppression : " + e.getMessage() + "</p>");
            }
        } else {
            out.println("<p style='color:red'>ID de rencontre manquant</p>");
        }
    }

    private void deleteRencontre(int i) {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        try (var conn = java.sql.DriverManager.getConnection(url); var stmt = conn.prepareStatement("DELETE FROM rencontres WHERE num_match = ?")) {
            stmt.setInt(1, i);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage(), e);
        }
    }
}
