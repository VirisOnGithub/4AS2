package tp_chess;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.github.bhlangonijr.chesslib.Board;
@WebServlet("/plateau")
public class DemoPlateau extends HttpServlet {
	Board board = new Board();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().print(board.toString());
	}
	
}
