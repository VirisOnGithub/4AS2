package model;

import dao.PartieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/modifierPartie")
public class ModifiePartieServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pno = Integer.parseInt(req.getParameter("pno"));
        int jno1 = Integer.parseInt(req.getParameter("jno1"));
        int jno2 = Integer.parseInt(req.getParameter("jno2"));
        String date = req.getParameter("date");
        int statut = Integer.parseInt(req.getParameter("statut"));
        int temps = Integer.parseInt(req.getParameter("temps"));
        int gagnant = Integer.parseInt(req.getParameter("gagnant"));

        Partie partie = new Partie(pno, jno1, jno2, java.time.LocalDate.parse(date), statut, temps, gagnant);
        PartieDAO.update(partie);

        resp.sendRedirect("./partie?action=voir&partie=" + pno);
    }
}
