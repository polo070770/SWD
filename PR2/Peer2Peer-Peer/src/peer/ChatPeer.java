package peer;

import interficie.peer.Peer2Peer;
import interficie.peer.Peer2Server;
import interficie.server.ChatDaemonInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;

import main.Configuration;
import utils.MD5;
import views.AddClientsToGroupDialog;
import views.ChatWindow;
import views.DialogWindow;
import views.GroupChatWindow;
import views.MainWindow;
import views.NewGroupChatDialog;
import collection.SyncPeerList;

import java.util.concurrent.ConcurrentHashMap;
public class ChatPeer extends UnicastRemoteObject implements Peer2Server,Peer2Peer {

	private SyncPeerList contacts;
	private ChatDaemonInterface server;
	private MainWindow window;
	private String peerName;
	private ConcurrentHashMap<String, ChatWindow> chats;
	private ConcurrentHashMap<String, GroupChatWindow> groupChats;
	private ChatWindow newChat;
	private Configuration config;
	private MD5 md5;

	public ChatPeer(ChatDaemonInterface server, String nombre)
			throws RemoteException {
		
		this.contacts = new SyncPeerList();
		this.server = server;
		this.peerName = nombre;
		this.chats = new ConcurrentHashMap<String, ChatWindow>();
		this.groupChats = new ConcurrentHashMap<String, GroupChatWindow>();
		this.config = Configuration.getInstance();
		this.md5 = new MD5();
	}
	
	/**
	 * Funcion que inicia el Chat y sus componentes
	 */
	public void go() {
		window = new MainWindow(this, this.peerName);
		System.out.println(this.peerName + " conectado");
		this.addContacts();
	}


	/**
	 * Funcion que solicita al servidor la desconexion
	 */
	public void finishConection() {
		
		// desconectamos de los grupos
		this.finishGroupConnections();

		try {
			this.server.unregisterPeer(this.peerName);
			System.out.println("Desconectando " + this.peerName);
		} catch (RemoteException e) {
			System.out.println("Error a la hora de finalizar la conexion");
			e.printStackTrace();
		}
	}


	/**
	 * Funcion privada que solicita al servidor los peers conectados y los añade a la lista local de peers
	 * Tmb los añade a la interfaz grafica
	 */
	private void addContacts() {
		try {
			Remote[] newContacts = this.server.getConnectedPeers(peerName);
			if (newContacts.length > 0) {
				contacts.addContacts(newContacts);
				window.addContactsNamesToList(contacts.getContactNames());
			}
		} catch (RemoteException e) {
			System.out.println("Imposible registrar los clientes del servidor");
			if(config.DEBUG)
				e.printStackTrace();
		}
	}

	/**
	 * Funcion que crea un chat a partir del nombre del contacto
	 * 
	 * @param contactName
	 */
	public void newChatWindow(String contactName) {

		if (chats.size() == 0) {

			newChat = new ChatWindow(this, contactName, this.peerName);
			chats.put(contactName, newChat);

		} else {

			if (!newChat.isVisible()) {
				newChat.setVisible();
			}

			if (!chats.containsKey(contactName)) {
				newChat.addConversation(contactName);
				chats.put(contactName, newChat);

			} else {
				newChat.putConversation(contactName);
			}
		}

	}

	/**
	 * Funcion que hace un spreading de un mensaje, esto es, envia el mensaje a todos los peers de la lista
	 * de contactos
	 * @param receiver
	 * @param message
	 */
	public void spreadMessage(String receiver, String message) {
		contacts.spreadMessage(receiver, peerName, message);
	}

	/**
	 * Funcion callback que recibe un mensaje de un emisor y lo muestra por pantalla
	 */
	@Override
	public void newMessageCallback(String emisor, String message) {
		newChatWindow(emisor);
		chats.get(emisor).newMessage(message, emisor, false);

	}

	/**
	 * Funcion callback que recibe una nueva referencia de peer y su nombre para añadirlo a la lista de contactos
	 */
	@Override
	public void newContactCallback(Peer2Peer peer, String peerName) {
		// Guardamos el peer en la lista
		if (!(contacts.contains(peerName))) {
			contacts.addPeer(peer, peerName);
			window.addContactNameToList(peerName);
		}
	}

	/**
	 * Funcion callback que elimina un peer de la lista de cotnactos
	 */
	@Override
	public void contactExitCallback(String peerName) {
		if ((contacts.contains(peerName))) {
			contacts.removePeer(peerName);
			window.removeContactNameFromList(peerName);
		}

	}

	/**
	 * Funcion callback que devuelve el nombre del peer
	 */
	@Override
	public String getNameCallback() throws RemoteException {
		System.out.println("Devuelvo mi nombre " + this.peerName);
		return this.peerName;

	}

	/**
	 * Funcion callback que informa de que el peer sigue vivo
	 */
	@Override
	public boolean ping() throws RemoteException {
		// Everything is fine
		// don't delete me!
		return true;
		
	}
	
	
	
	// ++++++++++++++++ GROUP FUNCTIONS +++++++++++++++//
	
	public void spreadGroupMessage(String id, String text, String[] contacts) {
		this.contacts.spreadGroupMessage(peerName, id, text, contacts);
		
	}
	
	public void newGroupChatRequest(){
		// creamos un dialog para que introduzcan los datos
		NewGroupChatDialog dialog = new NewGroupChatDialog(this.contacts.getContactNames(), this);
		
	}
	/**
	 * Funcion que crea un nuevo grupo e informa a los contactos
	 * @param groupName
	 * @param contacts
	 */
	public void createNewGroup(String groupName, String[] contacts){
		// creamos el nuevo identificador
		Date time = Calendar.getInstance().getTime();
		String groupKey = this.peerName + "-" + groupName + "-" + time;
		groupKey = md5.getMD5Hex(groupKey);
		//creamos el grupo
		GroupChatWindow groupChat = new GroupChatWindow(this, groupKey, groupName, peerName,contacts);
		this.groupChats.put(groupKey, groupChat);
		// informamos a los contactos añadidos
		// los contactos que enviamos han de incluir al local
		this.contacts.spreadNewGroup(groupKey, groupName, contacts, this.addLocalContact(contacts));
	}
	/** funcion que crea un nuevo grupo
	 * 
	 * @param groupName
	 */
	public void createNewGroup(String groupName){
		// creamos el nuevo identificador
		Date time = Calendar.getInstance().getTime();
		String groupKey = this.peerName + "-" + groupName + "-" + time;
		groupKey = md5.getMD5Hex(groupKey);
		//creamos el grupo
		GroupChatWindow groupChat = new GroupChatWindow(this, groupKey, groupName, peerName);
		this.groupChats.put(groupKey, groupChat);
	}
	
	
	public void notifyGroupDisconnect(String groupId, String[] contacts){
		this.contacts.spreadDeleteClientGroup(peerName, groupId, contacts);
	}
	
	public void requestAddNewClients(String groupId, String[] groupContacts, String groupName){
		AddClientsToGroupDialog dialog = new AddClientsToGroupDialog(groupId, groupContacts, this.contacts.getContactNames(), groupName, this);
	}
	
	public void notifyNewContactsToGroup(String groupKey, String groupName, String[] oldContacts, String[] newContacts){
		//incorporamos los nuevos contactos a nuestro grupo
		this.groupChats.get(groupKey).addContactsNamesToList(newContacts);

		String[] newListContacts = this.groupChats.get(groupKey).getContactsKeys();
		//informamos a los nuevos usuarios del grupo con todos los usuarios, nuevos y viejos, y con el usuario local
		this.contacts.spreadNewGroup(groupKey, groupName, newContacts, this.addLocalContact(newListContacts));
		
		//informamos a los antiguos usuarios del grupo de que hay nuevos contactos
		this.contacts.spreadNewContactGroup(groupKey, oldContacts, newContacts);
	}
	
	//++++++++ GROUP CALLBACKS++++++++++++//
	
	
	@Override
	public void getNewGroupCallback(String newGroupId, String NewGroupName,
			String[] newContactsKey) throws RemoteException {
			System.out.println("Solicitud de nuevo grupo entrante");
			GroupChatWindow groupChat = new GroupChatWindow(this, newGroupId, NewGroupName, this.peerName, newContactsKey);	
			//lo insertamos en la lista
			this.groupChats.put(newGroupId, groupChat);	
		
	}

	@Override
	public void getNewMessageGroupCallback(String emisor, String groupId, String newMessage)
			throws RemoteException {
		
		synchronized(groupChats){
			if(groupChats.containsKey(groupId)){
				groupChats.get(groupId).newMessage(newMessage, emisor, false);
			}
		}
	}



	@Override
	public void getDeleteClientGroupCallback(String contactKey, String groupId)
			throws RemoteException {
		synchronized(groupChats){
			if(groupChats.containsKey(groupId)){
				groupChats.get(groupId).removeContactNameFromList(contactKey);
			}
		}
	}

	@Override
	public void getNewClientGroupCallback(String newContactKey, String groupId)
			throws RemoteException {
		synchronized(groupChats){
			if(groupChats.containsKey(groupId)){
				groupChats.get(groupId).addContactNameToList(newContactKey);
				}
		}
		
	}

	@Override
	public void getNewClientGroupCallback(String[] newContactsKeys,
			String groupId) throws RemoteException {
		synchronized(groupChats){
			if(groupChats.containsKey(groupId)){
				groupChats.get(groupId).addContactsNamesToList(newContactsKeys);
				}
		}
		
	}
	
	/** HELPER FUNCTIONS **/
	/**
	 * Incluye el contacto local en un array de contactos
	 * @param contacts
	 * @return
	 */
	private String[] addLocalContact(String[] contacts){
		String[] allContacts = new String[contacts.length +1];
		int i = 0;
		allContacts[i++] = this.peerName;
		for(String name : contacts){
			allContacts[i++] = name;
		}
		
		return allContacts;
		
		
	}
	private void finishGroupConnections(){
		synchronized(groupChats){
			for(String groupKey : groupChats.keySet()){
				groupChats.get(groupKey).disconnect();
			}
		}
	}


}
