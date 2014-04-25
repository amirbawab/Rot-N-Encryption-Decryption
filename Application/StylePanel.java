package Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import Library.RotTheme;
import Listener.EncryptDecryptThemeListener;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Style Panel, specify the theme for the input and output
 * @version 1.0
 * */
public class StylePanel extends JPanel{
	//Listener for theme change
	private EncryptDecryptThemeListener themeListener;
	
	//Parent JFrame
	private JFrame parent;
	
	//default themes
	private RotTheme defaultTheme;
	private RotTheme amirTheme;
	private RotTheme sebouhTheme;
	
	//backup and tmp theme
	private RotTheme originalTheme;
	private RotTheme tmpTheme;
	
	//Available themes
	private ArrayList<RotTheme> availableThemes;
	private DefaultComboBoxModel styleListModel;
	private JComboBox styleList;
	
	//Constant properties
	private final int[] fontSizes = {10,12,14,16,18};
	private final String[] fontNames = {"arial","verdana", "serif"};
	private final int[] fontStyles = {Font.PLAIN,Font.ITALIC,Font.BOLD, Font.BOLD + Font.ITALIC};
	private String FILE_EXTENSION = "trot";
	
	//File
	private File lastFile;
	private JFileChooser fileChooser;
	private ObjectOutputStream objectWriter;
	private ObjectInputStream objectReader;
	
	/**
	 * @param parent Parent JFrame
	 * */
	public StylePanel(JFrame parent) {
		//set Layout
		setLayout(new GridBagLayout());
		
		// set parent
		this.parent = parent;
		
		//fileChooser
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileFilter() {
			//Extension appearing in the file chooser
			public String getDescription() {
				return FILE_EXTENSION;
			}
			
			//File to appear in the suggested window in the file chooser
			public boolean accept(File file) {
				String[] extentions = {FILE_EXTENSION};
				if (file.isDirectory()) {
			      return true;
			    } else {
			      String path = file.getAbsolutePath().toLowerCase();
			      for (int i = 0, n = extentions.length; i < n; i++) {
			        String extension = extentions[i];
			        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) 
			          return true;
			      }
			    }
			    return false;
			}
		});
		
		//Built in themes
		defaultTheme = new RotTheme("Default", new Color(255,255,255),new Color(0,0,0),new Font(fontNames[0], fontStyles[0], fontSizes[1]));
		amirTheme = new RotTheme("Amir", new Color(44,44,44),new Color(56,205,65),new Font(fontNames[0], fontStyles[2], fontSizes[1]));
		amirTheme.setOutputTextColor(new Color(227,152,47));
		sebouhTheme = new RotTheme("Sebouh", Color.BLACK, Color.YELLOW, new Font("Verdana", Font.ITALIC + Font.BOLD, 14));
		
		//Add themes to arraylist
		availableThemes = new ArrayList<RotTheme>();
		availableThemes.add(defaultTheme);
		availableThemes.add(amirTheme);
		availableThemes.add(sebouhTheme);
		
		//set original theme by default
		originalTheme = defaultTheme;
		
		//default grid pref
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_START;
		
		// Adding borders
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border innerBorder = BorderFactory.createTitledBorder("Style");
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		//create components
		styleListModel = new DefaultComboBoxModel();
		for(RotTheme availableTheme : availableThemes)
			styleListModel.addElement(availableTheme.getThemeName());
		styleList = new JComboBox(styleListModel);
		
		JButton createTheme = new JButton("Create theme");
		createTheme.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/add.png")));
		
		JButton saveTheme = new JButton("Save theme");
		saveTheme.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/save.png")));
		
		JButton loadTheme = new JButton("Load theme");
		loadTheme.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/load.png")));
		
		//add components
		gc.weightx = 0.05;
		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 10, 10);
		add(new JLabel("Theme: "),gc);
		
		gc.weightx = 0.05;
		gc.gridx++;
		add(styleList,gc);

		gc.gridx++;
		add(createTheme,gc);
		
		gc.gridx++;
		add(saveTheme,gc);
		
		gc.gridx++;
		gc.weightx = 3;
		add(loadTheme,gc);
		
		//add listeners
		createTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(themeListener != null){
					themeListener.previewOpened();
					tmpTheme = originalTheme.clone();
				}
				
				new ThemeCreator();
				themeListener.statusUpdate("Creating a new theme", StatusPanel.INFO);
			}
		});
		
		// on change theme
		styleList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(themeListener != null){
					themeListener.themePicked(availableThemes.get(styleList.getSelectedIndex()));
					themeListener.statusUpdate("Theme changed to \""+ styleList.getSelectedItem() +"\"", StatusPanel.CORRECT);
				}
				originalTheme = availableThemes.get(styleList.getSelectedIndex());
			}
		});
		
		// on click of save
		saveTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Set the path
				if(lastFile != null)
					fileChooser.setCurrentDirectory(lastFile);
				
				//save window, if hit cancel then exit
				if(fileChooser.showSaveDialog(StylePanel.this.parent) == JFileChooser.CANCEL_OPTION)
					return;
				
				//save file as last file opened
				lastFile = fileChooser.getSelectedFile();
				
				//verify that file ext is correct
				if(lastFile != null && themeListener != null){
					//If file exist, ask the user if replace file
					if(lastFile.exists() && JOptionPane.showConfirmDialog(StylePanel.this.parent, "Would you like to overwrite the existing file ?", "Overwite existing file", JOptionPane.YES_NO_OPTION) == JOptionPane.CANCEL_OPTION)
						return;
					
					try {
						if(getExtension(lastFile).equalsIgnoreCase(FILE_EXTENSION)){
								objectWriter = new ObjectOutputStream(new FileOutputStream(lastFile.getAbsolutePath()));
								themeListener.statusUpdate("Theme \""+ originalTheme.getThemeName() +"\"saved successfully at \""+ lastFile.getAbsolutePath() +"\"", StatusPanel.CORRECT);
						}else{
							objectWriter = new ObjectOutputStream(new FileOutputStream(lastFile.getAbsolutePath() + "." + FILE_EXTENSION));
							themeListener.statusUpdate("Theme \""+ originalTheme.getThemeName() +"\"saved successfully at \""+ lastFile.getAbsolutePath() +"."+ FILE_EXTENSION +"\"", StatusPanel.CORRECT);
						}
						objectWriter.writeObject(originalTheme);
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(StylePanel.this.parent, "An error occured while exporting the file");
						themeListener.statusUpdate("Theme save failed", StatusPanel.ALERT);
					}finally{
						if(objectWriter != null)
							try {
								objectWriter.close();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(StylePanel.this.parent, "File saved might be damaged, please save it again.");
								themeListener.statusUpdate("Theme save failed", StatusPanel.ALERT);
							}
					}
				}
			}
		});
		
		// on load theme
		loadTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the path
				if(lastFile != null)
					fileChooser.setCurrentDirectory(lastFile);
				
				//open window, if hit cancel then exit
				if(fileChooser.showOpenDialog(StylePanel.this.parent) == JFileChooser.CANCEL_OPTION)
					return;
				
				//save file as last file opened
				lastFile = fileChooser.getSelectedFile();
				
				//verify that file ext is correct
				if(lastFile != null && themeListener != null){
					if(getExtension(lastFile).equalsIgnoreCase(FILE_EXTENSION)){
						try {
							objectReader = new ObjectInputStream(new FileInputStream(lastFile.getAbsoluteFile()));
							RotTheme loadedTheme = (RotTheme) objectReader.readObject();
							availableThemes.add(loadedTheme);
							styleListModel.addElement(loadedTheme.getThemeName());
							themeListener.statusUpdate("Theme loaded successfully", StatusPanel.CORRECT);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(StylePanel.this.parent, "An error occurred while loading the file.");
							themeListener.statusUpdate("Theme load failed", StatusPanel.ALERT);
						} finally{
							if(objectReader != null)
								try {
									objectReader.close();
								} catch (IOException e) {
									JOptionPane.showMessageDialog(StylePanel.this.parent, "The file loaded might be damaged. Please load it again.");
								}
						}
						
					}else{
						JOptionPane.showMessageDialog(StylePanel.this.parent, "You selected an unsupported file theme.");
						themeListener.statusUpdate("Unsupported file. Theme load failed.", StatusPanel.ALERT);
					}
				}
			}
		});
	}
	
	//get extension
	public String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	/**
	 * Add theme listener, on change the listener will handle the action
	 * @param encryptDecryptThemeListener Listener
	 * */
	public void addThemeListener(EncryptDecryptThemeListener encryptDecryptThemeListener){
		themeListener = encryptDecryptThemeListener;
	}
	
	/**
	 * Set the original theme
	 * @param originalTheme Original theme
	 * */
	public void setOriginalTheme(RotTheme originalTheme){
		this.originalTheme = originalTheme;
	}
	
	/**
	 * Get Default Theme
	 * @return default theme
	 * */
	public RotTheme getDefaultTheme(){
		return defaultTheme;
	}
	
	/**
	 * Theme Creator Dialog, allow to create a new theme
	 * */
	private class ThemeCreator extends JDialog implements WindowListener {
		//Color chooser
		private JColorChooser colorChooser;
		
		//Input properties
		private JComboBox inputFontNameList;
		private JComboBox inputFontSizeList;
		private JComboBox inputFontStyleList;
		
		//Output properties
		private JComboBox outputFontNameList;
		private JComboBox outputFontSizeList;
		private JComboBox outputFontStyleList;
		
		//Cancel button
		private JButton cancelButton;
		
		public ThemeCreator() {
			setTitle("Create a new theme");
			setLayout(new BorderLayout());
			
			//set icon
			setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/lock.png")).getImage());
			
			// add window listener
			addWindowListener(this);
			
			//create panels
			JPanel dialogPanel = new JPanel();
			JPanel colorContainer = new JPanel();
			JPanel inputOutputContainer = new JPanel();
			JPanel inputPanel = new JPanel();
			JPanel outputPanel = new JPanel();
			JPanel buttonBar = new JPanel();
			JPanel topPanel = new JPanel();
			
			//Create a Font Chooser
			FontChooser fontChooser = new FontChooser();
			
			//configure dialogPanel
			dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
			
			//configure buttonBar
			buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.LINE_AXIS));
			
			// create color chooser
			colorChooser = new JColorChooser();
			colorChooser.setPreviewPanel(new JPanel());
			
			//configure input output panel
			inputOutputContainer.setLayout(new GridLayout(1,2));
			inputPanel.setLayout(new GridBagLayout());
			outputPanel.setLayout(new GridBagLayout());
			inputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createTitledBorder("Input style")));
			outputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),BorderFactory.createTitledBorder("Output style")));
			
			//deafult input output pref
			GridBagConstraints gc= new GridBagConstraints();
			gc.fill = GridBagConstraints.NONE;
			gc.weightx = 1;
			gc.weighty = 1;
			gc.anchor = GridBagConstraints.LINE_START;
			
			String fontDisplayNames[] = {"Arial","Verdana","Serif"};
			String fontDisplayStyles[] = {"Regular","Italic","Bold","Bold Italic"};
			
			//input componenent
			final JCheckBox inputBackgroundColor = new JCheckBox("Background color ");
			final JCheckBox inputTextColor = new JCheckBox("Text color ");
			
			DefaultComboBoxModel inputFontNameModel = new DefaultComboBoxModel();
			for(String fontName : fontDisplayNames)
				inputFontNameModel.addElement(fontName);
			inputFontNameList = new JComboBox(inputFontNameModel);
			
			DefaultComboBoxModel inputFontSizeModel = new DefaultComboBoxModel();
			for(int fontSize : fontSizes)
				inputFontSizeModel.addElement(fontSize);
			inputFontSizeList = new JComboBox(inputFontSizeModel);
			
			DefaultComboBoxModel inputFontStyleModel = new DefaultComboBoxModel();
			for(String fontStyle : fontDisplayStyles)
				inputFontStyleModel.addElement(fontStyle);
			inputFontStyleList = new JComboBox(inputFontStyleModel);
			
			//output componenent
			final JCheckBox outputBackgroundColor = new JCheckBox("Background color ");
			final JCheckBox outputTextColor = new JCheckBox("Text color ");
			
			DefaultComboBoxModel outputFontNameModel = new DefaultComboBoxModel();
			for(String fontName : fontDisplayNames)
				outputFontNameModel.addElement(fontName);
			outputFontNameList = new JComboBox(outputFontNameModel);
			
			DefaultComboBoxModel outputFontSizeModel = new DefaultComboBoxModel();
			for(int fontSize : fontSizes)
				outputFontSizeModel.addElement(fontSize);
			outputFontSizeList = new JComboBox(outputFontSizeModel);
			
			DefaultComboBoxModel outputFontStyleModel = new DefaultComboBoxModel();
			for(String fontStyle : fontDisplayStyles)
				outputFontStyleModel.addElement(fontStyle);
			outputFontStyleList = new JComboBox(outputFontStyleModel);
			
			//set default values
			inputFontNameList.setSelectedIndex(0);
			inputFontSizeList.setSelectedIndex(1);
			inputFontStyleList.setSelectedIndex(0);
			
			outputFontNameList.setSelectedIndex(0);
			outputFontSizeList.setSelectedIndex(1);
			outputFontStyleList.setSelectedIndex(0);
			
			//topBar components
			final JTextField themeName = new JTextField(20);
			
			//buttonBar components
			JButton createButton = new JButton("Create theme");
			cancelButton = new JButton("Cancel");
			
			//Add components to the input output panel
			gc.gridy = 0;
			gc.gridx = 0;
			inputPanel.add(inputBackgroundColor,gc);
			outputPanel.add(outputBackgroundColor,gc);
			
			gc.gridy++;
			gc.gridx = 0;
			inputPanel.add(inputTextColor,gc);
			outputPanel.add(outputTextColor,gc);
			
			gc.gridy++;
			gc.gridx = 0;
			inputPanel.add(new JLabel("Font name "),gc);
			outputPanel.add(new JLabel("Font name "),gc);
			gc.gridx = 1;
			inputPanel.add(inputFontNameList,gc);
			outputPanel.add(outputFontNameList,gc);

			gc.gridy++;
			gc.gridx = 0;
			inputPanel.add(new JLabel("Font style "),gc);
			outputPanel.add(new JLabel("Font style "),gc);
			gc.gridx = 1;
			inputPanel.add(inputFontStyleList,gc);
			outputPanel.add(outputFontStyleList,gc);
			
			gc.gridy++;
			gc.gridx = 0;
			inputPanel.add(new JLabel("Font size "),gc);
			outputPanel.add(new JLabel("Font size "),gc);
			gc.gridx = 1;
			inputPanel.add(inputFontSizeList,gc);
			outputPanel.add(outputFontSizeList,gc);
			
			// add topPanel component
			topPanel.add(new JLabel("Theme name"));
			topPanel.add(themeName);
			
			// add buttonPanel component
			buttonBar.add(Box.createHorizontalGlue());
			buttonBar.add(createButton);
			buttonBar.add(Box.createRigidArea(new Dimension(20,40)));
			buttonBar.add(cancelButton);
			buttonBar.add(Box.createHorizontalGlue());
			
			//add listeners when color change
			colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(themeListener != null){
						if(inputBackgroundColor.isSelected())
							tmpTheme.setInputBackgroundColor(colorChooser.getColor());
						
						if(inputTextColor.isSelected())
							tmpTheme.setInputTextColor(colorChooser.getColor());
						
						if(outputBackgroundColor.isSelected())
							tmpTheme.setOutputBackgroundColor(colorChooser.getColor());

						if(outputTextColor.isSelected())
							tmpTheme.setOutputTextColor(colorChooser.getColor());
						
						themeListener.themePicked(tmpTheme);
					}
				}
			});
			
			//on cancel click
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					exit();
				}
			});
			
			// when a component is selected
			inputFontNameList.addActionListener(fontChooser);
			inputFontSizeList.addActionListener(fontChooser);
			inputFontStyleList.addActionListener(fontChooser);
			outputFontNameList.addActionListener(fontChooser);
			outputFontSizeList.addActionListener(fontChooser);
			outputFontStyleList.addActionListener(fontChooser);
			
			// on create click
			createButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(themeName.getText().trim().equals("")){
						JOptionPane.showMessageDialog(ThemeCreator.this, "Please enter a name for your theme");
						return;
					}
					
					tmpTheme.setThemeName(themeName.getText());
					availableThemes.add(new RotTheme(tmpTheme));
					styleListModel.addElement(tmpTheme.getThemeName());
					styleList.setSelectedIndex(availableThemes.size()-1);
					originalTheme = availableThemes.get(availableThemes.size()-1);
					if(themeListener != null)
						themeListener.themePicked(originalTheme);
					ThemeCreator.this.dispose();
					themeListener.statusUpdate("Theme \""+ tmpTheme.getThemeName() +"\"created", StatusPanel.CORRECT);
				}
			});
			
			//add to the color container panel
			colorContainer.add(colorChooser);
			colorContainer.setPreferredSize(new Dimension(100,100));
			
			//add input output panel to inputOutputPanel
			inputOutputContainer.add(inputPanel);
			inputOutputContainer.add(outputPanel);
			
			//add panels to dialog
			dialogPanel.add(colorContainer);
			dialogPanel.add(inputOutputContainer);
			
			//add
			add(dialogPanel,BorderLayout.CENTER);
			add(buttonBar,BorderLayout.SOUTH);
			add(topPanel,BorderLayout.NORTH);
			
			// panel preference
			setSize(600,600);
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(parent);
		}
		
		/**
		 * Class Listener executed on any change in the input/output properties
		 * */
		private class FontChooser implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				tmpTheme.setInputFont(new Font(fontNames[inputFontNameList.getSelectedIndex()],fontStyles[inputFontStyleList.getSelectedIndex()],fontSizes[inputFontSizeList.getSelectedIndex()]));
				tmpTheme.setOutputFont(new Font(fontNames[outputFontNameList.getSelectedIndex()],fontStyles[outputFontStyleList.getSelectedIndex()],fontSizes[outputFontSizeList.getSelectedIndex()]));
				if(themeListener != null)
					themeListener.themePicked(tmpTheme);
			}
		}
		
		/**
		 * When cancel, restore previous theme
		 * */
		public void exit(){
			if(themeListener != null){
				themeListener.themePicked(originalTheme);
				themeListener.statusUpdate("Cancel new theme", StatusPanel.INFO);
			}
			ThemeCreator.this.dispose();
		}
		
		//Window Listener methods
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}
		public void windowClosing(WindowEvent arg0) {exit();} // restore theme when close
		public void windowDeactivated(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
		public void windowOpened(WindowEvent arg0) {}
	}
}
