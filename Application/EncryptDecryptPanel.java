package Application;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import Library.JTextareaPlaceholder;
import Library.RotTheme;
import Listener.EncryptDecryptTextModifyListener;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Encrypt Decrypt panel, Center panel containing input and output text area
 * @version 1.0
 * */
public class EncryptDecryptPanel extends JPanel{
	// main components
	private JTextareaPlaceholder inputText;
	private JTextareaPlaceholder outputText;
	private JSplitPane splitter;
	private JScrollPane spInput;
	private JScrollPane spOutput;
	
	// locks to prevent recursive calls
	private boolean lockInput;
	private boolean lockOutput;
	
	// used for backing up the default scroll for the output
	private BoundedRangeModel defaultSpOutputScroll;
	
	// Highliter
	private DefaultHighlighter.DefaultHighlightPainter highlighter;
	
	// Flag for the current mode
	private boolean compareMode = false;
	
	// Listener for any text modification
	private EncryptDecryptTextModifyListener encryptDecryptTextModifyListener;
	
	// Current them
	private RotTheme currentTheme;
	
	//place holders
	private String inputPlaceholder = "Enter input here";
	private String outputPlaceholder = "Click \"Go\" to generate output";
	
	public EncryptDecryptPanel() {
		setLayout(new GridLayout());
		
		// Adding borders
		Border outerBorder = BorderFactory.createEmptyBorder(15, 0, 5, 5);
		setBorder(outerBorder);
		
		//Highlight initialize
		highlighter = new DefaultHighlighter.DefaultHighlightPainter(UIManager.getColor("TextArea.selectionBackground"));

		//JTextArea
		inputText = new JTextareaPlaceholder(inputPlaceholder);
		outputText = new JTextareaPlaceholder(outputPlaceholder);
		
		spInput = new JScrollPane(inputText);
		spOutput = new JScrollPane(outputText);
		
		//Splitter
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,spInput,spOutput);
		splitter.setResizeWeight(0.55);
		splitter.setDividerSize(10);
		splitter.setContinuousLayout(true);
		
		//set backgrounds
		splitter.setOpaque(false);
		
		//set Border
		inputText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		outputText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		//Backup the scroll of the input
		defaultSpOutputScroll = spOutput.getVerticalScrollBar().getModel();
		
		// Add text selection listeners
		inputText.getCaret().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				// if user changed the input field, disable compare mode to let him/her re-encrypt
				if(inputText.getText().length() != outputText.getText().length()){
					if(encryptDecryptTextModifyListener != null)
						encryptDecryptTextModifyListener.setCompareModeEnabled(false);
				}
				
				// smart lock, to prevent recursive focus from input to output and vice versa
				lockInput = true;
				if(lockOutput == false && compareMode == true){
					//start highlighting
					int dot = inputText.getCaret().getDot();
					int mark = inputText.getCaret().getMark();
					
					outputText.getHighlighter().removeAllHighlights();
					if(dot - mark == 0)
						inputText.getHighlighter().removeAllHighlights();
					try {
						int start = Math.min(dot, mark);
	                    int end = Math.max(dot, mark);
						outputText.getHighlighter().addHighlight(start, end, highlighter);
					} catch (BadLocationException e1) {}
					
					outputText.getCaret().setDot(dot);
				}
				lockInput = false;
			}
		});
		
		outputText.getCaret().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				// if user changed the output field, disable compare mode to let him/her re-encrypt
				if(inputText.getText().length() != outputText.getText().length()){
					if(encryptDecryptTextModifyListener != null)
						encryptDecryptTextModifyListener.setCompareModeEnabled(false);
				}
				
				// smart lock, to prevent recursive focus from input to output and vice versa
				lockOutput = true;
				if(lockInput == false && compareMode == true){
					//start highlighting
					int dot = outputText.getCaret().getDot();
					int mark = outputText.getCaret().getMark();
					
					inputText.getHighlighter().removeAllHighlights();
					if(dot - mark == 0)
						outputText.getHighlighter().removeAllHighlights();
					try {
						 int start = Math.min(dot, mark);
	                     int end = Math.max(dot, mark);
	                     inputText.getHighlighter().addHighlight(start, end, highlighter);
					} catch (BadLocationException e1) {}
					
					inputText.getCaret().setDot(dot);
				}
				lockOutput = false;
			}
		});
		
		//Add components
		add(splitter);
	}
	
	/**
	 * Set output text
	 * @param text Output text to be placed
	 * */
	public void setOutput(String text){
		outputText.setText(text);
		
		//if hit reset button
		if(text == null){
			emptyTextarea();
			refreshTextarea();
		}else{
			//replace place holder font in case the output text area was empty before
			outputText.setFont(currentTheme.getOutputFont());
			outputText.empty = false;
		}
	}
	
	/**
	 * Set input text
	 * @param text Input text to be placed
	 * */
	public void setInput(String text){
		inputText.setText(text);
	}
	
	/**
	 * append input text
	 * @param text Input text to be appended
	 * */
	public void appendInput(String text){
		if(!text.trim().equals("")){
			//remove place holder text if empty before appending
			if(inputText.empty)
				inputText.setText("");
			inputText.append(text);
			inputText.empty = false;
			inputText.setFont(currentTheme.getInputFont());
		}
	}
	
	/**
	 * Get input text
	 * @return Input text
	 * */
	public String getInput(){
		if(inputText.empty)
			return "";
		return inputText.getText();
	}
	
	/**
	 * Get output text
	 * @return Output text
	 * */
	public String getOutput(){
		return outputText.getText();
	}
	
	/**
	 * Toggle split orientation
	 * @param vertical true for vertical, false for horizontal
	 * */
	public void toggleOrientation(boolean vertical){
		if(vertical)
			splitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		else
			splitter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitter.setDividerLocation(0.5);
	}
	
	/**
	 * Set if compare mode is enabled or not
	 * @param compareMode true for compare mode
	 * */
	public void setCompareMode(boolean compareMode){
		if(compareMode){
			// Lock scroll bar if same font name and size
			if(inputText.getFont().getSize() == outputText.getFont().getSize() && inputText.getFont().getName().equals(outputText.getFont().getName()))
				spOutput.getVerticalScrollBar().setModel(spInput.getVerticalScrollBar().getModel());
			
			// compare flag ON
			this.compareMode = true;
			
			// set not editable
			inputText.setEditable(false);
			outputText.setEditable(false);
		}else{
			// Unlock scroll bar
			spOutput.getVerticalScrollBar().setModel(defaultSpOutputScroll);
			
			// Remove highlights
			outputText.getHighlighter().removeAllHighlights();
			inputText.getHighlighter().removeAllHighlights();
			
			// compare flag OFF
			this.compareMode = false;

			//Set editable
			inputText.setEditable(true);
			outputText.setEditable(true);
		}
	}
	
	/**
	 * Set the listener for text modified event
	 * @param encryptDecryptTextModifyListener Text modeified Listener
	 * */
	public void addEncryptDecryptTextModifiedListener(EncryptDecryptTextModifyListener encryptDecryptTextModifyListener){
		this.encryptDecryptTextModifyListener = encryptDecryptTextModifyListener;
	}
	
	/**
	 * Set theme for input and output
	 * @param theme Theme of type RotTheme
	 * */
	public void setTheme(RotTheme theme){
		//make the new theme as current theme
		currentTheme = theme;
		
		//change display for input
		inputText.setBackground(theme.getInputBackgroundColor());
		inputText.setForeground(theme.getInputTextColor());
		inputText.setFont(theme.getInputFont());
		inputText.setCaretColor(theme.getInputTextColor());
		inputText.setNormalFont(currentTheme.getInputFont());
		inputText.setPlaceholderFont(theme.getInputFont());
		
		//change display for output
		outputText.setBackground(theme.getOutputBackgroundColor());
		outputText.setForeground(theme.getOutputTextColor());
		outputText.setFont(theme.getOutputFont());
		outputText.setCaretColor(theme.getOutputTextColor());
		outputText.setNormalFont(currentTheme.getOutputFont());
		outputText.setPlaceholderFont(theme.getOutputFont());
		
		//refresh text areas, useful if empty text area
		refreshTextarea();
	}
	
	/**
	 * Get current theme
	 * @return current theme
	 * */
	public RotTheme getTheme(){
		return currentTheme;
	}
	
	/**
	 * Make sure if empty, display the place holder text and font
	 * */
	public void refreshTextarea(){
		if(inputText.empty)
			inputText.refresh();
		
		if(outputText.empty)
			outputText.refresh();
	}
	
	/**
	 * Empty the text areas
	 * */
	private void emptyTextarea(){
		inputText.setText("");
		inputText.empty = true;
		
		outputText.setText("");
		outputText.empty = true;
	}
	
	/**
	 * Return if output is empty or has the place holder text
	 * @return output empty or place holder text
	 * */
	public boolean isEmptyOutput(){
		return outputText.empty;
	}
	
	/**
	 * Return if input is empty or has the place holder text
	 * @return input empty or place holder text
	 * */
	public boolean isEmptyInput(){
		return inputText.empty;
	}
}