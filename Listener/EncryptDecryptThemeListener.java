package Listener;

import Library.RotTheme;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Encryption Decryption Theme Listener, when theme changed or created, call a specific method from the listener
 * @version 1.0
 * */
public interface EncryptDecryptThemeListener {
	void themePicked(RotTheme theme);
	void previewOpened();
	void statusUpdate(String text, int mode);
}
