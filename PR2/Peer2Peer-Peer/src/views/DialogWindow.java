package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public class DialogWindow extends JDialog {
	private JTextField textField;

	public DialogWindow() {

		initialize();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	private void initialize() {

		setBounds(100, 100, 496, 248);
		getContentPane().setLayout(new BorderLayout());
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		{
			JLabel userLabel = new JLabel("user name:");
			userLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			getContentPane().add(userLabel, BorderLayout.WEST);
			userLabel.setToolTipText("Enter a user name to join the chat");

		}
		
		{
			textField = new JTextField();
			getContentPane().add(textField, BorderLayout.CENTER);
			textField.setColumns(10);
			textField.setSize(50,50);
		}

	}

}
