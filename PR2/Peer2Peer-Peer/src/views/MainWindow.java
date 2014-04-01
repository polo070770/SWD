package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import peer.ChatPeer;
import javax.swing.JScrollPane;

public class MainWindow {

	private JFrame frame;
	private ChatPeer context;
	private DefaultListModel<String> contactsModelList;
	private JList contactsList;
	private JTextArea textLogArea;
	private JLabel lblLogLabel;
	private Panel contactPanel;
	private Panel logPanel;
	private JScrollPane scrollPane;

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
		contactsModelList = new DefaultListModel<String>();

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

		contactPanel = new Panel();
		frame.getContentPane().add(contactPanel, BorderLayout.CENTER);
		contactPanel.setLayout(new BorderLayout(0, 0));

		contactsList = new JList(contactsModelList);
		contactsList.setLayoutOrientation(JList.VERTICAL_WRAP);
		contactsList.setVisibleRowCount(16);
		contactsList.setValueIsAdjusting(true);
		contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactPanel.add(contactsList, BorderLayout.CENTER);
		contactsList.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(contactsList, popupMenu);

		JMenuItem mntmNewChat = new JMenuItem("New Chat");
		mntmNewChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				context.newChatWindow((String) contactsList.getSelectedValue());
			}
		});
		popupMenu.add(mntmNewChat);

		logPanel = new Panel();
		frame.getContentPane().add(logPanel, BorderLayout.EAST);
		logPanel.setLayout(new BorderLayout(0, 0));

		lblLogLabel = new JLabel("LOG");
		logPanel.add(lblLogLabel, BorderLayout.NORTH);
		lblLogLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLogLabel.setHorizontalAlignment(SwingConstants.CENTER);

		textLogArea = new JTextArea();
		textLogArea.setLineWrap(true);
		logPanel.add(textLogArea, BorderLayout.CENTER);
		textLogArea.setWrapStyleWord(true);
		textLogArea.setColumns(20);
		textLogArea.setBackground(UIManager
				.getColor("FormattedTextField.inactiveBackground"));
		textLogArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textLogArea.setEditable(false);

		scrollPane = new JScrollPane(textLogArea);
		logPanel.add(scrollPane, BorderLayout.EAST);

	}

	public void addContactNameToList(String name) {
		logNuevaConexion(name);
		contactsModelList.addElement(name);
	}

	public void removeContactNameFromList(String name) {
		logNuevaDesconexion(name);
		contactsModelList.removeElement(name);
	}

	public void addContactsNamesToList(String[] names) {
		for (String name : names) {
			contactsModelList.addElement(name);
		}
	}

	private void logNuevaConexion(String name) {
		textLogArea.insert("- " + name + " se ha conectado\n", textLogArea
				.getText().length());
		DefaultCaret caret = (DefaultCaret) textLogArea.getCaret();
		// textLogArea.setCaretPosition(textLogArea.getText().length());
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// JOptionPane.showMessageDialog(frame, name + " se ha conectado",
		// "Atención", JOptionPane.INFORMATION_MESSAGE);
	}

	private void logNuevaDesconexion(String name) {
		textLogArea.insert("- " + name + " se ha desconectado\n", textLogArea
				.getText().length());
		textLogArea.setCaretPosition(textLogArea.getText().length());
		// JOptionPane.showMessageDialog(frame, name + " se ha desconectado",
		// "Atención", JOptionPane.INFORMATION_MESSAGE);
	}

	private void addPopup(Component component, final JPopupMenu popup) {
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
				if (!contactsList.isSelectionEmpty())
					popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
