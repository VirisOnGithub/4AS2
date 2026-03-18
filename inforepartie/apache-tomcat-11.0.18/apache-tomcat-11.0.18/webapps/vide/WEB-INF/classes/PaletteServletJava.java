import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.IntStream;

@WebServlet("/palette")
public class PaletteServletJava extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");

        String redComponentStr = req.getParameter("r");
        if (redComponentStr == null) {
            redComponentStr = "0";
        }
        int redComponent;
        try {
            redComponent = Integer.parseInt(redComponentStr);
        } catch (NumberFormatException e) {
            redComponent = 0;
        }

        if (redComponent < 0 || redComponent > 15) {
            redComponent = 0;
        }



        PrintWriter out = res.getWriter();
        out.println("""
            <!doctype html>
            <html lang="fr">
            <head>
                <meta charset="utf-8">
                <title>Palette de couleurs</title>
                <style>
                    table { border-collapse: collapse; }
                    td { width: 20px; height: 20px; }
                </style>
            </head>
            <body><h1>Palette de couleurs</h1>
            """
                + IntStream.rangeClosed(0, 15).mapToObj(e -> "<a href='palette?r=" + e + "'>" + e + "</a> ").reduce("", String::concat)
                + displayTab(redComponent) +
             """
            </body>
            </html>
            """);
    }
    public String displayTab(int redComponent) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        for (int i = 0; i < 16; i++) {
            sb.append("<tr>");
            for (int j = 0; j < 16; j++) {
                sb.append("<td bgcolor='#").append(Integer.toHexString(redComponent)).append(Integer.toHexString(i)).append(Integer.toHexString(j)).append("'>");
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}

