import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/joueurs")
public class ListeJoueursServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println(
            """
                <!doctype html>
                <html lang="fr">
                <head>
                    <meta charset="utf-8">
                    <title>Joueurs</title>
                </head>
                <body><h1>Liste des Joueurs</h1>
                """ +
                displayTab() +
                """
                </body>
                </html>
                """
        );
    }

    public String displayTab() {
        String url = "jdbc:postgresql://localhost:5432/devdb?user=webuser&password=webpwd";
        StringBuilder sb = new StringBuilder();
        try (
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM joueurs j join equipes e on j.club = e.num_equipe")
        ) {
            sb.append("<table border='1'>");
            sb.append(
                "<tr><th>ID Joueur</th><th>Nom Joueur</th><th>Pays</th><th>Poste</th><th>Maillot</th><th>Date de Naissance</th><th>Nom Equipe</th><th>Salaire</th></tr>"
            );

            while (rs.next()) {
                int id = rs.getInt("num_joueur");
                String nom = rs.getString("nom_joueur");
                String pays = rs.getString("pays");
                String poste = rs.getString("poste");
                int maillot = rs.getInt("maillot");
                Date dateNaissance = rs.getDate("date_naissance");
                String nomEquipe = rs.getString("nom_equipe");
                int salaire = rs.getInt("salaire");

                sb
                    .append("<tr><td>")
                    .append(id)
                    .append("</td><td>")
                    .append(nom)
                    .append("</td><td>")
                    .append(pays)
                    .append("</td><td>")
                    .append(poste)
                    .append("</td><td>")
                    .append(maillot)
                    .append("</td><td>")
                    .append(dateNaissance)
                    .append("</td><td>")
                    .append(nomEquipe)
                    .append("</td><td>")
                    .append(salaire)
                    .append("</td></tr>");
            }

            sb.append("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            sb.append("<p style='color:red'>Erreur SQL : ").append(e.getMessage()).append("</p>");
        }
        return sb.toString();
    }
}
