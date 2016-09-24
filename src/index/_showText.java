package index;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class _showText extends Sprite {
	private String showString="";
	BufferedImage textBImg;

	public _showText(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
	}

	public _showText(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		textBImg = img;
	}
	
	protected void setString(String text){
//		showString =text;
		showString ="test";
		textBImg = new BufferedImage(this.get_width(), this.get_height(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = textBImg.createGraphics();
		g2d.fillRect(100, 100, this.get_width(), this.get_height());		
//		g2d.setBackground(new Color(0, 0, 0, 0));
		g2d.setFont(new Font("Serif", Font.BOLD, 20));
		g2d.drawString(showString, 50, 50);
		g2d.dispose();
		
	}
		
//	private BufferedImage process(BufferedImage old) {
//        int w = old.getWidth();
//        int h = old.getHeight();
//        BufferedImage img = new BufferedImage(
//                w, h, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = img.createGraphics();
//        g2d.drawImage(old, 0, 0, null);
//        g2d.setPaint(Color.red);
//        g2d.setFont(new Font("Serif", Font.BOLD, 20));
//        String s = "Hello, world!";
//        FontMetrics fm = g2d.getFontMetrics();
//        int x = img.getWidth() - fm.stringWidth(s) - 5;
//        int y = fm.getHeight();
//        g2d.drawString(s, x, y);
//        g2d.dispose();
//        return img;
//    }
	
	
}
