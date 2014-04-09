package views;

import java.awt.Color;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import peer.ChatPeer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class ChatWindow {

	private JFrame frame;
	private JTextField textInput;
	private JButton btnSend;
	private JTabbedPane tabPanel;
	private DefaultListModel<String> contactsModelList;
	private JList contactsList;
	private ChatPeer context;
	private String localName;
	DateFormat df;

	/**
	 * Create the application.
	 */
	public ChatWindow(ChatPeer context, String name, String localName) {
		this.context = context;
		this.localName = localName;
		this.contactsModelList = new DefaultListModel<String>();

		initialize();

		addConversation(name);

		frame.setVisible(true);

		df = new SimpleDateFormat("HH:mm:ss");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle(" - Chat de " + localName);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Hide the frame by setting its visibility as false
				frame.setVisible(false);
				System.out.println("ocultando");
			}
		});

		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		// panel principal contenedor
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);

		// entrada de texto
		textInput = new JTextField();
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textInput.getText().length() > 0)
					newMessage(textInput.getText(), localName, true);
			}
		});
		panel.setLayout(new BorderLayout(0, 0));
		textInput.setColumns(10);
		panel.add(textInput, BorderLayout.CENTER);
		textInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}

			public void changed() {
				if (textInput.getText().equals("")) {
					btnSend.setEnabled(false);
				} else {
					btnSend.setEnabled(true);
				}

			}
		});

		// boton de enviar
		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		panel.add(btnSend, BorderLayout.EAST);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMessage(textInput.getText(), localName, true);
			}
		});

		// lista de contactos
		contactsList = new JList(contactsModelList);
		contactsList.setBorder(new LineBorder(new Color(0, 0, 0)));
		// frame.getContentPane().add(contactsList, BorderLayout.EAST);

		// pestanyas de conversaciones
		tabPanel = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(tabPanel, BorderLayout.CENTER);

	}

	private void addPanel(String text) {
		JPanel panelTab = new JPanel();
		tabPanel.add(text, panelTab);
		panelTab.setLayout(new BorderLayout(0, 0));

		JTextArea textArea;
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panelTab.add(textArea);

	}

	private void toFrontAndFocusable() {
		frame.toFront();
		frame.setFocusable(true);
	}

	public void addConversation(String name) {
		addContactNameToList(name);
		addPanel(name);
		putConversation(name);
	}

	public void putConversation(String name) {
		boolean trobat = false;
		int i = 0;
		while (!trobat && i < tabPanel.getTabCount()) {
			if (tabPanel.getTitleAt(i).equals(name)) {
				tabPanel.setSelectedIndex(i);
				trobat = true;
			}
			i++;
		}
		toFrontAndFocusable();
	}

	public void addContactNameToList(String name) {
		contactsModelList.addElement(name);
	}

	public boolean isVisible() {
		return frame.isVisible();
	}

	public void setVisible() {
		frame.setVisible(true);
	}

	public void newMessage(String text, String emisor, Boolean spread) {
		Date time = Calendar.getInstance().getTime();
		String s = "[" + emisor + " : " + df.format(time) + " ]: " + text;
		
		JPanel panelTab;
		JTextArea textArea;

		if (spread) {

			panelTab = (JPanel) tabPanel.getComponentAt(tabPanel
					.getSelectedIndex());
			textArea = (JTextArea) panelTab.getComponent(0);
			textArea.insert(s + "\n", textArea.getText().length());
			textArea.setCaretPosition(textArea.getText().length());

			textInput.setText("");
			textInput.requestFocusInWindow();
			String name = tabPanel.getTitleAt(tabPanel.getSelectedIndex());
			context.spreadMessage(name, text);

		} else {
			
			int i = 0;
			while (i < tabPanel.getTabCount()) {
				if (tabPanel.getTitleAt(i).equalsIgnoreCase(emisor)) {
					panelTab = (JPanel) tabPanel.getComponentAt(i);
					textArea = (JTextArea) panelTab.getComponent(0);
					textArea.insert(s + "\n", textArea.getText().length());
					textArea.setCaretPosition(textArea.getText().length());
					break;
				}
				i++;
			}
		}

		toFrontAndFocusable();
	}
}
