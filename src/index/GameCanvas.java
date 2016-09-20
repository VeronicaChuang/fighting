package index;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.sound.sampled.*;

import javazoom.jl.player.Player;


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
