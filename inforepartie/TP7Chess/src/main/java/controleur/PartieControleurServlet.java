package controleur;

import dao.PartieDAO;
import dao.JoueurDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Partie;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/partie")
public class PartieControleurServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Optional.ofNullable(req.getParameter("action")).orElse("liste");
        String partieParam = req.getParameter("partie");

        String vue;
        try {
            switch (action) {
                case "voir":
                    Partie partieVoir = getPartieOrThrow(partieParam, req);
                    if (partieVoir == null) { // getPartieOrThrow already set error attribute
                        vue = "vue/404.jsp";
                        break;
                    }
                    req.setAttribute("partie", partieVoir);
                    req.setAttribute("joueur1", JoueurDAO.findById(partieVoir.getJno1()));
                    req.setAttribute("joueur2", JoueurDAO.findById(partieVoir.getJno2()));
                    vue = "vue/voir.jsp";
                    break;

                case "modifier":
                    Partie partieMod = getPartieOrThrow(partieParam, req);
                    if (partieMod == null) {
                        vue = "vue/404.jsp";
                        break;
                    }
                    req.setAttribute("partie", partieMod);
                    req.setAttribute("joueurs", JoueurDAO.findAll());
                    vue = "vue/modifier.jsp";
                    break;

                case "supprimer":
                    Integer idASupprimer = parseId(partieParam);
                    if (idASupprimer == null) {
                        req.setAttribute("error", "Identifiant de partie invalide pour la suppression.");
                        vue = "vue/404.jsp";
                        break;
                    }
                    PartieDAO.delete(idASupprimer);
                    req.setAttribute("parties", PartieDAO.findAll());
                    vue = "vue/liste.jsp";
                    break;

                case "liste":
                default:
                    req.setAttribute("parties", PartieDAO.findAll());
                    vue = "vue/liste.jsp";
                    break;
            }
        } catch (Exception e) {
            // log si vous avez un logger (ici stack trace minimale)
            e.printStackTrace();
            req.setAttribute("error", "Une erreur serveur est survenue.");
            vue = "vue/404.jsp";
        }

        req.getRequestDispatcher(vue).forward(req, resp);
    }

    private Integer parseId(String idParam) {
        if (idParam == null) return null;
        try {
            return Integer.valueOf(idParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Partie getPartieOrThrow(String idParam, HttpServletRequest req) {
        Integer id = parseId(idParam);
        if (id == null) {
            req.setAttribute("error", "Identifiant de partie manquant ou invalide.");
            return null;
        }
        Partie p = PartieDAO.findById(id);
        if (p == null) {
            req.setAttribute("error", "Partie introuvable pour l'id : " + id);
        }
        return p;
    }
}
