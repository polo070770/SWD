package model.bean;

import model.Constant.Url;

public class Urls {
	private String login;
	private String catalogo;
	private String micuenta;
	private String base;
	private String staticcontent;
	private String download;
	
	
	
	public void set(String context){
		base = context + Url.INDEX.toString();
		login = context + Url.LOGIN.toString();
		catalogo = context + Url.CATALOGO.toString();
		micuenta = context + Url.MICUENTA.toString();
		
		staticcontent = context + Url.STATIC.toString();
		download = context + Url.DOWNLOAD.toString();
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
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
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
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
	
	
	

}
