package Library;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Rot theme, create a theme for the application
 * @version 1.0
 * */
public class RotTheme implements Serializable{
	private String themeName;
	private int themeId;
	public static int themeAutoIncrementId;
	
	private Color inputBackgroundColor;
	private Color inputTextColor;
	private Font inputFont;
	
	private Color outputBackgroundColor;
	private Color outputTextColor;
	private Font outputFont;
	
	/**
	 * @param themeName Theme name
	 * @param backgroundColor I/O text area background color
	 * @param textColor I/O text area text color
	 * @param font I/O text area font
	 * */
	public RotTheme(String themeName,Color backgroundColor, Color textColor, Font font) {
		this.themeName = themeName;
		this.themeId = RotTheme.themeAutoIncrementId++;
		
		this.inputBackgroundColor = backgroundColor;
		this.inputTextColor = textColor;
		this.inputFont = font;
		
		this.outputBackgroundColor = backgroundColor;
		this.outputTextColor = textColor;
		this.outputFont= font;
	}
	
	//copy constructor
	public RotTheme(RotTheme theme){
		this.themeName = theme.themeName;
		this.themeId = theme.themeId;
		
		this.inputBackgroundColor = theme.inputBackgroundColor;
		this.inputTextColor = theme.inputTextColor;
		this.inputFont = theme.inputFont;
		
		this.outputBackgroundColor = theme.outputBackgroundColor;
		this.outputTextColor = theme.outputTextColor;
		this.outputFont= theme.outputFont;
	}

	/**
	 * Get input background color
	 * @return input background color
	 * */
	public Color getInputBackgroundColor() {
		return inputBackgroundColor;
	}

	/**
	 * Set input background color
	 * @param inputBackgroundColor input background color
	 * */
	public void setInputBackgroundColor(Color inputBackgroundColor) {
		this.inputBackgroundColor = inputBackgroundColor;
	}

	/**
	 * Get input text color
	 * @return input text color
	 * */
	public Color getInputTextColor() {
		return inputTextColor;
	}

	/**
	 * Set input text color
	 * @param inputTextColor input text color
	 * */
	public void setInputTextColor(Color inputTextColor) {
		this.inputTextColor = inputTextColor;
	}

	/**
	 * Get output background color
	 * @return output background color
	 * */
	public Color getOutputBackgroundColor() {
		return outputBackgroundColor;
	}

	/**
	 * Set output background color
	 * @param outputBackgroundColor output background color
	 * */
	public void setOutputBackgroundColor(Color outputBackgroundColor) {
		this.outputBackgroundColor = outputBackgroundColor;
	}

	/**
	 * Get output text color
	 * @return output text color
	 * */
	public Color getOutputTextColor() {
		return outputTextColor;
	}

	/**
	 * Set output text color
	 * @param outputTextColor output text color
	 * */
	public void setOutputTextColor(Color outputTextColor) {
		this.outputTextColor = outputTextColor;
	}

	/**
	 * Get input font
	 * @return input font
	 * */
	public Font getInputFont() {
		return inputFont;
	}

	/**
	 * Set input font
	 * @param inputFont Input font
	 * */
	public void setInputFont(Font inputFont) {
		this.inputFont = inputFont;
	}

	/**
	 * Get output font
	 * @return output font
	 * */
	public Font getOutputFont() {
		return outputFont;
	}

	/**
	 * Set output font
	 * @param outputFont output font
	 * */
	public void setOutputFont(Font outputFont) {
		this.outputFont = outputFont;
	}
	
	@Override
	public RotTheme clone(){
		return new RotTheme(this);
	}

	/**
	 * Get theme name
	 * @return theme name
	 * */
	public String getThemeName() {
		return themeName;
	}

	/**
	 * Set theme name
	 * @param themeName Theme name
	 * */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * Get theme id
	 * @return Theme id
	 * */
	public int getThemeId() {
		return themeId;
	}

	/**
	 * Set theme id
	 * @param themeId Theme id
	 * */
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}
}
