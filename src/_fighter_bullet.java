import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class _fighter_bullet extends Sprite {
	private int newX =0;
	private int newY =0;
	private Timer fighterTimer;
	private _fighterBulletTask _fighterTask;
	
	public _fighter_bullet(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);

		fighterTimer = new Timer();
		_fighterTask = new _fighterBulletTask();
		fighterTimer.schedule(_fighterTask, 0, 50);
	}

	public _fighter_bullet(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		
		fighterTimer = new Timer();
		_fighterTask = new _fighterBulletTask();
		fighterTimer.schedule(_fighterTask, 0, 50);
	}
	
	//use timerTask to control bullet move
	class _fighterBulletTask extends TimerTask{
		@Override
		public void run() {
			_bullet_fly_up();			
		}		
	}	
	
	//move bullet
	protected void _bullet_fly_up(){ //give bullet x, y before calling fly up
		int i = 0;
		newX = this.get_position().x; //get bullet now x
		newY = this.get_position().y; //get bullet now y
		
		//set new bullet position after fire with timer		
		this.setPosition(newX, (newY -= MainScene.Velocity_Bullet)); 
	}
	
	

}
