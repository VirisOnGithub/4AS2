package com.example.tp4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/status")
public class StatusServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();
        String login = (String) session.getAttribute("login");

        PrintWriter out = res.getWriter();

        out.println(("""
                <!doctype html>
                <html lang="fr">
                <head>
                <meta charset="utf-8">
                <title>Foot servlet</title>
                </head>
                <body>
                <p>
                """ +
                (login != null ? "Successful connection<br><a href=\"./disconnect\">Se déconnecter</a>" : "You can't even remember your username or your password you knobhead") +
                """
                </p>
                </body>
                </html>
                """)
        );
    }
}
