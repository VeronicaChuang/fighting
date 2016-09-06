import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Matt on 2016/8/8.
 */
public class MainScene implements KeyListener{
    class RenderLayer{
        public Sprite Sprite;
        public int Layer;

        public RenderLayer(Sprite sprite, int layer){
            Sprite = sprite;
            Layer = layer;
        }

      /*Collection中的HashMap的key與HashSet的內容值需overridden hashCode()
      	使二個內容相同的物件有相同的hashCode */     
        @Override
        public int hashCode() {
            return Sprite.hashCode();
        }
    }

    public static final int Velocity_Fighter = 4;
    public static final int Velocity_Bullet = 7;
    public static final int Velocity_Bullet_Enemy = 3;
    public static final int Velocity_Enemy = 1;

    private List<RenderLayer> _render_objects = new CopyOnWriteArrayList<>();
    private Insets _rect = null;
    public Insets get_rect(){return _rect;}

    private Fighter _fighter;
    protected _enemy _enemy1;
    protected _enemy _enemy2;
    protected _enemy _enemy3;
    protected _enemy _enemy4;
    private Sprite _sprite_bg1, _sprite_bg2;    
    protected int ny2= 750;
    private List<Integer> userKeys; //for directions
    
    int bg1NewY = main.WINDOWS_HEIGHT / 2;
	int bg2NewY = 1140;
	int dx = 2;
    
    public MainScene() {//bg->fighter->enemy
    	userKeys = new ArrayList<Integer>();//array for direction keys 
    	
        _rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);
        
        _sprite_bg1 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
        _sprite_bg2 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
        
        bgScrol();//set bg1 and bg2 position and scrolling    
     
        _fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
        SpawnFighter();
        addToScene(_fighter);
        
        newEmemy(); //new 出敵機
   
    } 
    
    private void bgScrol(){//not finish
//    	int bg1NewY = main.WINDOWS_HEIGHT / 2;
//    	int bg2NewY = -768;
//    	int dx = 10;
    	//設定bg1初始位置
    	_sprite_bg1.setPosition(main.WINDOWS_WIDTH/2, bg1NewY);//(x,384)
    	//設定bg2初始位置
    	_sprite_bg2.setPosition(main.WINDOWS_WIDTH/2, bg2NewY);//(x,-768)     
        addToScene(_sprite_bg1);
        addToScene(_sprite_bg2);
        
        //set Timer to scrolling the bgs
        Timer bgTimer = new Timer();
        TimerTask bgTask = new TimerTask() {			
			@Override
			public void run() {
				if(bg1NewY <= -375){
					bg1NewY = 1140;
				}
//				if(bg2NewY <= -375){
//					bg2NewY = 1100;
//				}
				bg1NewY -= dx;
				System.out.println("bg1NewY: "+bg1NewY);
				_sprite_bg1.setPosition(main.WINDOWS_WIDTH/2, bg1NewY);//(x,384)
				
				bg2NewY -= dx;
				System.out.println("bg2NewY: "+bg2NewY);
				_sprite_bg2.setPosition(main.WINDOWS_WIDTH/2, bg2NewY);//(x,-768)  
			}
		};
		
		bgTimer.schedule(bgTask, 0, 10);
        
        
    }
    
    

    //重置飛機位置
    private void SpawnFighter(){
        _fighter.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT - 60);
    }

    //更新畫面
    private void updateFrame(){    	
        main.clearSprite();
        
        List<RenderLayer> render_objs = _render_objects;     

        Collections.sort(render_objs,
                new Comparator<RenderLayer>() {
                    public int compare(RenderLayer o1, RenderLayer o2) {
                        return o1.Layer - o2.Layer;
                    }
                });

        for(RenderLayer renderLayer : render_objs)
            main.addSprite(renderLayer.Sprite);
    }

    //更新
    public void update(){        
        updateFrame();
    }
    
    

    //移除場景物件
    private void removeFromScene(Sprite sprite){
        for(int i=0; i<_render_objects.size(); i++)
        if(_render_objects.get(i).hashCode()==sprite.hashCode())
            _render_objects.remove(i);
    }

    //加入場景
    private void addToScene(Sprite sprite){
        addToScene(sprite, 0);
    }

    private void addToScene(Sprite sprite, int layer){
        _render_objects.add(new RenderLayer(sprite, layer));
    }

 //key event
	@Override
	public void keyPressed(KeyEvent e) {
		//if figher.x>0 then move
		if(!userKeys.contains(e.getKeyCode()))
			userKeys.add(new Integer(e.getKeyCode()));		
		_fighter_move();// use combo keys to fly fighter by adding to list
		
//		System.out.println(userKeys);
	}	
	@Override
	public void keyReleased(KeyEvent e) {
		userKeys.remove(new Integer(e.getKeyCode()));		
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	
//call fighter bullet class
    private void SpawnBullet(){    	
    //new bullet
    	_fighter_bullet fBullet = new _fighter_bullet(this, "res\\bullet.png", 16, 20);
    	fBullet.setPosition(_fighter._x, _fighter._y);
    	addToScene(fBullet);
    	System.out.println("begin size:"+_render_objects.size());
    	
    //check if bullet out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask bulletTask = new TimerTask() {
			@Override
			public void run() {
				if(fBullet.bullet_img != null){					
					removeFromScene(fBullet);
					checkBoundary.cancel();//cancel timer when remove bullet
					System.out.println("after size:"+_render_objects.size());
				}
			}
		};
		checkBoundary.schedule(bulletTask, 200, 100);
//    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
    }
    
  //move fighter
	protected void _fighter_move(){
		int x = _fighter._x;
		int y = _fighter._y;		
//		int bgy = _sprite_bg._y;
		
		if(userKeys.contains(KeyEvent.VK_UP)){
			if((y-35)>0){
				y -= Velocity_Fighter;	
			}
//			bgy -= bgDX;
//			_sprite_bg.setPosition(main.WINDOWS_WIDTH/2, bgy);
	
		}
		
		if((y+35)<main.WINDOWS_HEIGHT){
			if(userKeys.contains(KeyEvent.VK_DOWN)){
				y += Velocity_Fighter;	
			}
		}
		
		if(x-45>0){
			if(userKeys.contains(KeyEvent.VK_LEFT)){
				x -= Velocity_Fighter;
			}
		}
		
		if((x+45) < main.WINDOWS_WIDTH){
			if(userKeys.contains(KeyEvent.VK_RIGHT)){
				x += Velocity_Fighter;
			}
		}
//		//發射子彈
		if(userKeys.contains(KeyEvent.VK_SPACE)){//抓不到key時確認輸入法為英文
			SpawnBullet();
			System.out.println("space");
		}
		
		
		_fighter.setPosition(x, y);
//		System.out.println("flight_x: "+x+ "flight_y: "+y);
	}

	
	//create enemies
	protected void newEmemy(){	
		Timer _new_enemy = new Timer();		
		TimerTask enemy2_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy2
				SpawnEnemy2();
			}
		};
		TimerTask enemy3_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy3
				SpawnEnemy3();
			}
		};
		TimerTask enemy4_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy4
				SpawnEnemy4();
			}
		};
		TimerTask enemy1_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy1
				SpawnEnemy1();
			}
		};
		
		//設定自動生出敵機的時間
		_new_enemy.schedule(enemy1_Task, 1000*10, 1000*10);
		_new_enemy.schedule(enemy2_Task, 0, 1000*3);
		_new_enemy.schedule(enemy3_Task, 1000*30, 1000*25);
		_new_enemy.schedule(enemy4_Task, 1000*20, 1000*15);
	}

	private void SpawnEnemy2(){
		_enemy2 = new _enemy(this, "res/enemy2.png", 70, 70);
		_enemy2.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy2);
		_enemy2.sendEnemy();//敵機new出來設定初始位置以後，再開始移動
	}	
	
	private void SpawnEnemy3(){
		_enemy3 = new _enemy(this, "res/enemy3.png", 70, 70);
		_enemy3.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy3);	
		_enemy3.sendEnemy();
	}	
	
	private void SpawnEnemy4(){
		_enemy4 = new _enemy(this, "res/enemy4.png", 70, 70);
		_enemy4.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy4);	
		_enemy4.sendEnemy();
	}	
	
	private void SpawnEnemy1(){
		_enemy1 = new _enemy(this, "res/enemy1.png", 70, 70);
		_enemy1.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy1);
		_enemy1.sendEnemy();
	}	
		
}