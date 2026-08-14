package controleur;

import dao.DAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet("/controleur")
public class ControleurServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "default";
        }
        String vue;
        DAO dao = new DAO();

        switch (action) {
            case "liste":
                req.setAttribute("fruits", dao.findAll());
                vue = "liste.jsp";
                break;
            case "vignette":
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("fruit", dao.findById(id));
                vue = "vignette.jsp";
                break;
            default:
                vue = "404.jsp";
                break;
        }

        req.getRequestDispatcher("vue/" + vue).forward(req, res);
    }
}