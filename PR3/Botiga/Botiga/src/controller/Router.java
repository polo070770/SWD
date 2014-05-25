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
import model.bean.Client;
import model.bean.Item;
import model.bean.Message;
import model.bean.Response;
import model.bean.Urls;

public class Router extends HttpServlet {
	
	private JSONDB db;
	private RegexParser regParser;
	private Downloader downloader;
	private Constant constantes;
	private boolean deploy = true;
	
	public  Router(){}
	
	


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
			
			// url en la que añadimos item al carro
		} else if (location.contains(CONTEXT + Url.ADDITEM)) { 	
			processAddItem(request, response);
			// url en la que añadimos item al carro
		} else if (location.contains(CONTEXT + Url.REMOVEITEM)) { 	
			processRemoveItem(request, response);	
		// procesamos peticiones de contenido estatico
		} else if (location.contains(CONTEXT + Url.STATIC)) {	
			processStaticContent(request, response);
		
		} else if (location.equals(CONTEXT + Url.LOGIN)) {
			processLogin(request, response);
		
		} else if (location.equals(CONTEXT + Url.LOGOUT)) {
			processLogout(request, response);	
			// procesamos peticiones de descarga
		} else if (location.contains(CONTEXT + Url.DOWNLOAD)) {
			processDownload(request, response);
			
		} else if (location.equals(CONTEXT + Url.CARRITO)) {
			processCarrito(request, response);
			
		} else if (location.equals(CONTEXT + Url.MICUENTA)) {
			processMiCuenta(request, response);
			
		} else if (location.equals(CONTEXT + Url.EMPTYCART)) {
			processEmptyCart(request, response);
			
		} else if (location.equals(CONTEXT + Url.CATALOGO)) {
			processCatalog(request, response);

		} else if (location.equals(CONTEXT + Url.AUTHERROR)) {
			processAuthError(request, response);
			
		} else { // error
			raise404(request, response);
		}
	}

	// SERVLET ==================================================

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// si no hemos creado todo el entorno
		if(deploy){
			deployRouter();
		}
		locationProxy(request, response);

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String CONTEXT = request.getContextPath();
		HttpSession session = request.getSession();
		String location = request.getRequestURI();

		String tokenRequest, tokenSession;
		tokenRequest = request.getParameter("token_carrito");
		tokenSession = (String)session.getAttribute("token_carrito");
		
		// si es el formulario del carrito
		if (location.equals(CONTEXT + Url.CARRITO) && 
				// con esto controlamos que los formularios vengan de nuestra pagina
				tokenRequest.equals(tokenSession)) {  
			
			Response messages;
			Cart carrito;
			deploySession(request, response);
			
			carrito = (Cart)session.getAttribute("carrito");
			messages = (Response)session.getAttribute("messages");
			// recuperamos el usuario
			Client user = db.getClient(request.getUserPrincipal().getName());
			boolean almenos_uno = false;
			if(user.hasEnoughCredit(carrito.getAmount())){
				
				
				for(Item item :carrito.getItems()){
					if(!user.hasItem(item)){
						user.addItem(item);
						user.substractCredit(item.getPrice());
						almenos_uno = true;
					}else{
						String respuesta = "El item " + item.getName() + " ya se encuentra en tu cuenta, no se incluye "
								+ "la compra final (tampoco se te descontara el precio)";
						
						messages.addMessage(respuesta, Message.WARNING);
					}

				}
				if(almenos_uno){ // si almenos ha comprado un elemento
					messages.addMessage("Compra realizada con exito", Message.SUCCESS);
				}else{
					messages.addMessage("No has comprado ningun item, es posible que ya los hubieras comprado con "
							+ "anterioridad", Message.DANGER);
				}
				carrito.emptyCart();
				response.sendRedirect(CONTEXT + Url.MICUENTA);
			}else{ // no hay pasta pa comprar todo
				messages.addMessage("No se ha podido realizar la compra, no tienes suficiente dinero", Message.DANGER);
				response.sendRedirect(CONTEXT + Url.CARRITO);
			}
		}else{
			if(!request.isUserInRole("Client")){
				response.sendRedirect(CONTEXT + Url.AUTHERROR);
			}
		}
		
		
	}

	// PROCESS ==================================================

	public void processLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO si no esta validado se muestra el formulario, si esta validado
		// accede a la parte privada
		if (request.isUserInRole("Client")) {
			response.sendRedirect(request.getContextPath() + Url.CATALOGO);
		} else {
			HttpSession session = request.getSession();
			String token = getFormToken("login");
			request.setAttribute("token_login", token);
			session.setAttribute("token_login",token);
			showLogin(request, response);
		}
	}
	
	/**
	 * finaliza la session por parte de usuario pero mantiene la de carrito y mensajes
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processLogout(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(request.getContextPath() + Url.CATALOGO);
	}
	
	public void processIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina principal
		showIndex(request, response);
	}
	
	/**
	 * añade item al carro
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processAddItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String CONTEXT = request.getContextPath();
		String location = request.getRequestURI();
		HttpSession session = request.getSession();
		
		String itemName;
		Cart carrito;
		Response messages;
		deploySession(request, response);
		
		carrito = (Cart)session.getAttribute("carrito");
		messages = (Response)session.getAttribute("messages");
		if( (itemName = regParser.getItemName(location)) != null){
			
			// recuperamos el item
			if(this.db.containsItem(itemName)){
				Item item = this.db.getItem(itemName);
				//comprobamos que no esta en el carrito
				if(!carrito.inCart(item)){
					carrito.addItem(item);
					messages.addMessage(item.getName() + " añadido del carrito", Message.SUCCESS);
				}else{
					messages.addMessage(item.getName() + " ya esta en el carrito", Message.WARNING);
				}
			}else{
				messages.addMessage(itemName + " no se encuentra en el catalogo", Message.WARNING);
			}
			
		}else{
			raise404(request, response);
		}
		// una vez hemos añadido al carro, redirigimos al catalogo
		response.sendRedirect(CONTEXT + Url.CATALOGO.toString());
		
	}
	
	/**
	 * elimina item del carro
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processRemoveItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String CONTEXT = request.getContextPath();
		String location = request.getRequestURI();
		HttpSession session = request.getSession();
		
		String itemName;
		Cart carrito;
		Response messages;
		deploySession(request, response);
		
		carrito = (Cart)session.getAttribute("carrito");
		messages = (Response)session.getAttribute("messages");
		if( (itemName = regParser.getItemName(location)) != null){
			
			// recuperamos el item
			if(this.db.containsItem(itemName)){
				Item item = this.db.getItem(itemName);
				//comprobamos que no esta en el carrito
				if(carrito.inCart(item)){
					carrito.removeItem(item);
					messages.addMessage(item.getName() + " eliminado del carrito", Message.SUCCESS);
				}
				
			}
	
		}else{raise404(request, response);}
		// una vez hemos añadido al carro, redirigimos al catalogo
		response.sendRedirect(CONTEXT + Url.CARRITO.toString());
		
	}
	
	/**
	 * vacia el carrito
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processEmptyCart(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String CONTEXT = request.getContextPath();
		HttpSession session = request.getSession();

		Cart carrito;
		Response messages;
		deploySession(request, response);
		
		carrito = (Cart)session.getAttribute("carrito");
		messages = (Response)session.getAttribute("messages");
		carrito.emptyCart();
		messages.addMessage("Se ha vaciado el carrito", Message.SUCCESS);
		
		
		response.sendRedirect(CONTEXT + Url.CARRITO.toString());
		
	}
	
	
	
	public void processCatalog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Cart carrito;
		deploySession(request, response);
		carrito = (Cart)session.getAttribute("carrito");
		
		//añadimos al request lo que necesitamos para mostrar
		request.setAttribute("carrito", carrito);
		request.setAttribute("catalog",db.getItems());
		showCatalog(request, response);
	}
	
	
	public void processCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		// pagina carrito
		deploySession(request, response);
		String carrito_token = getFormToken("carrito");
		
		request.setAttribute("token_carrito", carrito_token);
		session.setAttribute("token_carrito",carrito_token);
		showCarrito(request, response);
	}
	

	public void processMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// pagina micuenta
		deploySession(request, response);		
		showMiCuenta(request, response);
	}
	

	public void processDownload(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String location = request.getRequestURI();
		HttpSession session = request.getSession();
		boolean downloaded = false;
		String itemName;
		Client client;
		
		deploySession(request, response);
		
		if(!request.isUserInRole("Client")){
			raise404(request, response);
		}else if((client = (Client)session.getAttribute("client")) == null ){
			raise404(request, response);
		}else{
			//recuperamos el nombre del item
			if( (itemName = regParser.getItemName(location)) != null){
				// si el item existe y lo tiene el cliente
				if(client.hasItem(itemName) && this.db.containsItem(itemName)){
					Item item = this.db.getItem(itemName);
					downloaded = true;
					emitDownload(request, response, item.getUrl());
				}else{
					if (!downloaded)raise404(request, response);
				}
			}else{
				// si no hemos conseguido emitir la descarga
				if (!downloaded)raise404(request, response);
			}
		}
		
	}
	
	
	public void processStaticContent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String location = request.getRequestURI();
		String itemName;
		
		if( (itemName = regParser.getStaticUrl(Url.STATIC.toString(),  location)) != null){
			emitStaticContent(request, response, itemName);
		}else{
			raise404(request, response);
		}
		
	}
	

	public void processAuthError(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String CONTEXT = request.getContextPath();
		HttpSession session = request.getSession();
		Response messages;
		deploySession(request, response);
		messages = (Response)session.getAttribute("messages");
		messages.addMessage("Datos de acceso incorrectos" , Message.DANGER);

		response.sendRedirect(CONTEXT + Url.CARRITO.toString());
		
	}

	// PAGES ====================================================


	public void showLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("page_title","Login");
		showPage(request, response,Jsp.LOGIN.toString());
	}
	
	public void showIndex(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		showPage(request, response, Jsp.INDEX.toString());
	}
	
	public void showCatalog(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("page_title","Catalogo");
		showPage(request, response, Jsp.CATALOGO.toString());
	}
	
	
	public void showCarrito(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("page_title","Carrito");
		showPage(request, response, Jsp.CARRITO.toString());
	}

	public void showMiCuenta(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("page_title","Mi cuenta");
		showPage(request, response, Jsp.MICUENTA.toString());
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
		
		// ponemos la codificacion a utf-8
		request.setCharacterEncoding("UTF-8");
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/WEB-INF/JSP/"
				+ jspPage);
		
		// añadimos los beans al request que siempre necesitaremos
		deployGeneralRequest(request, response);

		rd.forward(request, response);
	}
	
	/**
	 * Desplega las variables necesarias para el router
	 */
	private void deployRouter(){
		this.constantes = constantes.getInstance();
		String mediaPath = getServletContext().getRealPath("")  + Url.MEDIA;
		String staticPath = getServletContext().getRealPath("")  + Url.STATIC;
		this.downloader = new Downloader(mediaPath, staticPath);
		this.db = new JSONDB(getServletContext().getRealPath("") + Url.JSON);
		this.regParser = new RegexParser();
		// solo se hace la primera vez		
		this.deploy = false;
	}
	
	private void deploySession(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//creamos el entorno de sesion
		HttpSession session = request.getSession();
		Cart carrito;
		Response messages;
		//sino tenemos carro lo creamos
		if(session.getAttribute("carrito") == null){
			carrito = new Cart();
			session.setAttribute("carrito", carrito);	
		}
		
		if(session.getAttribute("messages") == null){
			messages = new Response();
			session.setAttribute("messages", messages);	
		}
		
		// si hay cliente y no esta validado
		if(!request.isUserInRole("Client")){
			session.removeAttribute("client");
		}else if(session.getAttribute("client") == null){
			// si es user nuevo lo creamos
			if(	!db.clientExists(request.getUserPrincipal().getName())){
				db.newClient(request.getUserPrincipal().getName());
			}
			Client client= db.getClient(request.getUserPrincipal().getName());
			session.setAttribute("client", client);	
			// le damos la bienvenida
			messages = (Response)session.getAttribute("messages");
			messages.addMessage("Bienvenido " + client.getName() + "!", Message.INFO);
		}

		
	}
	
	private void deployGeneralRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(constantes.DEBUG){
			System.out.println("Deploying request");
		}
		HttpSession session = request.getSession();
		//siempre devolvemos las urls
		Urls urls = new Urls();
		urls.set(request.getContextPath());
		request.setAttribute("URLS",urls);
		
		// datos del cliente
		if(request.isUserInRole("Client") && session.getAttribute("client") != null){

			request.setAttribute("client", session.getAttribute("client"));
		}
		//responses
		if(session.getAttribute("messages") != null){
			if(constantes.DEBUG){
				System.out.println("Añadiendo messages al request");
			}
			Response messages = (Response)session.getAttribute("messages");
			request.setAttribute("messages", messages);
		}

	}
	
	private String getFormToken(String init){
		// aqui podriamos hacer un hash con cualquier cosa
		// para securizar los formularios
		return init + "-fake-token";
		
	}
}