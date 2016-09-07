package index;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class _enemy extends Sprite {
	private int x =0, y=0, newY=0, dx=0, ySpeedup=2;
	protected BufferedImage bullet_img =null;	
	private Timer enemyTimer;
	private _enemyMoveTask _enemyTask;	
	boolean leftWall =false;	
	protected int hp_enemy1 = 200, hp_enemy2 = 100, hp_enemy13= 150, hp_enemy4 = 80;
	
	public _enemy(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
		enemyTimer = new Timer();
		_enemyTask = new _enemyMoveTask();		
	}

	public _enemy(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		enemyTimer = new Timer();
		_enemyTask = new _enemyMoveTask();		
	}
	
	
	//use timerTask to control bullet move
	class _enemyMoveTask extends TimerTask{
		@Override
		public void run() {
			move();//enemy fly			
		}		
	}	
	
	//敵機移動頻率
	protected void sendEnemy(){ 
		enemyTimer.schedule(_enemyTask, 0, 300);
	}
	
	//enemy move
	protected void move(){ 	
		x = this.get_position().x;
		y = this.get_position().y;
		dx = ((int)(Math.random()*8)+1);	//左右移動距離
		if(x<=45){
			leftWall=true;			//碰到左邊界改+dx
		}
		
		if(x>=main.WINDOWS_WIDTH-45){//碰到右邊界改-dx
			leftWall =false;
		}
		
		if(leftWall){
			this.setPosition((x+=dx), (y += (MainScene.Velocity_Enemy)+ySpeedup));
		}else{		
			this.setPosition((x-=dx), (y += (MainScene.Velocity_Enemy)+ySpeedup));
		}
	}
	
	
	
}
