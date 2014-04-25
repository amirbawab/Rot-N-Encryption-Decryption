package Application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * About dialog box: Information about the game
 * @version 1.0
 * */
public class AboutDialog extends JDialog{
	
	/**
	 * About Dialog
	 * @param parent Parent JFrame
	 * */
	public AboutDialog(RotFrame parent) {
		setTitle("About Rot-n Encryption/Decryption");
		setLayout(new BorderLayout());
		
		//set icon
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/lock.png")).getImage());
		
		//Center text
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.LINE_AXIS));
		textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		
		//setting the text and size
		String content = "<html><h3>About Rot-n Ecryption/Decryptiom V1.0</h3>" +
				"<br/>ROTN (\"rotate by N places\", sometimes hyphenated ROT-N) is a simple letter substitution cipher that replaces a letter with the letter N letters after it in the alphabet.<br/>" +
				"<em>- Wikipedia: http://en.wikipedia.org/wiki/ROT13</em>" +
				"<br/><br/>Team: <b>Amir El Bawab & Sebouh Bardakjian</b>";
		JLabel text = new JLabel(content);
		text.setPreferredSize(new Dimension(250,100));
		
		//Border and position
		text.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		text.setHorizontalAlignment(SwingConstants.LEFT);
		text.setVerticalAlignment(SwingConstants.TOP);

		//adding to the panel
		textPanel.add(new JScrollPane(text),BorderLayout.CENTER);
		
		//Bottom button
		JPanel bottomButton = new JPanel();
		JButton close = new JButton("Close");
		bottomButton.add(close);
		
		//Add panels
		add(textPanel,BorderLayout.CENTER);
		add(bottomButton,BorderLayout.SOUTH);
		
		//Add Listener
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		//Default configuration
		setSize(300,300);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
	}
}
