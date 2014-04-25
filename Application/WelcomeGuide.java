package Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Welcome guide, pop up screen on application start
 * @version 1.0
 * */
public class WelcomeGuide extends JDialog{
	int currentPanel = 0;
	JPanel[] guidePanel = new JPanel[3];
	
	/**
	 * @param parent Parent JFrame
	 * */
	public WelcomeGuide(JFrame parent) {
		setLayout(new BorderLayout());
		setTitle("Welcome Guide");
		
		Font textFont = new Font("arial",Font.PLAIN,14);
		
		//Top title
		final String[] titles = {"<html><h2>Welcome to Rot-N Encryption Decryption</h2>","<html><h2>Choose a theme</h2>","<html><h2>Compare input and output</h2>"};
		JPanel titlePanel = new JPanel();
		final JLabel title = new JLabel(titles[0]);
		title.getInsets().set(5, 19, 5, 5);
		titlePanel.add(title);
		title.setFont(new Font("arial",Font.BOLD,15));
		add(titlePanel,BorderLayout.NORTH);
		
		//Guide Panel container
		JPanel guidePanelContainer = new JPanel();
		Dimension dialogDim = new Dimension(400,390);
		
		//Guide Panel 1
		guidePanel[0] = new JPanel();
		JLabel guide1 = new JLabel();
		guide1.setFont(textFont);
		JLabel g1Pic1 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/g1.png")));	
		guide1.setText("<html><br/><br/><b>What can this application do?</b><br/><br/>- Encrypt and decrypt using Rot-N algorithm<br/>- Import input file and export the output<br/>- Change the theme of the application<br/>- Compare and Map the changes<br/>- Status bar displaying your actions");
		guidePanel[0].add(g1Pic1);
		guidePanel[0].add(guide1);
		guidePanelContainer.add(guidePanel[0]);

		//Guide Panel 2
		guidePanel[1] = new JPanel();
		JLabel guide2 = new JLabel();
		guide2.setFont(textFont);
		JLabel g2Pic2 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/g2.png")));
		guide2.setText("<html><br/><br/><b>Choose and create a theme:</b><br/><br/>To activate the style panel: View > Style<br/><br/>- Theme: Pick an existing theme<br/>- Create theme: Create your own theme<br/>- Save theme: Save your created theme<br/>- Load theme: Load saved theme");
		guidePanel[1].add(g2Pic2);
		guidePanel[1].add(guide2);
		guidePanelContainer.add(guidePanel[1]);
		guidePanel[1].setVisible(false);
		
		//Guide Panel 3
		guidePanel[2] = new JPanel();
		JLabel guide3 = new JLabel();
		guide3.setFont(textFont);
		JLabel g3Pic3 = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/g3.png")));	
		g3Pic3.setBorder(BorderFactory.createLineBorder(Color.gray));
		guide3.setText("<html><br/><br/><b>Compare text:</b><br/><br/>`Compare mode` and `Map` are enabled after<br/>clicking `Go`<br/><br/>- Compare mode enabled: Select text<br/>simultaneously on input and output<br/>- Map: Monitor converted characters");
		guidePanel[2].add(g3Pic3);
		guidePanel[2].add(guide3);
		guidePanelContainer.add(guidePanel[2]);
		guidePanel[2].setVisible(false);

		//Add everything 
		for(JPanel current : guidePanel){
				current.setPreferredSize(dialogDim);
		}
		add(guidePanelContainer, BorderLayout.CENTER);
		
		//Buttons
		JPanel bottomPanel = new JPanel();
		final JButton next = new JButton("Next");
		final JButton previous = new JButton("Previous");
		previous.setEnabled(false);
		bottomPanel.add(previous);
		bottomPanel.add(next);
		add(bottomPanel,BorderLayout.SOUTH);
		
		//Default configuration
		setSize(dialogDim);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		
		//Button listeners
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				++currentPanel;
				for(int i=0; i<guidePanel.length;i++){
					if(i == currentPanel){
						guidePanel[i].setVisible(true);
						title.setText(titles[i]);
					}else{
						guidePanel[i].setVisible(false);
					}
				}
				
				if(currentPanel == guidePanel.length-1){
					next.setText("Finish");
				}else if(currentPanel == guidePanel.length){
					dispose();
				}
				previous.setEnabled(true);
			}
		});
		
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				--currentPanel;
				for(int i=0; i<guidePanel.length;i++){
					if(i == currentPanel){
						guidePanel[i].setVisible(true);
						title.setText(titles[i]);
					}else{
						guidePanel[i].setVisible(false);
					}
				}
				
				if(currentPanel == 0)
					previous.setEnabled(false);
				next.setText("Next");
				next.setEnabled(true);
			}
		});
	}
}
