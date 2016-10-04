package index;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by matt1201 on 2016/8/9.
 */
public class Fighter extends Animation{
	private int dx=0, dy=0;	
	protected boolean isfighterAlive= true;
	protected int _fighter_HP = 100;
    private static MainScene _main_scene = new MainScene();

    public Fighter(MainScene scene, String img_path, int width, int height, int frame_count) {
        super(scene, null, img_path, width, height, frame_count);
        
    }
    
    public void move(){
    	if(isfighterAlive){
	    	if(dx == -MainScene.Velocity_Fighter){//往左走
	    		if((this._x-45-6)<=0){//-6為血條超出主機的寬度(12/2)
	    			dx =0;
	    			this._x +=dx;
	    		}else{
	    			this._x +=dx;
	    		}
	    	}
	    
	    	if(dx == MainScene.Velocity_Fighter){ //往右走
		    	if((this._x+45+12) >main.WINDOWS_WIDTH){//不超過右牆	//-6為血條超出主機的寬度(12/2) 		
		    			dx = 0;
		    			this._x +=dx;
		    	}else{	    			
		    		this._x += dx;
		    	}	    		
		    }
	    	
	    	if(dy == -MainScene.Velocity_Fighter){//往上走
	    		if((this._y-35)<=0){
	    			dy =0;
	    			this._y +=dy;
	    		}else{
	    			this._y +=dy;
	    		}
	    	}
	    	
	    	if(dy == MainScene.Velocity_Fighter){//往下走
	    		if((this._y+40+26)>=main.WINDOWS_HEIGHT){//主角圖片離邊界長度+血調位置+長度
	    			dy =0;
	    			this._y +=dy;
	    		}else{
	    			this._y +=dy;
	    		}
	    	}    
    	}
    }
    
    public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();		
		if(key == KeyEvent.VK_UP){
			dy = -MainScene.Velocity_Fighter;
		}
		if(key == KeyEvent.VK_DOWN){
			dy = MainScene.Velocity_Fighter;
		}
		if(key == KeyEvent.VK_LEFT){
			dx = -MainScene.Velocity_Fighter;
		}
		if(key == KeyEvent.VK_RIGHT){
			dx = MainScene.Velocity_Fighter;
		}		
	}
    
    public void keyReleased(KeyEvent e){    	
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP){
			dy = 0;
		}
		if(key == KeyEvent.VK_DOWN){
			dy = 0;
		}
		if(key == KeyEvent.VK_LEFT){
			dx = 0;
		}
		if(key == KeyEvent.VK_RIGHT){
			dx = 0;
		}		
	}
    
    @Override
    public Rectangle getBound() {
		return new Rectangle((_x-28), _y, (this.get_width()-20), (this.get_height()-48));

    }

}
