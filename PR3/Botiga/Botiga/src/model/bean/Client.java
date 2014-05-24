package model.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
	private String name;
	private BigDecimal credit;
	private List<Item> items = new ArrayList<Item>();
	
	//Getters
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the credit
	 */
	public BigDecimal getCredit() {
		return credit;
	}
	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}
	
	
	//setters
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	
	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
	// Usefull methods
    public boolean equals(Client other) {
        return (other instanceof Client) && (name != null) ? name.equals(other.name) : (other == this);
    }
    
    public int hashCode() {
        return (name != null) ? (getClass().hashCode() + name.hashCode()) : super.hashCode();
    }
    
    /**
     * devuelve si tiene credito
     * @return
     */
    public boolean hasCredit(){
    	return credit.signum() > 0;
    }
    
    /**
     * Devuelve si tiene suficiente credito
     * @param amount
     * @return
     */
    public boolean hasEnoughCredit(BigDecimal amount){
    	return credit.compareTo(amount) >= 0;
    }
	//Usefull methods
	/**
	 * agrega un item al carro
	 * @param item
	 */
	public void addItem(Item item){
		items.add(item);
	}
	
	/**
	 * devuelve si un item ya esta en el carro
	 * @param item
	 * @return
	 */
	public boolean hasItem(Item item){
		
		return items.contains(item);
	}
	
	
	public boolean hasItem(String id){
		for(Item i: items){
			if(i.getId().equals(id)) return true;
		}
		return false;
	}
	
	/**
	 * elimina un item del carro
	 */
	public void removeItem(Item item){
		items.remove(item);
	}
	
	public void substractCredit(BigDecimal amount){
		this.credit = this.credit.subtract(amount);
	}
	public String getCreditRounded(){
		 return  new BigDecimal(this.credit.toString()).setScale(2, BigDecimal.ROUND_FLOOR).toString();
	}
}
