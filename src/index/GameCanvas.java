package index;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by matt1201 on 2016/8/7.
 */
public class GameCanvas extends Canvas {
	MainScene scene = new MainScene();		
			
    public void onDraw(BufferedImage image){
        Graphics graphics = getGraphics();         
        if(graphics!=null)
            graphics.drawImage(image, 0, 0, null);

    }
    //TODO set font in screen, bullet missing
    protected void showTextOnScreen(){
    	Graphics g = getGraphics();
    	
    	String scord="Scords: ";
    	String fps ="FPS: "+ (int)scene.FPS;
    	
    
    }
}
