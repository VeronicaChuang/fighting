package index;

import java.nio.file.Paths;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class music {
	protected MediaPlayer BGmediaPlayer;
	protected boolean stop=false;
	
	public music(){}
	
	protected void bgMusic(){
    	try {
    		 new JFXPanel();//    		 
	         Media hit = new Media(Paths.get("res/Music/bgMusic.mp3").toUri().toString());
	         BGmediaPlayer = new MediaPlayer(hit);
	         BGmediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					BGmediaPlayer.seek(Duration.ZERO);//duration zero, playback music	
				}
			});
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
    
    }
	
	protected void bgPlay(){
		if(BGmediaPlayer != null){
			   BGmediaPlayer.stop();	//TODO BG music can't stop 
		}
		   BGmediaPlayer.setVolume(1.2);	
		   BGmediaPlayer.play();	
	}
	
	protected void bgStop(){
		System.out.println("stop");
		BGmediaPlayer.stop();	//TODO BG music can't stop 
	}
	
	
	protected void explosionMusic(){
		try {
			new JFXPanel();//    		 
	        Media hit = new Media(Paths.get("res/Music/explodeEffect.mp3").toUri().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(hit);
	        mediaPlayer.setVolume(0.6);
	        mediaPlayer.play();
			
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
	}
	
	protected void fighterFireMusic(){
		try {
			new JFXPanel();//    		 
	        Media hit = new Media(Paths.get("res/Music/fireEffect.mp3").toUri().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(hit);
	        mediaPlayer.setVolume(0.6);
	        mediaPlayer.play();
			
		} catch (Exception e) {
			System.out.println("fighterFireMusic err");
			e.printStackTrace();
		}
	}
	
	protected void enemyCrashMusic(){
		try {
			new JFXPanel();//    		 
	        Media hit = new Media(Paths.get("res/Music/shipDestroyEffect.mp3").toUri().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(hit);
	        mediaPlayer.setVolume(0.3);
	        mediaPlayer.play();
			
		} catch (Exception e) {
			System.out.println("enemycrashMusic err");
			e.printStackTrace();
		}
	}
	
	protected void menuMusic(){
		try {
			new JFXPanel();//    		 
	        Media hit = new Media(Paths.get("res/Music/mainMainMusic.mp3").toUri().toString());	       
	        BGmediaPlayer = new MediaPlayer(hit);
			
		} catch (Exception e) {
			System.out.println("menuMusic err");
			e.printStackTrace();
		}
	}
	
}
