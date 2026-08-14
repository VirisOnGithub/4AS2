package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet("/joueursChoisis")
public class JoueursChoisisServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        Set<Integer> selectedPlayers = (Set<Integer>) session.getAttribute("selectedPlayers");
        if (selectedPlayers == null || selectedPlayers.isEmpty()) {
            out.println("<p>Aucun joueur sélectionné.</p>");
        } else {
            out.println("<h1>Joueurs Choisis</h1><ul>");
            for (Integer num_joueur : selectedPlayers) {
                out.println("<li>Numéro de joueur : " + num_joueur + "</li>");
            }
            out.println("</ul>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numJoueur = req.getParameter("num_joueur");
        if (numJoueur != null) {
            try {
                addPlayerToList(Integer.parseInt(numJoueur), req.getSession());
                resp.sendRedirect("./joueursChoisis");
            } catch (Exception e) {
                PrintWriter out = resp.getWriter();
                out.println("<p style='color:red'>Erreur lors de l'ajout du joueur : " + e.getMessage() + "</p>");
            }
        } else {
            PrintWriter out = resp.getWriter();
            out.println("<p style='color:red'>Numéro de joueur manquant</p>");
        }
    }

    private void addPlayerToList(int num_joueur, HttpSession session) {
        Set<Integer> selectedPlayers = (Set<Integer>) session.getAttribute("selectedPlayers");
        if (selectedPlayers == null) {
            selectedPlayers = new HashSet<>();
        }
        if (!selectedPlayers.contains(num_joueur)) {
            selectedPlayers.add(num_joueur);
            session.setAttribute("selectedPlayers", selectedPlayers);
        }
    }
}