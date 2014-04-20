package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import peer.ChatPeer;

public class NewGroupChatDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField group_name_input;
	private DefaultListModel<String> contactsModelList;
	private JList<String> add_contacts_list;
	private String[] contacts;
	private ChatPeer context;


	/**
	 * Create the dialog.
	 */
	public NewGroupChatDialog(String[] contacts,ChatPeer context) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.contacts = contacts;
		this.contactsModelList = new DefaultListModel<String>();
		this.context = context;
		for (String name : contacts) {
			contactsModelList.addElement(name);
		}
		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblIntroduceUnNombre = new JLabel("Introduce un nombre para el grupo");
				panel.add(lblIntroduceUnNombre, BorderLayout.CENTER);
				lblIntroduceUnNombre.setVerticalAlignment(SwingConstants.TOP);
			}
			{
				group_name_input = new JTextField();
				panel.add(group_name_input, BorderLayout.SOUTH);
				group_name_input.setColumns(10);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{

				add_contacts_list = new JList(contactsModelList);
				JScrollPane add_contacts_scroll = new JScrollPane( add_contacts_list ); 
				panel.add(add_contacts_scroll, BorderLayout.CENTER);
				
			}
			{
				JLabel lblSeleccionaLosContactos = new JLabel("Selecciona los contactos para el grupo (Ctrl para seleccion multiple)");
				panel.add(lblSeleccionaLosContactos, BorderLayout.NORTH);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(group_name_input.getText().length() == 0){
							JOptionPane.showMessageDialog(null, "Debes especificar un nombre de grupo valido", 
									"Atencion", JOptionPane.ERROR_MESSAGE);
						}else{
							crearGroupChat();
							dispose();

						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cerrar();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		this.setVisible(true);
	}
	
	public void cerrar(){
		this.dispose();
	}
	
	public void crearGroupChat(){
		if(add_contacts_list.getSelectedValuesList().isEmpty()){
			this.context.createNewGroup(group_name_input.getText());
		}else{
			String[] selected_contacts;
			selected_contacts = (String[]) add_contacts_list.getSelectedValuesList().toArray(new String[add_contacts_list.getSelectedValuesList().size()]);
			this.context.createNewGroup(group_name_input.getText(),selected_contacts);
		}
		
	}
}
