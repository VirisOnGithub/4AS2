package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/compteurSession")
public class CompteurSessionServlet extends HttpServlet {
    public static final int compteurGlobal = 53;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer compteur = (Integer) session.getAttribute("compteur");
        if (compteur == null) {
            compteur = 1;
        } else {
            compteur++;
        }
        session.setAttribute("compteur", compteur);
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println(String.format("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Compteur de Session</title>
                </head>
                <body><h1>Compteur de Session</h1>
                <p>Vous avez accédé %d fois à cette page sur les %d accès au total.</p>
                </body>
                </html>
                """, compteur, compteurGlobal));
    }
}
