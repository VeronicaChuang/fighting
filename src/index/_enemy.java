package index;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class _enemy extends Sprite {
	private int x =0, y=0, newY=0, dx=0, ySpeedup=2;
	protected BufferedImage enemy_img =null;	
	private Timer enemyTimer;
	private _enemyMoveTask _enemyTask;	
	boolean leftWall =false;	
	protected int hp_enemy1 = 200, hp_enemy2 = 100, hp_enemy13= 150, hp_enemy4 = 80;
	private int checkY =0;
	
	protected int _enemy_HP;
	protected boolean alive = false;
	
	
	public _enemy(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
		enemyTimer = new Timer();
		_enemyTask = new _enemyMoveTask();		
		
		alive = true; //敵機存活
		
		switch(img_path){//給敵機不同血量
		case "res/enemy1.png":
			_enemy_HP = 50;
			break;
		case "res/enemy2.png":
			_enemy_HP = 20;
			break;
		case "res/enemy3.png":
			_enemy_HP = 30;
			break;
		case "res/enemy4.png":
			_enemy_HP = 40;
			break;
		}
	}

	public _enemy(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
		enemyTimer = new Timer();
		_enemyTask = new _enemyMoveTask();	
		
		alive = true; //敵機存活
		
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
		
		//return enemyY after fire
		checkY = this.get_position().y;		
//		System.out.println("checkY: "+checkY);
		//enemy stop moving after y>800, then send a parameter to tell mainScene it's over
		if(checkY>(main.WINDOWS_HEIGHT+ 40)){//if enemy out of boundary remove
			enemyTimer.cancel();
			enemy_img = this.getImg();
			alive = false; //敵機存活否
		}
//		System.out.println(checkY);
		
	}
	
	protected void destory(){
		enemyTimer.cancel();
		alive = false; //敵機存活
	}
	
	
	
}
