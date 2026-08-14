package com.example.tp6;

import dao.JoueurDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import metier.Joueur;

import java.io.IOException;

@WebServlet("/joueur")
public class AfficheJoueurServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int jnoInput = Integer.parseInt(req.getParameter("jno"));

        Joueur j = JoueurDAO.findById(jnoInput);

        req.setAttribute("joueur", j);

        req.getRequestDispatcher("/joueur.jsp").forward(req, resp);
    }
}
