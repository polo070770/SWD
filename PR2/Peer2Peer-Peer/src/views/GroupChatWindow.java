package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;






import javax.swing.JTextArea;

import java.awt.Panel;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;

import peer.ChatPeer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GroupChatWindow {

	private JFrame frame;
	private JTextField text_input;
	private String title;
	private String id; //identificador unico
	private String local_user_name;
	private DefaultListModel<String> contactsModelList;
	private DateFormat df;
	private JTextArea text_content;
	private ChatPeer context;
	private JButton send_button;

	/**
	 * Create the application.
	 * @wbp.parser.constructor
	 */
	
	public GroupChatWindow(ChatPeer context, String id, String groupName, String local_user_name) {
		this.id = id;
		this.title = groupName;
		this.local_user_name = local_user_name;
		df = new SimpleDateFormat("HH:mm:ss");
		this.context = context;
		this.contactsModelList = new DefaultListModel<String>();
		initialize();
		
	}

	public GroupChatWindow(ChatPeer context, String id, String groupName, String local_user_name, String[] contacts) {
		
		this(context, id, groupName, local_user_name);
		this.addContactsNamesToList(contacts);
		
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//notificamos al resto de contactos que dejamos el grupo
				disconnect();

			}
		});
		contactsModelList = new DefaultListModel<String>();
		Panel contacts_panel = new Panel();
		frame.getContentPane().add(contacts_panel, BorderLayout.EAST);
		contacts_panel.setLayout(new BorderLayout(0, 0));
		
		JList contacts_list = new JList(contactsModelList);
		JScrollPane contacts_list_scroll = new JScrollPane(contacts_list);
		
		

		
		contacts_panel.add(contacts_list_scroll, BorderLayout.WEST);
		
		Button add_contact_button = new Button("Add contact");
		add_contact_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add_contact();
			}
		});
		contacts_panel.add(add_contact_button, BorderLayout.NORTH);
		
		Panel text_panel = new Panel();
		frame.getContentPane().add(text_panel, BorderLayout.CENTER);
		text_panel.setLayout(new BorderLayout(0, 0));
		
		text_content = new JTextArea();
		text_content.setLineWrap(true);
		text_content.setWrapStyleWord(true);
		text_content.setEditable(false);
		
		//*** AÑADIMOS SCROLL **/
		JScrollPane text_conent_scroll = new JScrollPane( text_content ); 
		text_panel.add(text_conent_scroll, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		text_input = new JTextField();
		text_input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send_button.doClick();
			}
		});
		panel.add(text_input);
		text_input.setColumns(10);
		
		send_button = new JButton("Enviar");
		send_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(text_input.getText().length() > 0){
					newMessage(text_input.getText(), local_user_name, true);
				}
			}
		});
		panel.add(send_button, BorderLayout.EAST);
		frame.setTitle(this.local_user_name + " - " + this.title);
		frame.setVisible(true);
	}
	
	
	/** funciones con los contactos **/
	public void addContactNameToList(String name) {
		contactsModelList.addElement(name);
		String message = name + " se ha incorporado al grupo!";
		newMessage(message, "LOG", false);
	}

	public void removeContactNameFromList(String name) {
		contactsModelList.removeElement(name);
		String message = name + " ha abandonado el grupo!";
	    newMessage(message, "LOG", false);
		
	}

	public void addContactsNamesToList(String[] names) {
		for (String name : names) {
			if(!name.equals(local_user_name)){
				String message = name + " se ha incorporado al grupo!";
				newMessage(message, "LOG", false);
				contactsModelList.addElement(name);
			}
		}
	}
	
	
	public void disconnect(){
		context.notifyGroupDisconnect(this.id, this.getContactsKeys());
		frame.dispose();
	}
	public void add_contact(){
		context.requestAddNewClients(this.id, this.getContactsKeys(), this.title);
	}
	
	public String[] getContactsKeys(){
		String[] contacts = new String[contactsModelList.size()];
		for(int i = 0; i< contactsModelList.size(); i++){
			contacts[i] = (String)contactsModelList.get(i);
		}
		
		return contacts;
		
	}
	
	/** Funciones de envio y recepcion de mensajes*/

	public void newMessage(String text, String emisor, Boolean spread) {
		Date time = Calendar.getInstance().getTime();
		String s = "[" + emisor + " : " + df.format(time) + " ]: " + text;

		text_content.insert(s + "\n", text_content.getText().length());
		text_content.setCaretPosition(text_content.getText().length());

		if (spread) {
			text_input.setText("");
			text_input.requestFocusInWindow();		
			context.spreadGroupMessage(this.id, text, getContactsKeys());
		}
	}
	

}
