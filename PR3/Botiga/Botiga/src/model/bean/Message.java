package model.bean;

public class Message {
	public static final String SUCCESS = "success";
	public static final String INFO = "info";
	public static final String WARNING = "warning";
	public static final String DANGER = "danger";
	
	private String text;
	private String type;
	
	
	/**
	 * @return the message
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param message the message to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public void fill (Message other){
		this.text = other.text;
		this.type = other.type;
	}
	

}
