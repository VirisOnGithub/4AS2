package com.example.tp6;

import dao.JoueurDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import metier.Joueur;

import java.io.IOException;

@WebServlet("/add-player")
public class AjouteJoueurServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int jno = Integer.parseInt(req.getParameter("jno"));
            String pseudo = req.getParameter("pseudo");
            String email = req.getParameter("email");
            String pwd = req.getParameter("pwd");
            int elo = Integer.parseInt(req.getParameter("elo"));

            if (pseudo == null || email == null || pwd == null || elo <= 0 || JoueurDAO.findById(jno) != null) {
                resp.sendRedirect("./error.jsp");
            }

            Joueur j = new Joueur(jno, pseudo, email, pwd, elo);
            JoueurDAO.create(j);

            resp.sendRedirect("./success.jsp");
        } catch (NumberFormatException e) {
            resp.sendRedirect("./error.jsp");
        }
    }
}
