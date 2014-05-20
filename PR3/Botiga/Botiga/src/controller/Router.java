package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Constant;
import model.Constant.Url;
import model.Constant.Jsp;
import model.JSONDB;
import model.bean.Cart;
import model.bean.Item;
import model.bean.Urls;

public class Router extends HttpServlet {
	public Constant constantes;
	private JSONDB db;
	public  Router(){
		this.constantes = constantes.getInstance();
		this.db = null;
		
	}
	private Downloader downloader;
	

	// private static LaVostraBD laVostraBd = new LaVostraBD();
	// LOCATIONS ================================================
	public void locationProxy(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String CONTEXT = request.getContextPath();
		String location = request.getRequestURI();
		
		if(constantes.DEBUG)
			System.out.println("REQUEST LOCATION :" + location);
		
		// indice
		if (location.equals(CONTEXT + Url.INDEX)) {
			response.sendRedirect(CONTEXT + Url.CATALOGO);
			
		// procesamos peticiones de contenido estatico
		} else if (location.contains(CONTEXT + Url.STATIC)) {	
			processStaticContent(request, response);
		
			// procesamos peticiones de descarga
		} else if (location.contains(CONTEXT + Url.DOWNLOAD)) {
			processDownload(request, response);
		} else if (location.equals(CONTEXT + Url.LOGIN)) {
			processLogin(request, response);
		} else if (location.equals(CONTEXT + Url.CARRITO)) {
			processCarrito(request, response);
		} else if (location.equals(CONTEXT + Url.MICUENTA)) {
			processMiCuenta(request, response);
		} else if (location.equals(CONTEXT + Url.CATALOGO)) {
			processCatalog(request, response);

		} else if (location.equals(CONTEXT + Url.ERROR)) {
			processError(request, response);
		} else if (location.equals(CONTEXT + Url.AUTHERROR)) {
			processAuthError(request, response);
		} else { // error

			System.out.println("REDIRECT TO " + CONTEXT + Url.ERROR);
			response.sendRedirect(CONTEXT + Url.ERROR);
		}
	}

	// SERVLET ==================================================

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if(this.downloader == null){
			String mediaPath = getServletContext().getRealPath("")  + Url.MEDIA;
			String staticPath = getServletContext().getRealPath("")  + Url.STATIC;
			this.downloader = new Downloader(mediaPath, staticPath);
			this.db = new JSONDB(getServletContext().getRealPath("") + Url.JSON);
		}
		
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
			response.sendRedirect(request.getContextPath() + Url.CATALOGO);
		} else {
			showLogin(request, response);
		}
	}
	public void processIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showIndex(request, response);
	}
	public void processCatalog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Cart carrito;
		
		// datos del cliente
		if(request.isUserInRole("Client")){
			request.setAttribute("user", db.getClient(request.getUserPrincipal().getName()));
		}
		
		//sino tenemos carro lo creamos
		if(session.getAttribute("carrito") == null){
			carrito = new Cart();
			session.setAttribute("carrito", carrito);	
		}else{carrito = (Cart)session.getAttribute("carrito");}
		
		// añadimos el producto al carrito si existe el parametro add
		if(request.getParameter("add") != null){
			String sItem = request.getParameter("add");
			// recuperamos el item
			if(this.db.containsItem(sItem)){
				Item item = this.db.getItem(sItem);
				//comprobamos que no esta en el carrito
				if(!carrito.inCart(item))carrito.addItem(item);
			}
		}

		request.setAttribute("carrito", carrito);
		request.setAttribute("catalog",db.getItems());
		showCatalog(request, response);
	}
	public void processCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina carrito
		showCarrito(request, response);
	}

	public void processMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina micuenta
		showMiCuenta(request, response);
	}

	public void processDownload(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String location = request.getRequestURI();

		//Pattern regex = Pattern.compile("download/(\\w*\\.\\w*)");
		//capturamos el nombre del archivo
		Pattern regex = Pattern.compile("download/(\\w*\\.+\\w*)");
		Matcher m = regex.matcher(location);
		//capturem l'arxiu
		String matched = null;
		while (m.find()) {
			   matched= m.group(1);
		}
		
		if (matched != null) {
			// and user has bougth this item
			System.out.println("Downloading: " + matched);
			emitDownload(request, response, matched);
		}

	}
	
	public void processStaticContent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String location = request.getRequestURI();
		//capturamos el nombre del archivo
		Pattern regex = Pattern.compile(Url.STATIC + "([A-Za-z0-9._%+-\\/]*)");
		Matcher m = regex.matcher(location);
		//capturem l'arxiu
		String matched = null;
		while (m.find()) {
			   matched= m.group(1);
			   if(constantes.DEBUG){
				   System.out.println(matched);
			   }
		}
		
		if (matched != null) {
			// and user has bougth this item
			emitStaticContent(request, response, matched);
		}else{
			raise404(request, response);
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
		showPage(request, response,Jsp.LOGIN.toString());
	}

	/**
	 * Pagina principal del catalogo
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void showIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.INDEX.toString());
	}
	public void showCatalog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.CATALOGO.toString());
	}
	public void showCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.CARRITO.toString());
	}

	public void showMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.MICUENTA.toString());
	}

	public void showPageInternalError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.ERROR.toString());
	}

	public void showAuthError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.AUTHERROR.toString());
	}

	/**
	 * Funcion que devuelve un error 404
	 */
	public void raise404(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(constantes.DEBUG)
			System.out.println("raise 404");
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	public void emitDownload(HttpServletRequest request,
			HttpServletResponse response, String item) throws ServletException,
			IOException {
			downloader.stream(item, request, response, getServletConfig().getServletContext());
	}
	public void emitStaticContent(HttpServletRequest request,
			HttpServletResponse response, String item) throws ServletException,
			IOException {
			downloader.streamStatic(item, request, response, getServletConfig().getServletContext());
	}
	public void showPage(HttpServletRequest request,
			HttpServletResponse response, String jspPage)
			throws ServletException, IOException {
		
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/WEB-INF/JSP/"
				+ jspPage);
		
		//siempre devolvemos las urls
		Urls urls = new Urls();
		urls.set(request.getContextPath());
		request.setAttribute("URLS",urls);
		
		rd.forward(request, response);
	}
}