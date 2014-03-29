
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GuiChat2 extends JFrame implements ActionListener {

    private String screenName;

    // GUI stuff
    private JTextArea  enteredText = new JTextArea(10, 32);
    private JTextField typedText   = new JTextField(32);


    public GuiChat2(String screenName, String hostName) {

               // close output stream  - this will cause listen() to stop and exit
        this.screenName = screenName;
		addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
					//onclose...
                }
            }
        );
		
        enteredText.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		JOptionPane.showMessageDialog(enteredText,
                        "Click.",
                        "Hey!",
                        JOptionPane.INFORMATION_MESSAGE);
        	}
        });


        // create GUI stuff
        enteredText.setEditable(false);
        enteredText.setBackground(Color.LIGHT_GRAY);
        typedText.addActionListener(this);

        Container content = getContentPane();
        content.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        content.add(typedText, BorderLayout.SOUTH);


        // display the window, with focus on typing box
        setTitle("GuiChat 1.0: [" + screenName + "]");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        typedText.requestFocusInWindow();
        setVisible(true);

    }

    // process TextField after user hits Enter
    public void actionPerformed(ActionEvent e) {
		String s = "[" + screenName + "]: " + typedText.getText();
        //System.out.println(s);
		enteredText.insert(s + "\n", enteredText.getText().length());
		enteredText.setCaretPosition(enteredText.getText().length());
        typedText.setText("");
        typedText.requestFocusInWindow();
		//Talk with the other pal.
		
    }

    public static void main(String[] args)  {
		if (args.length!= 2){
			System.out.println("java GuiChat <nickname> <host>");
		}
		else {
			GuiChat2 client = new GuiChat2(args[0], args[1]);
		}

    }
}