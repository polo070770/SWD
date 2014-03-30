package views;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import javax.swing.JList;

import peer.ChatPeer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private ChatPeer context;
	private DefaultListModel<String> contactsModelList;
	private JList contactsList;
	
	/**
	 * Create the application.
	 */
	public MainWindow(ChatPeer context, String name) {
		this.context = context;
		initialize();
		frame.setTitle(name);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		contactsModelList  = new DefaultListModel<String>();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Cerrando");
				context.finishConection();
			}
		});
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		contactsList = new JList(contactsModelList);
		
		frame.getContentPane().add(contactsList, BorderLayout.CENTER);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(contactsList, popupMenu);
		
		JMenuItem mntmNewChat = new JMenuItem("New Chat");
		mntmNewChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				context.newChatWindow((String)contactsList.getSelectedValue());
			}
		});
		popupMenu.add(mntmNewChat);
	}
	
	public void addContactNameToList(String name){
		dialogNuevaConexion(name);
		contactsModelList.addElement(name);
	}
	public void removeContactNameFromList(String name){
		dialogNuevaDesconexion(name);
		contactsModelList.removeElement(name);
	}
	
	public void addContactsNamesToList(String[] names){
		for(String name : names){
			contactsModelList.addElement(name);			
		}

	}
	
	
	
	private void dialogNuevaConexion(String name){
		JOptionPane.showMessageDialog(frame,
                name + " se ha conectado",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE);
	}
	private void dialogNuevaDesconexion(String name){
		JOptionPane.showMessageDialog(frame,
                name + " se ha desconectado",
                "Atención",
                JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
