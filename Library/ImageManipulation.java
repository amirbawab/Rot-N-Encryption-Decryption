package Library;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * COMP 249 - Assignment 4 Q1
 * Due Friday, April 11, 2014
 * 
 * Image Manipulation, responsive image as background
 * @version 1.0
 * */
public class ImageManipulation extends JPanel {
	  private Image img;

	  /**
	   * @param img Bakcground image
	   * */
	  public ImageManipulation(Image img) {
	    this.img = img;
	  }

	  @Override
	  public void paintComponent(Graphics g) {
		  g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // draw the image
	  }

	}