package index;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;

import javazoom.jl.player.Player;
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

    
   
//    protected void bgMusic(){
//    	try {
//			FileInputStream fis = new FileInputStream("res/Music/bgMusic.mp3");
//			Player bgplayer = new Player(fis);
//			bgplayer.play();
//			System.out.println("music on");
//		} catch (Exception e) {
//			System.out.println("bgmusic err");
//			e.printStackTrace();
//		}
//    
//    }
//    	try {
//			AudioInputStream audioIS = AudioSystem.getAudioInputStream(this.getClass().getResource("res/Music/bgMusic.mp3"));
//			Clip clip = AudioSystem.getClip();
//			clip.open(audioIS);
//			clip.start();
//			
//    	} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	
    
//    	AudioPlayer BGPlayer = AudioPlayer.player;
//    	AudioStream BGMusic;
//    	AudioData musicData;
//    	ContinuousAudioDataStream loop =null;
//    	
//    	try {
//			BGMusic = new AudioStream(new FileInputStream("res/Music/bgMusic.mp3"));
//			musicData = BGMusic.getData();
//			loop = new ContinuousAudioDataStream(musicData);
//				
//    	} catch (Exception e) {
//			System.out.println("bg music err");
//			e.printStackTrace();
//		}
//    	BGPlayer.start(loop);
//    }
}


