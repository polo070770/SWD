package views;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.WindowConstants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Window.Type;

import javax.swing.DefaultListModel;

import peer.ChatPeer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.border.LineBorder;
public class ChatWindow {

	private JFrame frame;
	private JTextField textInput;
	private JTextArea textArea;
	private DefaultListModel<String> contactsModelList;
	private JList contactsList;
	private ChatPeer context;
	private String title;
	private String localName;
	DateFormat df ;

	/**
	 * Create the application.
	 */
	public ChatWindow(ChatPeer context,String name, String localName) {
		this.context = context;
		this.title = name;
		this.localName = localName;
		initialize();
		frame.setVisible(true);
		//df = new SimpleDateFormat("yyyy/MM/DD HH:mm");
		df = new SimpleDateFormat("HH:mm:ss");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		contactsModelList  = new DefaultListModel<String>();
		addContactNameToList(title);
		frame.setType(Type.UTILITY);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Hide the frame by setting its visibility as false
	            frame.setVisible(false);
				System.out.println("ocultando");
			}
		});
		
		frame.setBounds(100, 100, 440, 304);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setTitle(localName +" - Chat con " + title);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		textInput = new JTextField();
		
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textInput.getText().length() > 0)
					newMessage(textInput.getText(), localName, true);
			}
		});
		
		textInput.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(textInput);
		textInput.setColumns(30);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textInput.getText().length() > 0)				
					newMessage(textInput.getText(),localName, true);
			}
		});
		panel.add(btnSend);
		
		contactsList = new JList(contactsModelList);
		contactsList.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(contactsList, BorderLayout.EAST);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
	}

	public void addContactNameToList(String name){
		contactsModelList.addElement(name);
	}
	
	public boolean isVisible(){
		return frame.isVisible();
	}
	
	public void setVisible(){
		frame.setVisible(true);
	}
	
	public void newMessage(String text, String emisor, Boolean spread){
		Date time = Calendar.getInstance().getTime();
		String s = "[" + emisor + " : " + df.format(time) + " ]: " + text;
        //System.out.println(s);
		textArea.insert(s + "\n", textArea.getText().length());
		textArea.setCaretPosition(textArea.getText().length());
		if(spread){
	        textInput.setText("");
	        textInput.requestFocusInWindow();
	        context.spreadMessage(title, text);
		}
		
	}
}
