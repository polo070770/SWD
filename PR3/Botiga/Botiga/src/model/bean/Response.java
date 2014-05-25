package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Response {
	private List<Message> messages = new ArrayList<Message>();
	/**
	 * @return the items
	 */
	public List<Message> getMessages() {
		return messages;
	}
	
	
	/**
	 * @param items the items to set
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public int count(){
		return messages.size();
	}
	
	public void addMessage(String text, String type){
		Message message = new Message();
		message.setText(text);
		message.setType(type);
		messages.add(message);
	}
	
	public List<Message> getAll(){
		List<Message> to_return = new ArrayList<Message>();
		//devolvemos una copia y eliminamos los que hay
		for(Message m : messages){
			Message other = new Message();
			other.fill(m);
			to_return.add(other);
		}
		messages.clear();
		return to_return;
	}
}
