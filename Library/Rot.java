package Library;

import java.util.ArrayList;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Rot-N algorithm, Encryption / Decryption
 * @version 1.0
 * */
public class Rot {
	private ArrayList<String> letters;
	private ArrayList<String> encryptedLetters;
	
	public Rot(){
		letters = new ArrayList<String>();
		encryptedLetters = new ArrayList<String>();
	}
	
	/**
	 * Encrypt text
	 * @param message Message to encrypt
	 * @param rotN Rot number
	 * @param encryptDigit True or False
	 * @return encrypted message
	 * */
	public String encrypt(String message, int rotN, boolean encryptDigit){
		String out = "";
		char encryptedCurrent;
		char current;
		int typeAlpha;
		
		//clear arrays
		letters.clear();
		encryptedLetters.clear();
		
		for(int i=0; i<message.length();i++){
			current = message.charAt(i);
			if((typeAlpha = typeAlpha(current)) != 0){
				encryptedCurrent = (char) (current + rotN);
				if(typeAlpha == 1 && encryptedCurrent > 'z' || typeAlpha == 2 && encryptedCurrent > 'Z')
					encryptedCurrent -= 'z' - 'a' + 1;
			}else if(encryptDigit && isNumeric(current)){
				encryptedCurrent = (char) (current + rotN%10);
				if(encryptedCurrent > '9')
					encryptedCurrent -= '9' - '0' + 1;
			} else {
				encryptedCurrent = current;
			}
			
			if((typeAlpha != 0 || (isNumeric(current) && encryptDigit)) && letters.indexOf(current+"") == -1){
				letters.add(current+"");
				encryptedLetters.add(encryptedCurrent+"");
			}
				
			out += encryptedCurrent;
		}
		return out;
	}
	
	/**
	 * Decrypt text
	 * @param message Message to decrypt
	 * @param rotN Rot number
	 * @param decryptDigit True or False
	 * @return decrypted message
	 * */
	public String decrypt(String message, int rotN, boolean decryptDigit){
		String out = "";
		char encryptedCurrent;
		char current;
		int typeAlpha;
		
		//clear arrays
		letters.clear();
		encryptedLetters.clear();
		
		for(int i=0; i<message.length();i++){
			current = message.charAt(i);
			if((typeAlpha = typeAlpha(current)) != 0){
				encryptedCurrent = (char) (current - rotN);
				if(typeAlpha == 1 && encryptedCurrent < 'a' || typeAlpha == 2 && encryptedCurrent < 'A')
					encryptedCurrent += 'z' - 'a' + 1;
			}else if(decryptDigit && isNumeric(current)){
				encryptedCurrent = (char) (current - rotN%10);
				if(encryptedCurrent < '0')
					encryptedCurrent += '9' - '0' + 1;
			} else {
				encryptedCurrent = current;
			}
			
			if((typeAlpha != 0 || (isNumeric(current) && decryptDigit)) && letters.indexOf(current+"") == -1){
				letters.add(current+"");
				encryptedLetters.add(encryptedCurrent+"");
			}
			
			out += encryptedCurrent;
		}
		return out;
	}

	/**
	 * Check if the character is alphabetic
	 * @param c Character
	 * @return 1 => Lowercase, 2 => Uppercase, 3 => Not alpha
	 * */
	public int typeAlpha(char c) {
		if(c >= 'a' && c <= 'z') 
			return 1;
		if(c >= 'A' && c <= 'Z')
			return 2;
		return 0;
	}

	/**
	 * Check if character is a number
	 * @param c Character
	 * @return true or false
	 * */
	public boolean isNumeric(char c) {
		return c >= '0' && c <= '9';
	}
	
	/**
	 * Get original letters
	 * @return original letters
	 * */
	public ArrayList<String> getLetters(){
		return letters;
	}
	
	/**
	 * Get encrypted letters
	 * @return encrypted/decrypted letters
	 * */
	public ArrayList<String> getEncryptedLetters(){
		return encryptedLetters;
	}
}
