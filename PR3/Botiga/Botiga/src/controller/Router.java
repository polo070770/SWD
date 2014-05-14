package controller;
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
	// private static LaVostraBD laVostraBd = new LaVostraBD();
	// LOCATIONS ================================================
	public void locationProxy(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String CONTEXT = request.getContextPath();
		String location = request.getRequestURI();
		System.out.println("LOCATION :" + location);
		if (location.equals(CONTEXT + "/")) {
			response.sendRedirect(CONTEXT + "/catalogo");
			// processIndex(request, response);
		} else if (location.equals(CONTEXT + "/login")) {
			processLogin(request, response);
		} else if (location.equals(CONTEXT + "/carrito")) {
			processCarrito(request, response);
		} else if (location.equals(CONTEXT + "/micuenta")) {
			processMiCuenta(request, response);
		} else if (location.equals(CONTEXT + "/catalogo")) {
			processIndex(request, response);
		} else if (location.equals(CONTEXT + "/download/*")) {
			processDownload(request, response);
		} else if (location.equals(CONTEXT + "/error")) {
			processError(request, response);
		} else if (location.equals(CONTEXT + "/auth-error")) {
			processAuthError(request, response);
		} else { // error

			System.out.println("REDIRECT TO " + CONTEXT + "/error");
			response.sendRedirect(CONTEXT + "/error");
		}
	}

	// SERVLET ==================================================

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		locationProxy(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		locationProxy(request, response);
	}

	// PROCESS ==================================================
	public void processLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO si no esta validado se muestra el formulario, si esta validado
		// accede a la parte privada
		if (request.isUserInRole("Client")) {
			response.sendRedirect(request.getContextPath() + "/catalogo");
		} else {
			showLogin(request, response);
		}
	}

	public void processIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showIndex(request, response);
	}

	public void processCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showCarrito(request, response);
	}

	public void processMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showMiCuenta(request, response);
	}

	public void processDownload(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("item") != null) {
			// and user has bougth this item
			emitDownload(request, response, request.getParameter("item"));
		}

	}

	public void processAuthError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showAuthError(request, response);
	}

	public void processError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showPageInternalError(request, response);
	}

	// PAGES ====================================================
	public void showLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "login.jsp");
	}

	public void showIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "index.jsp");
	}

	public void showCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "carrito.jsp");
	}

	public void showMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "micuenta.jsp");
	}

	public void showPageInternalError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "error.jsp");
	}

	public void showAuthError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, "auth_error.jsp");
	}

	public void emitDownload(HttpServletRequest request,
			HttpServletResponse response, String item) throws ServletException,
			IOException {
		
		response.setContentType("image/png");
		
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc
				.getRequestDispatcher("/WEB-INF/media/" + item);
		
		rd.include(request, response);

	}

	public void showPage(HttpServletRequest request,
			HttpServletResponse response, String jspPage)
			throws ServletException, IOException {
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/WEB-INF/JSP/"
				+ jspPage);
		rd.forward(request, response);
	}
}