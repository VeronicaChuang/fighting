package index;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class _fighter_bullet extends Sprite {
	private int newX =0;
	private int newY =0;
	private int checkY = 800;
	protected BufferedImage bullet_img =null;
	
	private Timer fighterTimer;
	private _fighterBulletTask _fighterTask;
	private Timer enemyTimer;
	private _enemyBulletTask _enemyTask;
	
	
	public _fighter_bullet(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);		
		
//		System.out.println("path: "+img_path);
		fighterTimer = new Timer();
		_fighterTask = new _fighterBulletTask();
		fighterTimer.schedule(_fighterTask, 0, 40);	
		
//		enemyTimer = new Timer();
//		_enemyTask = new _enemyBulletTask();
//		enemyTimer.schedule(_enemyTask, 0, 45);
		
	}

	public _fighter_bullet(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		
//		System.out.println("img: "+img);
		fighterTimer = new Timer();
		_fighterTask = new _fighterBulletTask();
		fighterTimer.schedule(_fighterTask, 0, 40);	
		
//		enemyTimer = new Timer();
//		_enemyTask = new _enemyBulletTask();
//		enemyTimer.schedule(_enemyTask, 0, 45);
		
	}
	
	//use timerTask to control bullet move
	class _fighterBulletTask extends TimerTask{
		@Override
		public void run() {
			_bullet_fly_up();//fighter bullet fly			
		}		
	}	
	
	class _enemyBulletTask extends TimerTask{
		@Override
		public void run() {
			_bullet_fly_down();//enemy bullet fly			
		}		
	}	
	
	//move bullet
	protected void _bullet_fly_up(){ //give bullet x, y before calling fly up
		newX = this.get_position().x; //get bullet now x
		newY = this.get_position().y; //get bullet now y
		
		//set new bullet position after fire with timer		
		this.setPosition(newX, (newY -= MainScene.Velocity_Bullet));
		
		//return bulletY after fire
		checkY = this.get_position().y;		
		
		//bullet stop moving after y<0, then send a parameter to tell mainScene it's over
		if(checkY<0){
			fighterTimer.cancel();
			bullet_img = this.getImg();
		}
//		System.out.println(checkY);
	}
	
	protected void _bullet_fly_down(){ //give bullet x, y before calling fly up
		newX = this.get_position().x; //get bullet now x
		newY = this.get_position().y; //get bullet now y
		
		//set new bullet position after fire with timer		
		this.setPosition(newX, (newY += MainScene.Velocity_Bullet_Enemy));
		
		//return bulletY after fire
		checkY = this.get_position().y;		
		
		//bullet stop moving after y<0, then send a parameter to tell mainScene it's over
		if(checkY>main.WINDOWS_HEIGHT){
//			fighterTimer.cancel();
			bullet_img = this.getImg();
		}
//		System.out.println(checkY);
	}
}
