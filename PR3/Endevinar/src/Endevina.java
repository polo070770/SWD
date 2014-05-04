

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Endevina
 */
@WebServlet("/endevina")
public class Endevina extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Endevina() {
    	super();
    }
    private void generateNombre(){
    	
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int numero_secret, numero_prova, tirades;
		String resultat;
		resultat = "";
		tirades = 0;
		String contextPath = request.getContextPath();
		// recuperamos las variables de session
		HttpSession session = request.getSession();
		
		// con session recogemos las variables de sesion que se guardan mientras la sesion del navegador este activa
		// con request recogemos los parametros de la url y otras cosas
		
		//reiniciamos el juego
		if(request.getParameter("reiniciar") != null){
			session.invalidate();
			response.sendRedirect(contextPath);
			return;
		}

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		
		// creamos la variable de sesion con el numero si es que no existe
		if(session.getAttribute("nombre-clau") == null ){
			Random rand = new Random();
	        numero_secret = rand.nextInt((10000 - 0) + 1) + 0; 
	        session.setAttribute("nombre-clau", numero_secret);
	        
		}else{
			numero_secret = (int)session.getAttribute("nombre-clau");
		} 
		

		// recuperamos el numero que nos han pasado si es que existe
		if(request.getParameter("numero") != null){
			// comprobamos la tirada o reiniciamos las tiradas
			if(session.getAttribute("tirades") == null){
				//primera tirada
		        session.setAttribute("tirades", 1);
		        tirades  = 1;
			}else{
				//tirada
				tirades = (int)session.getAttribute("tirades");
				session.setAttribute("tirades", tirades + 1 );
			} 
			// comprobamos que no este vacio el campo
			if(!request.getParameter("numero").equals("")){
				numero_prova = Integer.parseInt(request.getParameter("numero"));
			
				if(numero_prova > numero_secret) resultat = "El numero buscat es Menor";
				else if(numero_prova < numero_secret) resultat = "El numero buscat es Major";
				else resultat = "Ho has endevinat!, el numero era el " + numero_secret;
			}
		}

		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		
		String title = "Endevina";

		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>" + resultat + "</h1>");
		out.println("<h2>" + tirades + " tirades</h2>");
		out.println("<form action='"+contextPath+"' method='GET'>"
				+ "<input type='text' name='numero' placeholder='intrudieix un numero' />"
				+ "<input type='submit' value='probar sort' />"
				+ "</form>");
		
		out.println("<a href='" + contextPath + "?reiniciar'> Reiniciar </a>");
		// mostrem el numero secret per la consola javascript
		out.println("<script>console.log('Numero secret: " + numero_secret + "')</script>");
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
