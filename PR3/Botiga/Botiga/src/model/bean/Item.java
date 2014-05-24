package model.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {

	// properties
	private String id;
	private String url;
	private String image;
	private String name;
	private String description;
	private BigDecimal price;
	private String type;
	
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param img the img to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	// Usefull methods
    public boolean equals(Item other) {
        return (other instanceof Item) && (id != null) ? id.equals(other.id) : (other == this);
    }
    
    public int hashCode() {
        return (id != null) ? (getClass().hashCode() + id.hashCode()) : super.hashCode();
    }
    
    public String toString() {
        return String.format("%s - %s" , type, name);
    }
    
    public boolean isType(String type){
    	return this.type == type;
    }
    
	public String getPriceRounded(){
		 return  new BigDecimal(this.price.toString()).setScale(2, BigDecimal.ROUND_FLOOR).toString();
	}
}
