package index;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
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
    private Sprite _sprite_bg1, _sprite_bg2;    
    protected int ny2= 750;
    private List<Integer> userKeys; //for directions    
    
    //background setting 
    int bg1NewY = main.WINDOWS_HEIGHT / 2;
	int bg2NewY = 1140;
	int bgSpeed = 30;
    
    public MainScene() {//bg->fighter->enemy
    	userKeys = new ArrayList<Integer>();//array for direction keys     	
        _rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);
        
        //background Image new
        _sprite_bg1 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
        _sprite_bg2 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
       
        //set position and scrolling for bg1 and bg2 
        bgScrol();
     
        _fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
        SpawnFighter();
        addToScene(_fighter);
        
        newEmemy(); 
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

    //scorlling bg1 and bg2
    private void bgScrol(){   	
    	//initial bg1 position
    	_sprite_bg1.setPosition(main.WINDOWS_WIDTH/2, bg1NewY);//(x,384)
    	//initial bg2 position
    	_sprite_bg2.setPosition(main.WINDOWS_WIDTH/2, bg2NewY);//(x,-768)     
        addToScene(_sprite_bg1);
        addToScene(_sprite_bg2);
        
        //set Timer to scrolling the bgs
        Timer bgTimer = new Timer();
        TimerTask bgTask = new TimerTask() {			
			@Override
			public void run() {
				if(bg1NewY <= ((main.WINDOWS_HEIGHT / 2) * -1)){ //if bg1 out of top boundary 
					bg1NewY = ((main.WINDOWS_HEIGHT /2 * 3) - 12);//reset bg1 to the button and repeat scrolling
				}
				if(bg2NewY <= ((main.WINDOWS_HEIGHT / 2) *-1)){
					bg2NewY = ((main.WINDOWS_HEIGHT /2 * 3) - 12);
				}
				bg1NewY -= 1;//arg. for moving bg1.y
//				System.out.println("bg1NewY: "+bg1NewY);
				_sprite_bg1.setPosition(main.WINDOWS_WIDTH/2, bg1NewY);//(x,384)
				
				bg2NewY -= 1;
//				System.out.println("bg2NewY: "+bg2NewY);
				_sprite_bg2.setPosition(main.WINDOWS_WIDTH/2, bg2NewY);//(x,-768)  
			}
		};		
		bgTimer.schedule(bgTask, 0, bgSpeed);
    }
    
    
 //key listener
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
    	_bullet fBullet = new _bullet(this, "res\\bullet.png", 16, 20);
    	fBullet.setPosition(_fighter._x, _fighter._y);
    	addToScene(fBullet);
//    	System.out.println("begin size:"+_render_objects.size());
    	
    //check if bullet out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask bulletTask = new TimerTask() {
			@Override
			public void run() {
				if(fBullet.bullet_img != null){					
					removeFromScene(fBullet);
					checkBoundary.cancel();//cancel timer when remove bullet
//					System.out.println("after size:"+_render_objects.size());
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
		if(userKeys.contains(KeyEvent.VK_UP)){
			if((y-35)>0){
				y -= Velocity_Fighter;	
			}	
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
//			System.out.println("space");
		}
		
		
		_fighter.setPosition(x, y);
//		System.out.println("flight_x: "+x+ "flight_y: "+y);
	}
	
//create enemies TODO 一直長出敵機要在主角死亡後取消TIMER
	protected void newEmemy(){	 
		Timer _new_enemy = new Timer();		
		TimerTask enemy2_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy2
				SpawnEnemy2();			}
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
		_new_enemy.schedule(enemy2_Task, 0, 1000* 5);
		_new_enemy.schedule(enemy3_Task, 1000*30, 1000*25);
		_new_enemy.schedule(enemy4_Task, 1000*20, 1000*15);
	}


//new出敵機與子彈連續發射  TODO 敵機擊中後要消失
	private void SpawnEnemy2(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy2 = new _enemy(this, "res/enemy2.png", 70, 70);
		_enemy2.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy2);
		_enemy2.sendEnemy();//敵機new出來設定初始位置以後，再開始移動		
		
		//bullet連續射擊
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				_bullet enemyBullet = new _bullet(_enemy2._scene, "res\\bullet_enemy.png", 16, 20);	

				enemyBullet.setPosition(_enemy2.get_position().x, _enemy2.get_position().y);
				addToScene(enemyBullet);
//					System.out.println("e bullet");
//		    	System.out.println("begin size:"+_render_objects.size());

				//check if bullet out of boundary
		    	Timer checkBoundary = new Timer();
		    	TimerTask bulletTask = new TimerTask() {
					@Override
					public void run() {
						if(enemyBullet.bullet_img != null){					
							removeFromScene(enemyBullet);
							checkBoundary.cancel();//cancel timer when remove bullet
//								ebulletTimer.cancel();
//							System.out.println("after size:"+_render_objects.size());
						}
					}
				};
				checkBoundary.schedule(bulletTask, 200, 100);
//			    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				
			}
		};
		keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
	}	
	
	private void SpawnEnemy3(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy3 = new _enemy(this, "res/enemy3.png", 70, 70);
		_enemy3.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy3);	
		_enemy3.sendEnemy();
		
		//bullet連續射擊
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				_bullet enemyBullet = new _bullet(_enemy3._scene, "res\\bullet_enemy.png", 16, 20);	

				enemyBullet.setPosition(_enemy3.get_position().x, _enemy3.get_position().y);
				addToScene(enemyBullet);
//							System.out.println("e bullet");
//		    	System.out.println("begin size:"+_render_objects.size());

				//check if bullet out of boundary
		    	Timer checkBoundary = new Timer();
		    	TimerTask bulletTask = new TimerTask() {
					@Override
					public void run() {
						if(enemyBullet.bullet_img != null){					
							removeFromScene(enemyBullet);
							checkBoundary.cancel();//cancel timer when remove bullet
//										ebulletTimer.cancel();
//							System.out.println("after size:"+_render_objects.size());
						}
					}
				};
				checkBoundary.schedule(bulletTask, 200, 100);
//			System.out.println("mainScene hashcode: "+fBullet.hashCode());
				
			}
		};
		keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
	}	
	
	private void SpawnEnemy4(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));

		_enemy _enemy4 = new _enemy(this, "res/enemy4.png", 70, 70);
		_enemy4.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy4);	
		_enemy4.sendEnemy();
		
		//bullet連續射擊
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				_bullet enemyBullet = new _bullet(_enemy4._scene, "res\\bullet_enemy.png", 16, 20);	

				enemyBullet.setPosition(_enemy4.get_position().x, _enemy4.get_position().y);
				addToScene(enemyBullet);
//							System.out.println("e bullet");
//		    	System.out.println("begin size:"+_render_objects.size());

				//check if bullet out of boundary
		    	Timer checkBoundary = new Timer();
		    	TimerTask bulletTask = new TimerTask() {
					@Override
					public void run() {
						if(enemyBullet.bullet_img != null){					
							removeFromScene(enemyBullet);
							checkBoundary.cancel();//cancel timer when remove bullet
//										ebulletTimer.cancel();
//							System.out.println("after size:"+_render_objects.size());
						}
					}
				};
				checkBoundary.schedule(bulletTask, 200, 100);
//					    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				
			}
		};
		keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
	}	
	
	private void SpawnEnemy1(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));

		_enemy _enemy1 = new _enemy(this, "res/enemy1.png", 70, 70);
		_enemy1.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy1);
		_enemy1.sendEnemy();
		
		//bullet連續射擊
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				_bullet enemyBullet = new _bullet(_enemy1._scene, "res\\bullet_enemy.png", 16, 20);	

				enemyBullet.setPosition(_enemy1.get_position().x, _enemy1.get_position().y);
				addToScene(enemyBullet);
//									System.out.println("e bullet");
//		    	System.out.println("begin size:"+_render_objects.size());

				//check if bullet out of boundary
		    	Timer checkBoundary = new Timer();
		    	TimerTask bulletTask = new TimerTask() {
					@Override
					public void run() {
						if(enemyBullet.bullet_img != null){					
							removeFromScene(enemyBullet);
							checkBoundary.cancel();//cancel timer when remove bullet
//												ebulletTimer.cancel();
//							System.out.println("after size:"+_render_objects.size());
						}
					}
				};
				checkBoundary.schedule(bulletTask, 200, 100);
//							    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				
			}
		};
		keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
	}	
		
}