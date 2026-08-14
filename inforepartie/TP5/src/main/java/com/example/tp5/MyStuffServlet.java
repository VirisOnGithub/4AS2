package com.example.tp5;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import metier.Stuff;

import java.io.IOException;

@WebServlet(name = "myStuffServlet", value = "/my-stuff")
public class MyStuffServlet extends HttpServlet {
    private static final String STUFF_KEY = "globalStuffCounter";

    private Stuff getOrCreateStuff() {
        ServletContext context = getServletContext();
        synchronized (context) {
            Stuff stuff = (Stuff) context.getAttribute(STUFF_KEY);
            if (stuff == null) {
                stuff = new Stuff();
                context.setAttribute(STUFF_KEY, stuff);
            }
            return stuff;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Stuff stuff = getOrCreateStuff();
        int value = stuff.incrementAndGet();

        request.setAttribute("counterMessage", stuff.buildMessage(value));
        request.getRequestDispatcher("/myStuff.jsp").forward(request, response);
    }
}

