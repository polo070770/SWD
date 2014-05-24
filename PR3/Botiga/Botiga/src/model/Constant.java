package model;

import java.util.HashMap;
import java.util.Map;

public final class Constant {
	
    private static Constant INSTANCE = new Constant();
    
    
    
    /* DECLARACION VARIABLES */
    public final boolean DEBUG;
    /* FIN VARIABLES */
    private Constant() {
    	DEBUG = true;
    }
 
    public static Constant getInstance() {
        return INSTANCE;
    }
    
    public enum Url{
    	LOGIN("/login"),
    	LOGOUT("/logout"),
    	INDEX("/"),
    	CARRITO("/carrito"),
    	MICUENTA("/mi-cuenta"),
    	DOWNLOAD("/download/"),
    	STATIC("/static/"),
    	MEDIA("/WEB-INF/media/"),
    	JSON("/WEB-INF/JSON/"),
    	ERROR("/error"),
    	AUTHERROR("/auth-error"),
    	CATALOGO("/catalogo"),
    	ADDITEM("/catalogo/add/"),
    	REMOVEITEM("/catalogo/remove/"),
    	EMPTYCART("/catalogo/empty-cart")
    	;
        private String url; 
        private Url(String url) { 
            this.url = url; 
        } 
        
        @Override 
        public String toString(){ 
            return url; 
        } 
        public String getName() {
            return url;
        }
    }
    public enum Jsp{
    	LOGIN("login.jsp"),
    	CARRITO("carrito.jsp"),
    	CATALOGO("catalogo.jsp"),
    	AUTHERROR("auth_error.jsp"),
    	ERROR("erro.jsp"),
    	INDEX("index.jsp"),
    	MICUENTA("micuenta.jsp");
        private String jsp; 
        
        private Jsp(String jsp) { 
            this.jsp = jsp; 
        } 
        
        @Override 
        public String toString(){ 
            return jsp; 
        } 
        public String getName() {
            return jsp;
        }
    }
    
    
    
}


