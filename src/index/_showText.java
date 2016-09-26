package index;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class _showText extends Sprite {
	static int width = 180;
	static int height = 140;
	
	private static BufferedImage textBImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);;
//	Graphics2D g2d = textBImg.createGraphics();
	
	public _showText(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
	}

	public _showText(MainScene scene, int width, int height) {
		super(scene, textBImg, width, height);
		
	}
	
	protected void setScore(int score){
//		showString = (String)score;
//		showString ="test";
		
		Graphics2D g2d = textBImg.createGraphics();
//		g2d.clearRect(width, height, this.get_width(), this.get_height());
		g2d.fillRect(width, height, this.get_width(), this.get_height());//fillRect(int x, int y, int width, int height)
		g2d.setFont(new Font("Arial", Font.BOLD, 26));
		g2d.setColor(Color.white);
		g2d.drawString("Score: "+score,0 , height/2);//drawString(String str, int x, int y)
		g2d.dispose();
		
		
	}
	
	protected void setFPS(String fps){
		
//		Graphics2D g2d = textBImg.createGraphics();
////		g2d.clearRect(width, height, this.get_width(), this.get_height());
//		g2d.fillRect(width, height, this.get_width(), this.get_height());//fillRect(int x, int y, int width, int height)
//		g2d.setFont(new Font("Serif", Font.BOLD, 26));
//		g2d.setColor(Color.white);
//		g2d.drawString("FPS: ",0 , height/2);//drawString(String str, int x, int y)
//		g2d.dispose();
	}	
	
}
