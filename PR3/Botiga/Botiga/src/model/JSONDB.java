package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.MathContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import model.bean.Client;
import model.bean.Item;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONDB {
	public Constant constantes;
	private final String CLIENTS = "clients.json";
	private final String ITEMS = "items.json";
	private List<Item> items = new ArrayList<Item>();
	private List<Client> clients = new ArrayList<Client>();
	private JSONArray tableClients;
	private JSONArray tableItems;
	
	public JSONDB(String path){
		this.constantes = constantes.getInstance();
		loadTable(path, ITEMS);
		parseItems();
		loadTable(path, CLIENTS);
		parseClients();
	}
	
	private void loadTable(String path, String fileName){
		
		try {
    		BufferedReader br = new BufferedReader(new FileReader(path + fileName));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            String jsonString = null;
        	
        	
            while (( line = br.readLine() ) != null) {

                stringBuilder.append( line );
            }
            
            jsonString = stringBuilder.toString();
            if(fileName.equals(CLIENTS)){
            	this.tableClients = new JSONArray(jsonString);
            }else if(fileName.equals(ITEMS)){
            	this.tableItems = new JSONArray(jsonString);
            }
        }
        catch (Exception e) {
        	System.out.println( fileName + "resource not found");
        }
		
	}
	
	/**
	 * funcion que devuelve el item asociado a la id
	 * @param id
	 * @return
	 */
	public Item getItem(String id){
		for(Item i: items){
			if(i.getId().equals(id)) return i;
		}
		return null;
	}
	public Client getClient(String name){
		for(Client c: clients){
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	
	public boolean clientExists(String name){
		for(Client c: clients){
			if(c.getName().equals(name)) return true;
		}
		return false;
	}
	
	private void parseItems(){
		try{
			for (int i = 0; i < tableItems.length(); ++i) {
				JSONObject row = tableItems.getJSONObject(i);
	            
	            Item item = new Item();
	            item.setId(row.getString("id")); // recuperamos la id
	            item.setUrl(row.getString("url")); // recuperamos la url
	            item.setImage(row.getString("image")); // recuperamos la imagen
	            item.setName(row.getString("name")); // recuperamos el nombre
	            item.setDescription(row.getString("description")); // recuperamos la descripcion
	            item.setPrice(new BigDecimal(row.getDouble("price"), MathContext.DECIMAL64)); // recuperamos el precio
	            item.setType(row.getString("type")); // recuperamos el type
	
	            //añadimos el item
	            items.add(item);
			}
		}catch(Exception e) {
			if(constantes.DEBUG)e.printStackTrace();
        	System.out.println("unable to parse tableItems");
        }
	}
	
	
	private void parseClients(){
		try{
			for (int i = 0; i < tableClients.length(); ++i) {
				JSONObject row = tableClients.getJSONObject(i);
				Client client = new Client();
				client.setName(row.getString("name"));
				client.setCredit(new BigDecimal(row.getDouble("credit"), MathContext.DECIMAL64));
				JSONArray item_rows = row.getJSONArray("items");
				for(int j = 0; j < item_rows.length(); ++j){
					client.addItem(getItem(item_rows.getString(i)));
				}
				
				clients.add(client);
				
			}
		}catch(Exception e) {
        	System.out.println("unable to parse tableUsers");
        }
		
	}
	
	public List<Item>getItems(){
		return items;
	}
	
	public List<Client>getClients(){
		return clients;
	}
	
	public boolean containsItem(String id){
		for(Item i: items){
			if(i.getId().equals(id)) return true;
		}
		return false;
	}
	public void newClient(String name){
		try{
			JSONObject newUser = new JSONObject();
			newUser.append("name",name);
			newUser.append("credit", 500);
			newUser.append("items", new JSONArray());
			Client client = new Client();
			client.setName(name);
			client.setCredit(new BigDecimal(500, MathContext.DECIMAL64));
			client.setItems(new ArrayList<Item>());
			clients.add(client);
		}catch(Exception e) {
        	System.out.println("unable to create new user");
        }
	}
}
