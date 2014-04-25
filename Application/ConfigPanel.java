package Application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import Library.Rot;
import Listener.ConfigEncryptDecryptListener;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Configuration panel, Left panel used to configure the application functionality
 * @version 1.0
 * */
public class ConfigPanel extends JPanel {
	private GridBagConstraints gc;
	private JFrame parent;
	
	//Interface object
	private ConfigEncryptDecryptListener encryptDecryptListener;
	
	// compare checkbox & map button
	private JCheckBox compareMode;
	private JButton map;
	
	// Map arrayLists
	private ArrayList<String> letters;
	private ArrayList<String> encryptedLetters;
	
	// Components used outside constructor
	private JComboBox rotBox;
	final JCheckBox digitEncrypt;
	
	/**
	 * ConfigPanel
	 * @param frame Parent frame
	 * */
	public ConfigPanel(JFrame frame) {
		//set the parent
		parent = frame;
	
		//Create Rot to Encrypt/Decrypt
		final Rot rot = new Rot();
		
		// Grid layout
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		//panels
		JPanel logoPanel = new JPanel();
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new GridBagLayout());
		
		//remove panels background
		logoPanel.setPreferredSize(new Dimension(200,90));
		logoPanel.setOpaque(false);
		configPanel.setOpaque(false);
		
		// Adding borders
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border innerBorder = BorderFactory.createTitledBorder("Configuration");
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		// Logo
		JLabel logo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/logo.png")));
		logoPanel.add(logo);
		
		// ComboBox
		JLabel rotLabel = new JLabel("ROT-N: ");
		String[] rotOption = {"-- Choose --" ,"ROT5", "ROT7", "ROT13", "ROT17" };
		rotBox = new JComboBox(rotOption);
		rotBox.setSelectedIndex(0);

		// Radio buttons
		ButtonGroup radioGroup = new ButtonGroup();
		JLabel modeLabel = new JLabel("Mode: ");
		final JRadioButton encryptRadio = new JRadioButton("Encrypt");
		encryptRadio.setSelected(true);
		JRadioButton decryptRadio = new JRadioButton("Decrypt");
		radioGroup.add(encryptRadio);
		radioGroup.add(decryptRadio);

		// Checkbox digit encryption
		digitEncrypt = new JCheckBox(" Encrypt digits");
		
		//Compare mode
		compareMode = new JCheckBox(" Compare mode");
		compareMode.setEnabled(false);
		
		// Map
		map = new JButton("Map");
		map.setEnabled(false);
		
		// Run Button
		JButton run = new JButton("Go");
		run.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/go.png")));
		run.setMargin(new Insets(2, 4, 3, 8));

		// Run Button
		JButton reset = new JButton("Reset");
		reset.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/reset.png")));
		reset.setMargin(new Insets(4, 4, 4, 5));

		//remove backgrounds
		encryptRadio.setOpaque(false);
		decryptRadio.setOpaque(false);
		digitEncrypt.setOpaque(false);
		compareMode.setOpaque(false);
		
		// Default grid configuration
		gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 10, 0, 10);
		gc.anchor = GridBagConstraints.NORTHWEST;
		int y = 0;
				
		// Add ComboBox
		gc.weightx = 1;
		gc.weighty = 0.15;
		gridPosition(0, y);
		configPanel.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/lock.png"))),gc);
		gc.insets = new Insets(3, 30, 0, 0);
		configPanel.add(rotLabel, gc);
		gc.insets = new Insets(0, 10, 0, 10);
		gridPosition(1, y++);
		configPanel.add(rotBox, gc);

		// Add Radio Buttons
		gridPosition(0, y++);
		gc.weighty = 0.01;
		configPanel.add(modeLabel, gc);
		gridPosition(0, y);
		gc.weighty = 0.2;
		configPanel.add(encryptRadio, gc);
		gridPosition(1, y++);
		configPanel.add(decryptRadio, gc);

		// Add Checkbox Digits
		gridPosition(0, y++);
		gc.weighty = 0.07;
		configPanel.add(digitEncrypt, gc);

		gridPosition(0, y++);
		gc.weighty = 0.05;
		configPanel.add(compareMode,gc);
		
		gridPosition(0, y++);
		gc.weighty = 0.5;
		configPanel.add(map,gc);
		
		// Add Run Button
		gridPosition(0, y);
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.NORTHEAST;
		configPanel.add(run, gc);
		
		// Add Reset button
		gridPosition(1, y);
		gc.weightx = 1.5;
		gc.anchor = GridBagConstraints.NORTHWEST;
		configPanel.add(reset,gc);
		
		// Add panels
		add(logoPanel);
		add(configPanel);

		//Run Listener
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// Get the rotN selected
				int rotN = -1;
				switch(rotBox.getSelectedIndex()){
				case 1:
					rotN = 5;
					break;
				case 2:
					rotN = 7;
					break;
				case 3:
					rotN = 13;
					break;
				case 4:
					rotN = 17;
					break;
				}
				
				// If rotN not chosen
				if(rotN == -1){
					JOptionPane.showMessageDialog(parent, "Please choose a Rot number");
					return;
				}
				
				// if listener set
				if(encryptDecryptListener != null){
					// If input field is empty
					if(encryptDecryptListener.getInput().length() == 0){
						JOptionPane.showMessageDialog(parent, "Please enter a text to process");
						return;
					}
					String enableDigit = digitEncrypt.isSelected() ? "and digits " : "";
					if(encryptRadio.isSelected()){
						encryptDecryptListener.setOutput(rot.encrypt(encryptDecryptListener.getInput(), rotN, digitEncrypt.isSelected()));
						encryptDecryptListener.statusUpdate("Input text "+ enableDigit +"successfully encrypted using " + rotBox.getSelectedItem(), StatusPanel.CORRECT);
					}else{
						encryptDecryptListener.setOutput(rot.decrypt(encryptDecryptListener.getInput(), rotN, digitEncrypt.isSelected()));
						encryptDecryptListener.statusUpdate("Input text "+ enableDigit +"successfully decrypted using " + rotBox.getSelectedItem(), StatusPanel.CORRECT);
					}
						
					// enable compare mode & map
					compareMode.setEnabled(true);
					map.setEnabled(true);
					
					// By default not in compare mode
					encryptDecryptListener.setCompareMode(false);
				}
			}
		});
		
		// compare mode Listener
		compareMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(encryptDecryptListener != null){
					encryptDecryptListener.setCompareMode(compareMode.isSelected());
					if(compareMode.isSelected())
						encryptDecryptListener.statusUpdate("Compare mode enabled", StatusPanel.INFO);
					else
						encryptDecryptListener.statusUpdate("Compare mode disabled", StatusPanel.INFO);
				}
			}
		});
		
		// reset listener
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(encryptDecryptListener != null){
					encryptDecryptListener.setInput(null);
					encryptDecryptListener.setOutput(null);
					encryptDecryptListener.setCompareMode(false);
					encryptDecryptListener.statusUpdate("Application was reset. Insert text and click \"Go\"", StatusPanel.NORMAL);
				}
				compareMode.setSelected(false);
			}
		});
		
		// map listener
		map.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//prepare the dialog to pop up
				JDialog dialog = new JDialog(parent);
				dialog.setLayout(new BorderLayout());
				dialog.setTitle("Charaters map");
				
				letters = rot.getLetters();
				encryptedLetters = rot.getEncryptedLetters();
				String text = "Character(s) changed:\nTotal = "+ letters.size() +"\n";
				
				// before => after
				for(int i=0; i<letters.size();i++){
					text += "\n" + letters.get(i) + "\t=>\t" + encryptedLetters.get(i);
				}
				
				JTextPane tp = new JTextPane();
				tp.setText(text);
				tp.setEditable(false);
				JScrollPane jstp = new JScrollPane(tp);
				Dimension dim = jstp.getPreferredSize();
				dim.setSize(200,300);
				
				//add and specify the default configuration of the dialog
				dialog.add(jstp);
				dialog.setSize(dim);
				dialog.setResizable(false);
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(parent);
			}
		});
	}
	
	/**
	 * Listener will be executed when click: Run, Compare mode
	 * @param encryptDecryptListener Listener
	 * */
	public void addConfigEncryptDecryptListener(ConfigEncryptDecryptListener encryptDecryptListener){
		this.encryptDecryptListener = encryptDecryptListener;
	}
	
	/**
	 * Set grid position
	 * @param x X-Position
	 * @param y Y-Position
	 * */
	private void gridPosition(int x, int y) {
		gc.gridx = x;
		gc.gridy = y;
	}
	
	/**
	 * Toggle compare mode
	 * @param enabled Compare mode and map enabled or not
	 * */
	public void setCompareModeAndMapEnabaled(boolean enabled){
		compareMode.setEnabled(enabled);
		compareMode.setSelected(false);
		map.setEnabled(enabled);
	}
	
	/**
	 * Set checkbox selection
	 * @param enabled Check the compare mode checkbox
	 * */
	public void setCheckedCompareMode(boolean enabled){
		compareMode.setSelected(enabled);
	}
}