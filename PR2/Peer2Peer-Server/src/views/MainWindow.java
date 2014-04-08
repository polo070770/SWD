package views;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import server.ChatServer;

public class MainWindow {

	private JFrame frmServer;
	private ChatServer context;
	private DefaultListModel<String> peersModelList;
	private JList peersList;

	/**
	 * Create the application.
	 */
	public MainWindow(ChatServer context) {
		this.context = context;
		initialize();
		frmServer.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServer = new JFrame();
		frmServer.setTitle("Server");
		peersModelList = new DefaultListModel<String>();

		frmServer.setBounds(100, 100, 450, 300);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServer.getContentPane().setLayout(new BorderLayout(0, 0));

		peersList = new JList(peersModelList);

		frmServer.getContentPane().add(peersList, BorderLayout.CENTER);

		frmServer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				context.stopServer();
			}
		});

	}

	/**
	 * Anyade el nombre del nuevo peer conectado en la lista del servidor
	 * 
	 * @param name
	 */
	public void addPeerNameToList(String name) {
		peersModelList.addElement(name);
	}

	/**
	 * Quita el nombre del nuevo peer conectado en la lista del servidor
	 * 
	 * @param name
	 */
	public void removePeerNameFromList(String name) {
		peersModelList.removeElement(name);

	}

	/**
	 * Anyade varios nombres de peers en la lista del servidor
	 * 
	 * @param names
	 */
	public void addPeersNamesToList(String[] names) {
		for (String name : names) {
			peersModelList.addElement(name);
		}

	}

}
