import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Router extends HttpServlet {
	//private static LaVostraBD laVostraBd = new LaVostraBD();
	// LOCATIONS ================================================
	public void locationProxy(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String CONTEXT = request.getContextPath();
		String location = request.getRequestURI();
		

		if ( location.equals( CONTEXT + "/") ){
			processIndex(request, response); 
		}else if( location.equals( CONTEXT + "/login") ){
			processLogin(request, response); 
		}else{ // error
			System.out.println("error!");
			//processLogin(request, response); 
		}
	}
	
	
	// SERVLET ==================================================

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{ 
		locationProxy( request, response ); 
	
	}
	 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		locationProxy( request, response ); 
	}
	
	// PROCESS ==================================================
	public void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
			// TODO	si no esta validado se muestra el formulario, si esta validado accede a la parte privada
			showLogin(request, response);
	}
	
	public void processIndex(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
			// pagina principal
			showIndex(request, response);
	}	
	 
	// PAGES ====================================================
	public void showLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		showPage( request, response, "login.jsp" );
	}
	
	public void showIndex(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		showPage( request, response, "non-protected.jsp" );
	}
	
	public void showPageInternalError(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		showPage( request, response, "internalError.jsp" );
	}

	public void showPage(HttpServletRequest request, HttpServletResponse response, String jspPage)
			throws ServletException, IOException{
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher( "/WEB-INF/JSP/" + jspPage);
		rd.forward(request, response);
	}
}