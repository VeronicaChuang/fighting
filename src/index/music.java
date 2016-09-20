package index;

import java.io.FileInputStream;

import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class music {
	int i =0;
	public music(){
		System.out.println(i++);
		bgMusic();
	}
	
	protected void bgMusic(){
    	try {
			FileInputStream fis = new FileInputStream("res/Music/bgMusic.mp3");
			AdvancedPlayer bgplayer = new AdvancedPlayer (fis);
			System.out.println("player before");
			bgplayer.play();
			System.out.println("music over");
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
    
    }
	protected void explosionMusic(){
		try {
			FileInputStream fis = new FileInputStream("res/Music/explodeEffect.mp3");
			AdvancedPlayer bgplayer = new AdvancedPlayer (fis);
			bgplayer.setPlayBackListener(new PlaybackListener() {
				@Override
				public void playbackFinished(PlaybackEvent arg0) {
					// TODO Auto-generated method stub
					super.playbackFinished(arg0);
				}
			});
			
			bgplayer.play();
			System.out.println("music over");
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
	}
	
	protected void fighterFireMusic(){
		try {
			FileInputStream fis = new FileInputStream("res/Music/fireEffect.mp3");
			AdvancedPlayer bgplayer = new AdvancedPlayer (fis);
			System.out.println("player before");
			bgplayer.play();
			System.out.println("music over");
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
	}
	
	protected void enemyCrashMusic(){
		try {
			FileInputStream fis = new FileInputStream("res/Music/shipDestroyEffect.mp3");
			AdvancedPlayer bgplayer = new AdvancedPlayer (fis);
			System.out.println("player before");
			bgplayer.play();
			System.out.println("music over");
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
	}
	
	protected void menuMusic(){
		try {
			FileInputStream fis = new FileInputStream("res/Music/mainMainMusic.mp3");
			AdvancedPlayer bgplayer = new AdvancedPlayer (fis);
			System.out.println("player before");
			bgplayer.play();
			System.out.println("music over");
		} catch (Exception e) {
			System.out.println("bgmusic err");
			e.printStackTrace();
		}
	}
	
}
