package model.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
	private List<Item> items = new ArrayList<Item>();
	private BigDecimal amount = BigDecimal.ZERO;
	
	
	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	//Usefull methods
	/**
	 * agrega un item al carro
	 * @param item
	 */
	public void addItem(Item item){
		items.add(item);
		amount = amount.add(item.getPrice());
	}
	
	/**
	 * devuelve si un item ya esta en el carro
	 * @param item
	 * @return
	 */
	public boolean inCart(Item item){
		
		return items.contains(item);
	}
	/**
	 * elimina un item del carro
	 */
	public void removeItem(Item item){
		items.remove(item);
		amount = amount.subtract(item.getPrice());
	}
	
	public boolean isEmpty(){
		return items.isEmpty();
	}
	
	public void emptyCart(){
		items.clear();
		amount = BigDecimal.ZERO;
	}
	public int getNumItems(){
		return items.size();
	}
	public String getAmountRounded(){
		 return  new BigDecimal(this.amount.toString()).setScale(2, BigDecimal.ROUND_FLOOR).toString();
	}
}
