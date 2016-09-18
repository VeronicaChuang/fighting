package index;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class _bullet extends Sprite {
	private int newX =0;
	private int newY =0;
	private int checkY = 800;
	protected BufferedImage bullet_img =null;
	
	private Timer fighterTimer;
	private _fighterBulletTask _fighterTask;
	private Timer enemyTimer;
	private _enemyBulletTask _enemyTask;	
	private boolean isFighter=false;
	protected String imgPath ="";
	
	
	public _bullet(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);		
		imgPath = scene.toString();
//		System.out.println("path: "+img_path);
		
		if(img_path.equals("res\\bullet.png")) {isFighter=true;} 
		if(isFighter){
			fighterTimer = new Timer();
			_fighterTask = new _fighterBulletTask();
			fighterTimer.schedule(_fighterTask, 0, 40);	
		}else{
			enemyTimer = new Timer();
			_enemyTask = new _enemyBulletTask();
			enemyTimer.schedule(_enemyTask, 0, 45);
		}
	}

	public _bullet(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		
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
//		System.out.println("fighter bullet");
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
			enemyTimer.cancel();
			bullet_img = this.getImg();
		}
//		System.out.println(checkY);
	}
	
	protected void destory(){
		if(isFighter){
			fighterTimer.cancel();
		}else{
			enemyTimer.cancel();
		}
	}
	
}
