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

public class AddClientsToGroupDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private DefaultListModel<String> contactsModelList;
	private JList<String> add_contacts_list;
	private ChatPeer context;
	private String groupName;
	private String groupKey;
	private String[] groupContacts;


	/**
	 * Create the dialog.
	 */
	public AddClientsToGroupDialog(String groupId, String[] groupContacts, String[] possibleContacts, String groupName, ChatPeer context) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.contactsModelList = new DefaultListModel<String>();
		this.groupName = groupName;
		this.context = context;
		this.groupContacts = groupContacts;
		this.groupKey = groupId;
		this.filterAndAddPossibleContacts(groupContacts, possibleContacts);

		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		add_contacts_list = new JList(contactsModelList);
		JScrollPane add_contacts_scroll = new JScrollPane( add_contacts_list ); 
		panel.add(add_contacts_scroll, BorderLayout.CENTER);
	
		JLabel lblSeleccionaLosContactos = new JLabel("Selecciona los contactos para el grupo (Ctrl para seleccion multiple)");
		panel.add(lblSeleccionaLosContactos, BorderLayout.NORTH);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(add_contacts_list.getSelectedValuesList().size() == 0){
					JOptionPane.showMessageDialog(null, "Debes seleccionar al menos un contacto", 
							"Atencion", JOptionPane.ERROR_MESSAGE);
				}else{
					addContacts();
					dispose();

				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrar();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		this.setTitle("Añadir contactos a " + this.groupName);
		this.setVisible(true);
	}
	
	private void filterAndAddPossibleContacts(String[] groupContacts, String[] possibleContacts) {
		boolean add;
		for (String name : possibleContacts) {
			add = true;
			// buscamos si el posible contacto ya esta en la lista de contactos del grupo
			for(String contactGroupName : groupContacts){
				if(name.equals(contactGroupName)){
					add = false;
					break;
				}
			}
			if (add){
				contactsModelList.addElement(name);
			}
		}
	}

	public void cerrar(){
		this.dispose();
	}
	
	public void addContacts(){
			String[] selected_contacts;
			selected_contacts = (String[]) add_contacts_list.getSelectedValuesList().toArray(new String[add_contacts_list.getSelectedValuesList().size()]);
			context.notifyNewContactsToGroup(groupKey, groupName, groupContacts, selected_contacts);
	}
}
