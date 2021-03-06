package index;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class _showText extends Sprite {
	static int width = 180;
	static int height = 140;
	public Graphics2D g2d;
	
	private static BufferedImage textBImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);;
	
	public _showText(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
	}

	public _showText(MainScene scene, int width, int height) {
		super(scene, textBImg, width, height);
		
	}
	
	protected void setScore(String score){
		g2d = textBImg.createGraphics();
		g2d.setBackground(new Color(0, 0, 0, 0));
		g2d.clearRect(0, 0, this.get_width(), this.get_height());
		g2d.setFont(new Font("Arial", Font.BOLD, 26));
		g2d.setColor(Color.white);
		g2d.drawString("Score: "+score,0 , height/2);
		g2d.dispose();
	}
	
}
