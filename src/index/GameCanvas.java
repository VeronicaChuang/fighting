package index;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;
import sun.nio.ch.WindowsAsynchronousChannelProvider;


/**
 * Created by matt1201 on 2016/8/7.
 */
public class GameCanvas extends Canvas {
	MainScene scene = new MainScene();	
	
	protected double FPS=0;
	protected int Scord =0;
	
	//play bg music
	protected music playmusic = new music();
	protected boolean stop =false;
	
	public GameCanvas(){
		
		playmusic.bgMusic();
		playmusic.bgPlay();
		
	}
	
	
	//use timerTask to control bullet move
		class _fighterBulletTask extends TimerTask{
			@Override
			public void run() {
						
			}		
		}
	
	
    public void onDraw(BufferedImage image){ 
        Graphics graphics = getGraphics();         
        if(graphics!=null)
            graphics.drawImage(image, 0, 0, null);
    }

    
}


