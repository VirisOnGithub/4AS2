package com.example.tp3;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/dummy")
public class DummyServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        res.setContentType("text/html;charset=UTF-8");
        out.println("""
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Mon Equipe</title>
                </head>
                <body><h1>Mon Equipe</h1>
                <p>Voici les membres de mon équipe :</p>
                <ul>
                    <li>Jean Dupont</li>
                    <li>Marie Curie</li>
                    <li>Albert Einstein</li>
                    <li>Isaac Newton</li>
                </ul>
                </body>
                </html>
                """);
    }
}
