package com.example.tp6;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import metier.Joueur;

import java.io.IOException;

@WebServlet("/modify-player")
public class ModifierJoueurServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int jno = Integer.parseInt(req.getParameter("jno"));
        Joueur j = dao.JoueurDAO.findById(jno);
        req.setAttribute("joueur", j);
        req.getRequestDispatcher("./modify-player.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int jno = Integer.parseInt(req.getParameter("jno"));
        String pseudo = req.getParameter("pseudo");
        String email = req.getParameter("email");
        String pwd = req.getParameter("password");
        int elo = Integer.parseInt(req.getParameter("elo"));

        if (pseudo == null || email == null || pwd == null || elo <= 0) {
            resp.sendRedirect("./error.jsp");
        }

        Joueur j = new Joueur(jno, pseudo, email, pwd, elo);
        dao.JoueurDAO.update(j);
        resp.sendRedirect("./success.jsp");
    }
}