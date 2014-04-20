package views;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import server.ChatServer;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;

public class MainWindow {

	private JFrame frmServer;
	private ChatServer context;
	private DefaultListModel<String> peersModelList;
	private JList peersList;
	private JMenuItem peerCleanerActions;
	private final boolean PEER_CLEANER_ENABLED = true;
	private final boolean PEER_CLEANER_DISABLED = false;
	private boolean peerCleanerStatus;
	private final String PEER_CLEANER_DISABLED_TEXT = "Enable peer cleaner";
	private final String PEER_CLEANER_ENABLED_TEXT = "Stop peer cleaner";

	/**
	 * Create the application.
	 */
	public MainWindow(ChatServer context) {
		this.context = context;
		this.peerCleanerStatus = PEER_CLEANER_ENABLED;
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
		
		JMenuBar menuBar = new JMenuBar();
		frmServer.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		peerCleanerActions = new JMenuItem(PEER_CLEANER_ENABLED_TEXT);
		peerCleanerActions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				peerCleanerStatus = !peerCleanerStatus;
				if(peerCleanerStatus == PEER_CLEANER_ENABLED){
					peerCleanerActions.setText(PEER_CLEANER_ENABLED_TEXT);
					
				}else{
					peerCleanerActions.setText(PEER_CLEANER_DISABLED_TEXT);
				}
				context.startStopPeerCleaner(peerCleanerStatus);
			}
		});
		mnFile.add(peerCleanerActions);

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
		synchronized(peersModelList){
			peersModelList.removeElement(name);
		}
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
