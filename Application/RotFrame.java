package Application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import Library.ImageManipulation;
import Library.RotTheme;
import Listener.ConfigEncryptDecryptListener;
import Listener.EncryptDecryptTextModifyListener;
import Listener.EncryptDecryptThemeListener;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Rot Frame, main frame
 * @version 1.0
 * */
public class RotFrame extends JFrame {
	// Panels
	private ConfigPanel cp;
	private EncryptDecryptPanel edp;
	private StatusPanel sp;
	private StylePanel stylePanel;
	
	//Title
	private final static String TITLE = "Rot-n Encryption/Decryption";
	
	public RotFrame() {
		super(TITLE);

		//set the background and layout of JFrame
		ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/bg1.jpg"));
		ImageManipulation bg = new ImageManipulation(backgroundImage.getImage());
		setContentPane(bg);
		setLayout(new BorderLayout());

		//set icon
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/lock.png")).getImage());
		
		//set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		//Initialize panels
		RotMenuBar menuBar = new RotMenuBar();
		edp = new EncryptDecryptPanel();
		cp = new ConfigPanel(this);
		sp = new StatusPanel();
		stylePanel = new StylePanel(this);
		
		//remove background for child panels
		cp.setOpaque(false);
		edp.setOpaque(false);
		sp.setOpaque(false);
		stylePanel.setOpaque(false);
		
		//set default theme
		edp.setTheme(stylePanel.getDefaultTheme());
		
		//by default, stylePanel is not visible
		stylePanel.setVisible(false);
		
		//Set the configuration Encryption Decryption listener
		cp.addConfigEncryptDecryptListener(new ConfigEncryptDecryptListener() {
			public String getInput() {
				return edp.getInput();
			}

			public void setOutput(String text) {
				edp.setOutput(text);
			}

			public void setInput(String text) {
				edp.setInput(text);
			}

			public void setCompareMode(boolean b) {
				edp.setCompareMode(b);
			}

			public void statusUpdate(String text, int mode) {
				sp.setText(text, mode);
			}
		});
		
		//Set the Encryption Decryption text modification listener
		edp.addEncryptDecryptTextModifiedListener(new EncryptDecryptTextModifyListener() {
			public void setCompareModeEnabled(boolean enabled) {
				cp.setCompareModeAndMapEnabaled(enabled);
			}
		});
		
		//Set the theme Listener
		stylePanel.addThemeListener(new EncryptDecryptThemeListener() {
			public void themePicked(RotTheme theme) {
				edp.setTheme(theme);
				
				//uncheck compare mode
				cp.setCheckedCompareMode(false);
				edp.setCompareMode(false);
			}

			public void previewOpened() {
				//backup the original theme in case of cancel
				stylePanel.setOriginalTheme(edp.getTheme());
				
				//uncheck compare mode
				cp.setCheckedCompareMode(false);
				edp.setCompareMode(false);
			}

			public void statusUpdate(String text, int mode) {
				sp.setText(text, mode);
			}
		});
		
		//Add panels to frame
		add(edp,BorderLayout.CENTER);
		add(cp,BorderLayout.WEST);
		add(sp,BorderLayout.SOUTH);
		add(stylePanel,BorderLayout.NORTH);
		
		//set JMenu
		setJMenuBar(menuBar.getMenuBar());
		
		// Frame preference
		setVisible(true);
		setSize(1000,700);
		Dimension dim = new Dimension();
		dim.setSize(700,500);
		setMinimumSize(dim);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	/**
	 * Top menu bar implements Window Listener
	 * */
	private class RotMenuBar implements WindowListener{
		// Instances
		private JMenuBar menuBar;
		private File lastFile;
		private Scanner fileInputReader;
		private PrintWriter fileOutputWriter;
		private JFileChooser fileChooser;
		
		//constants
		private String FILE_EXTENSION = "txt";
		
		public RotMenuBar() {
			menuBar = new JMenuBar();
			RotFrame.this.addWindowListener(this);
			
			//fileChooser
			fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileFilter() {
				//File extension to be displayed on the file chooser
				public String getDescription() {
					return FILE_EXTENSION;
				}
				
				//the files that should appear on the file chooser
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
			
			//File
			JMenu file = new JMenu("File");
			JMenuItem importTxt = new JMenuItem("Import TXT");
			JMenuItem exportTxt = new JMenuItem("Export as TXT");
			JMenuItem restart = new JMenuItem("Restart");
			JMenuItem exit = new JMenuItem("Exit");
			file.add(importTxt);
			file.add(exportTxt);
			file.add(restart);
			file.add(exit);
			
			//View
			JMenu view = new JMenu("View");
			final JCheckBoxMenuItem showStyle = new JCheckBoxMenuItem("Style");
			JMenu encryptDecryptSplit = new JMenu("Encrypt Decrypt");
			ButtonGroup encryptDecryptGroup = new ButtonGroup();
			JRadioButtonMenuItem horizontal = new JRadioButtonMenuItem("Split horizontally");
			JRadioButtonMenuItem vertical = new JRadioButtonMenuItem("Split vertically");
			vertical.setSelected(true);
			encryptDecryptGroup.add(vertical);
			encryptDecryptGroup.add(horizontal);
			encryptDecryptSplit.add(vertical);
			encryptDecryptSplit.add(horizontal);
			view.add(encryptDecryptSplit);
			view.add(showStyle);
			
			//Help
			JMenu help = new JMenu("Help");
			JMenuItem about = new JMenuItem("About");
			JMenuItem guide = new JMenuItem("Tour guide");
			help.add(guide);
			help.add(about);
			
			//Add Menu to MenuBar
			menuBar.add(file);
			menuBar.add(view);
			menuBar.add(help);

			//START LISTENERS
			//Exit Options & Listeners
			exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			});
			
			//View>Style Listener
			showStyle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(showStyle.isSelected())
						stylePanel.setVisible(true);
					else
						stylePanel.setVisible(false);
				}
			});
			
			//View>Encrypt decrypt
			horizontal.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					edp.toggleOrientation(true);
				}
			});
			
			vertical.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					edp.toggleOrientation(false);
				}
			});
			
			//Help>About
			about.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AboutDialog(RotFrame.this);
				}
			});
			
			//Help>Tour Guide
			guide.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new WelcomeGuide(RotFrame.this);
				}
			});
			
			//File>Import TXT
			importTxt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK));
			importTxt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//Set the path
					if(lastFile != null)
						fileChooser.setCurrentDirectory(lastFile);
					
					//open window, if hit cancel then exit
					if(fileChooser.showOpenDialog(RotFrame.this) == JFileChooser.CANCEL_OPTION)
						return;
					
					//save file as last file opened
					lastFile = fileChooser.getSelectedFile();
					
					//verify that file ext is correct
					if(lastFile != null){
						if(getExtension(lastFile).equalsIgnoreCase(FILE_EXTENSION)){
							try {
								fileInputReader = new Scanner(lastFile);
								//empty old input
								edp.setInput("");
								while(fileInputReader.hasNextLine()){
									edp.appendInput(fileInputReader.nextLine()+"\n");
								}
								//update status
								sp.setText("File input imported successfully", StatusPanel.CORRECT);
							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(RotFrame.this, "An error occurred while loading the file.");
								sp.setText("Import input file failed", StatusPanel.ALERT);
							} finally{
								if(fileInputReader != null)
									fileInputReader.close();
							}
							
							// disable comapre mode
							edp.setCompareMode(false);
							cp.setCompareModeAndMapEnabaled(false);
						}else{
							JOptionPane.showMessageDialog(RotFrame.this, "You selected an unsupported file input.");
							sp.setText("Unsupported input file. File load failed", StatusPanel.ALERT);
						}
					}
				}
			});
			
			//File>Export
			exportTxt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
			exportTxt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(edp.isEmptyOutput()){
						JOptionPane.showMessageDialog(RotFrame.this, "You can't export an empty output");
						sp.setText("Empty output, export failed", StatusPanel.ALERT);
						return;
					}
					
					//Set the path
					if(lastFile != null)
						fileChooser.setCurrentDirectory(lastFile);
					
					//save window, if hit cancel then exit
					if(fileChooser.showSaveDialog(RotFrame.this) == JFileChooser.CANCEL_OPTION)
						return;
					
					//save file as last file opened
					lastFile = fileChooser.getSelectedFile();
					
					//verify that file ext is correct
					if(lastFile != null){
						//If file exist, ask the user if replace file
						if(lastFile.exists() && JOptionPane.showConfirmDialog(RotFrame.this, "Would you like to overwrite the existing file ?", "Overwite existing file", JOptionPane.YES_NO_OPTION) == JOptionPane.CANCEL_OPTION)
							return;
						
						try {
							if(getExtension(lastFile).equalsIgnoreCase(FILE_EXTENSION)){
									fileOutputWriter = new PrintWriter(lastFile.getAbsolutePath());
									sp.setText("File output exported successfully at \""+ lastFile.getAbsolutePath() +"\"", StatusPanel.CORRECT);
							}else{
									fileOutputWriter = new PrintWriter(lastFile.getAbsolutePath()+ "." + FILE_EXTENSION);
									sp.setText("File output exported successfully at \""+ lastFile.getAbsolutePath() + "." + FILE_EXTENSION + ".\"", StatusPanel.CORRECT);
							}
							fileOutputWriter.print(edp.getOutput());
						} catch (FileNotFoundException e) {
							JOptionPane.showMessageDialog(RotFrame.this, "An error occured while exporting the file");
							sp.setText("Export output file failed", StatusPanel.ALERT);
						} finally{
							if(fileOutputWriter != null)
								fileOutputWriter.close();
						}
					}
				}
			});
			
			
			//File>Restart
			restart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RotFrame.this.dispose();
					cp = null;
					edp = null;
					sp = null;
					stylePanel = null;
					menuBar= null;
					lastFile = null;
					fileInputReader = null;
					fileOutputWriter = null;
					fileChooser = null;
					System.gc();
					new RotFrame().setTitle(TITLE);
				}
			});
			//END OF LISTENERS
		}//end of constructor
		
		
		/**
		 * Get Menu Bar
		 * @return Menu Bar
		 * */
		public JMenuBar getMenuBar(){
			return menuBar;
		}
		
		/**
		 * Custom exit with pop up question
		 * */
		public void exit(){
			if(JOptionPane.showConfirmDialog(RotFrame.this, "Are you sure you want to exit ?","Exit Rot-n Encryption/Decryption",JOptionPane.YES_NO_OPTION) == 0)
				System.exit(0);
		}
		
		/**
		 * Get extension of a file
		 * @param f File to be evaluated
		 * */
		public String getExtension(File f) {
	        String ext = "";
	        String s = f.getName();
	        int i = s.lastIndexOf('.');
	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
	    }
		
		// Window Listener methods
		public void windowClosed(WindowEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {exit();} // pop up message on exit
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {new WelcomeGuide(RotFrame.this);} // pop up guide tour on open
		
	}//close of JMenuBar
}