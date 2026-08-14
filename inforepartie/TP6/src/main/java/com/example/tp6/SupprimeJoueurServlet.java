package com.example.tp6;

import dao.JoueurDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-player")
public class SupprimeJoueurServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int jno = Integer.parseInt(req.getParameter("jno"));

            if (jno <= 0) {
                resp.sendRedirect("./error.jsp");
            }

            JoueurDAO.delete(jno);
        } catch (NumberFormatException e) {
            resp.sendRedirect("./error.jsp");
        }
    }
}
