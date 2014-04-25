package Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Status Panel, display text describing action performed
 * @version 1.0
 * */
public class StatusPanel extends JPanel{
	private JLabel status;
	
	//Constants
	public static final int CORRECT = 0;
	public static final int ALERT = 1;
	public static final int NORMAL = 2;
	public static final int INFO = 3;
	
	public StatusPanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		status = new JLabel("Insert text and click \"Go\"");
		add(Box.createRigidArea(new Dimension(5,0)));
		add(status);
	}
	
	/**
	 * Set status text
	 * @param text Text to insert
	 * @param mode	Mode of the text
	 * */
	public void setText(String text,int mode){
		status.setText(text);
		switch (mode) {
		case ALERT:
			status.setForeground(Color.RED);
			break;
		case CORRECT:
			status.setForeground(Color.BLUE);
			break;
		case INFO:
			status.setForeground(new Color(22,106,20));
			break;
		default:
			status.setForeground(Color.DARK_GRAY);
			break;
		}
	}
}
