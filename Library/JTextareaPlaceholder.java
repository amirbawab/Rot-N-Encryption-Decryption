package Library;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Rot Theme version of JTextarea. Feature: Text hint / Place holder
 * @version 1.0
 * */
public class JTextareaPlaceholder extends JTextArea implements FocusListener {
	private static final long serialVersionUID = 1L;

	private String placeholder;
	private Font normalFont;
	private Font placeholderFont;
	public boolean empty;
	
	/**
	 * @param placeholder Default text when empty
	 * */
	public JTextareaPlaceholder(String placeholder) {
		super();
		this.addFocusListener(this);
		this.placeholder = placeholder;

		empty = true;
		this.normalFont = this.getFont();
		this.placeholderFont = new Font(normalFont.getFontName(), Font.ITALIC,
				normalFont.getSize());

		this.focusLost(null);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (empty) {
			this.setFont(normalFont);
			this.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			this.setFont(placeholderFont);
			empty = true;
			this.setText(placeholder);
		}else{
			empty = false;
		}
	}

	@Override
	public String getText() {
		String text = super.getText();
		return text;
	}
	
	/**
	 * Refresh text area to check if empty and set the corresponding font and text
	 * */
	public void refresh(){
		if(empty){
			this.setFont(placeholderFont);
			this.setText(placeholder);
		}
	}
	
	/**
	 * Modify normal font
	 * @param font New font
	 * */
	public void setNormalFont(Font font){
		this.normalFont = font;
	}
	
	/**
	 * Modify text hing font
	 * @param font New font
	 * */
	public void setPlaceholderFont(Font font){
		this.placeholderFont = new Font(font.getName(), Font.ITALIC, font.getSize());
	}
}