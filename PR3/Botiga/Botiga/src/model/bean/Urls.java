package model.bean;

import model.Constant.Url;

public class Urls {
	private String login;
	private String logout;
	private String catalogo;
	private String micuenta;
	private String base;
	private String staticcontent;
	private String download;
	private String carrito;
	private String addItem;
	private String removeItem;
	private String emptyCart;
	
	
	
	public void set(String context){
		base = context + Url.INDEX.toString();
		login = context + Url.LOGIN.toString();
		logout = context + Url.LOGOUT.toString();
		catalogo = context + Url.CATALOGO.toString();
		micuenta = context + Url.MICUENTA.toString();
		carrito = context + Url.CARRITO.toString();
		staticcontent = context + Url.STATIC.toString();
		download = context + Url.DOWNLOAD.toString();
		addItem = context + Url.ADDITEM.toString();
		removeItem = context + Url.REMOVEITEM.toString();
		emptyCart = context + Url.EMPTYCART.toString();
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @return the logout
	 */
	public String getLogout() {
		return logout;
	}
	/**
	 * @return the login
	 */
	public String getCarrito() {
		return carrito;
	}
	/**
	 * @return the catalogo
	 */
	public String getCatalogo() {
		return catalogo;
	}
	
	/**
	 * @return the micuenta
	 */
	public String getMicuenta() {
		return micuenta;
	}
	/**
	 * @return the base
	 */
	public String getBase() {
		return base;
	}
	/**
	 * @return the staticcontent
	 */
	public String getStaticcontent() {
		return staticcontent;
	}
	/**
	 * @return the download
	 */
	public String getDownload() {
		return download;
	}
	/**
	 * @return the 
	 */
	public String getAddItem() {
		return addItem;
	}
	/**
	 * @return the 
	 */
	public String getRemoveItem() {
		return removeItem;
	}
	
	/**
	 * @return the 
	 */
	public String getEmptyCart() {
		return emptyCart;
	}
	
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @paramlogout
	 */
	public void setLogout(String logout) {
		this.login = logout;
	}
	
	/**
	 * @param login the login to set
	 */
	public void setCarrito(String carrito) {
		this.login = carrito;
	}
	/**
	 * @param catalogo the catalogo to set
	 */
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	/**
	 * @param micuenta the micuenta to set
	 */
	public void setMicuenta(String micuenta) {
		this.micuenta = micuenta;
	}
	/**
	 * @param base the base to set
	 */
	public void setBase(String base) {
		this.base = base;
	}
	/**
	 * @param staticcontent the staticcontent to set
	 */
	public void setStaticcontent(String staticcontent) {
		this.staticcontent = staticcontent;
	}
	/**
	 * @param download the download to set
	 */
	public void setDownload(String download) {
		this.download = download;
	}
	/**
	 * @param d
	 */
	public void setAddItem(String addItem) {
		this.addItem = addItem;
	}
	
	/**
	 * @param d
	 */
	public void setRemoveItem(String removeItem) {
		this.removeItem = removeItem;
	}
	/**
	 * @param d
	 */
	public void setEmptyCart(String emptyCart) {
		this.emptyCart = emptyCart;
	}
}
