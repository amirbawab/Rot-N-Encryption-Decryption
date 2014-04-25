package Listener;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Configuration Encryotion Decryption Listener, any action done in the configuration {Go, Compare mode etc...}
 * will call a specific method
 * @version 1.0
 * */
public interface ConfigEncryptDecryptListener {
	void setOutput(String text);
	void setInput(String text);
	String getInput();
	void setCompareMode(boolean b);
	void statusUpdate(String text, int mode);
}
