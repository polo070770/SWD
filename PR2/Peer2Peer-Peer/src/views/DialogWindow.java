package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.MainPeer;
import java.awt.Font;

public class DialogWindow extends JDialog implements Runnable {
	private JTextField textField;
	private JLabel userLabel;
	private JButton okButton;

	private String urlRegistro;
	private MainPeer context;
	private boolean ready;
	private String userName;

	public DialogWindow(MainPeer mp, String url) {

		this.ready = false;
		this.urlRegistro = url;
		this.context = mp;

		initialize();

	}

	@Override
	public void run() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	private void initialize() {

		setBounds(100, 100, 496, 248);
		getContentPane().setLayout(new BorderLayout(0, 0));

		{
			// BUTTON PANE
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new LineBorder(new Color(0, 0, 0)));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						userName = textField.getText();
						tanca();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setHorizontalAlignment(SwingConstants.RIGHT);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						userName = null;
						tanca();
					}
				});
			}
		}
		{
			// INFO PANE
			JPanel infoPane = new JPanel();
			getContentPane().add(infoPane);
			infoPane.setLayout(null);

			{
				userLabel = new JLabel("USER NAME:");
				userLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				userLabel.setBounds(200, 100, 80, 20);
				infoPane.add(userLabel);
				userLabel.setHorizontalAlignment(SwingConstants.CENTER);
				userLabel.setToolTipText("Enter a user name to join the chat");

			}

			{
				textField = new JTextField();
				textField.setLocation(175, 131);
				textField.setHorizontalAlignment(SwingConstants.LEFT);
				userLabel.setLabelFor(textField);
				infoPane.add(textField);
				textField.setColumns(10);
				textField.setSize(130, 20);
				textField.getDocument().addDocumentListener(
						new DocumentListener() {

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
								if (textField.getText().equals("")) {
									okButton.setEnabled(false);
								} else {
									okButton.setEnabled(true);
								}

							}
						});

			}

			JLabel infoServerLabel = new JLabel();
			infoServerLabel.setHorizontalAlignment(SwingConstants.CENTER);
			infoServerLabel.setText("Attempting to connect...");
			infoServerLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
			infoServerLabel.setBounds(116, 11, 247, 42);
			infoPane.add(infoServerLabel);

			JLabel label = new JLabel();
			label.setVerticalAlignment(SwingConstants.TOP);
			label.setText(this.urlRegistro);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Tahoma", Font.BOLD, 14));
			label.setBounds(116, 60, 247, 42);
			infoPane.add(label);
		}

	}

	private void tanca() {
		synchronized (context) {
			ready = true;
			context.notifyAll();
		}
	}

	public boolean isReady() {
		return ready;
	}

	public String getUserName() {
		return userName;
	}
}
