import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/fibo")
public class FibonacciServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("""
            <!doctype html>
            <html lang="fr">
            <head>
                <meta charset="utf-8">
                <title>Première servlet</title>
            </head>
            <body>
            <h1>Servlet Fibonacci</h1>
            <p>30 premières valeurs de la suite de Fibonacci :</p>
            <ul>
            """);
        for (int f : fibo(30)) {
            out.println("<li>" + f + "</li>");
        }
        out.println("""
            </ul>
            </body>
            </html>
            """);
    }

    public int[] fibo(int n) {
        int[] f = new int[n];
        if (n > 0) f[0] = 1;
        if (n > 1) f[1] = 1;
        for (int i = 2; i < n; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f;
    }
}