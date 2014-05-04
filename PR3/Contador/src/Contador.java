

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Contador
 */
@WebServlet("/contar")
public class Contador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int veces = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Contador() {
        super();
        // TODO Auto-generated constructor stub
    }

    private synchronized void counter(){
    	veces++;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		counter();
		
		String contextPath = request.getContextPath();
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");

		String title = "Contador";

		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1> Heu entrat " + veces + " cops </h1>");
		out.println("<a href='" + contextPath + "'> Sumar </a>");

		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
