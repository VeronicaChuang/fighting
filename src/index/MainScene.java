package index;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.omg.stub.java.rmi._Remote_Stub;

/**
 * Created by Matt on 2016/8/8.
 */
public class MainScene implements KeyListener{
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
	
	//FPS
	int frames =0; //更新次數
	long getStartTime=0;
	long lastTime=0;  //上次FPS計算時間
	long updateTimer=1000; // FPS計算周期
	double FPS=0; //fps結果
	
	//game info
	Font font = new Font("Helvetica", Font.BOLD, 16);
	String fpsInfo ="";
	protected int scord=0;
	boolean isgameOver =false;
	
	
	//collision detected
	int _fighter_HP = 100;
	ArrayList<_bullet> _fighter_Missile;
	ArrayList<_enemy> _enemyArray;
	ArrayList<_bullet> _enemy_Millsile;
	
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
	
    public MainScene() {//bg->fighter->enemy
    	userKeys = new ArrayList<Integer>();//array for direction keys     	
        _rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);
        
        //construction list
        _fighter_Missile = new ArrayList<>();
    	_enemyArray = new ArrayList<>();
    	_enemy_Millsile = new ArrayList<>();
        
        //background Image new
        _sprite_bg1 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
        _sprite_bg2 = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
       
        //set position and scrolling for bg1 and bg2 
        bgScrol();
     
        _fighter = new Fighter(this, "res\\fighter.png", 85, 55, 3);
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
	        if(_fighter.isfighterAlive){//fighter還活著才繼續
		        _fighter.move(); 
	        	checkCollision();
	        
		        //FPS count
		        getStartTime = System.currentTimeMillis();//程式開始起始時間
		        frames++;
		        if((getStartTime-lastTime)>updateTimer){
		        	FPS=((double)frames/(double)(getStartTime-lastTime))*1000;
		        	lastTime = getStartTime;
		        	frames =0;
		//        	System.out.println("FPS: "+(int)FPS);
		        }
	        }
//        System.out.println(frames ++ +" ,getStartTime: "+getStartTime);
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

    protected void gameOver(){//fighter dead clear all sprite in render objects and show GameOver
    	isgameOver = true;
    	_fighter.isfighterAlive = false;
    	main.clearSprite();
    	removeFromScene(_fighter);
    	_render_objects.clear();    	
    	
    	//show game over
    	Sprite over = new Sprite(this, "res\\gameOver.png", 320, 90);
    	over.setPosition((main.WINDOWS_WIDTH/2), (main.WINDOWS_HEIGHT/2));
    	addToScene(over);
    }
    
    
    public void checkCollision(){ 
    	//fighter bullet
    	Rectangle fighterR = _fighter.getBound();         //主機
	    	for(int i=0; i<_fighter_Missile.size();i++){     //主機子彈
	    		Rectangle fBullet = _fighter_Missile.get(i).getBound();
	    		
	    		for(int j=0; j<_enemyArray.size();j++){      //敵機
	        		Rectangle enemy= _enemyArray.get(j).getBound();
	        		if(fBullet.intersects(enemy)){           //01-子彈打到敵機要做的事.
	        			//TODO 01產生爆炸動畫      	        			
	        			
	        			//02我方子彈消失
	        			_fighter_Missile.get(i).destory();
	        			removeFromScene(_fighter_Missile.get(i));
	        			_fighter_Missile.remove(i);
//	        			System.out.println("_fighter_Missile: "+_fighter_Missile.size());
	        			//03扣血        			
	        			if(_enemyArray.get(j)._enemy_HP>0){        				
	        				_enemyArray.get(j)._enemy_HP -= 5; 
	        				System.out.println("hp: "+_enemyArray.get(j)._enemy_HP);
	        			}
	        			if(_enemyArray.get(j)._enemy_HP<1){
	        				//給分數
	        				scord += 18;
	        				
	        				//移除死機
	        				_enemyArray.get(j).destory();
	        				removeFromScene(_enemyArray.get(j));
	        				_enemyArray.remove(j);
	        			}
//	        			System.out.println("eme num: "+_enemyArray.size());
	        		}  
	        	}	
	    	}
	    	
	    	for(int j=0; j<_enemyArray.size();j++){         //敵機
	    		Rectangle enemy= _enemyArray.get(j).getBound();    		
		    		if(fighterR.intersects(enemy)){            //02-敵機撞我機
		    			//TODO 01產生爆炸動畫       
		    			
		    			//02主機扣血
		    			if(_fighter._fighter_HP >0){
		    				_fighter._fighter_HP -= 10;
//		    				System.out.println("fhp: "+_fighter._fighter_HP);
		    			}
		    			if(_fighter._fighter_HP <1){
		    				//場景畫面停止/清空
		    				for(int t=0; t<_enemyArray.size();t++){
		    					_enemyArray.get(t).destory();
		    					_enemyArray.get(j).destory();
		    				}		    				
		    				//跳出gameover
		    				gameOver();
		    			}
		    			//03敵方扣血        
		    			if(_enemyArray.get(j).alive){
			    			if(_enemyArray.get(j)._enemy_HP>0){        				
			    				_enemyArray.get(j)._enemy_HP -= 10; 
			    				System.out.println("hp: "+_enemyArray.get(j)._enemy_HP);
			    			}
			    			if(_enemyArray.get(j)._enemy_HP<1){
			    				//給分數
			    				scord += 9;
			    				
			    				//移除死機
			    				_enemyArray.get(j).destory();
			    				removeFromScene(_enemyArray.get(j));
			    				_enemyArray.remove(j);
			    			}
		    			}
//		    			System.out.println("eme num: "+_enemyArray.size());
		    		}    		
	    	}
	    	
	    	for(int k=0; k<_enemy_Millsile.size();k++){  //敵機子彈
	    		Rectangle eBullet = _enemy_Millsile.get(k).getBound();
	    		
	    		if(fighterR.intersects(eBullet)){         //03-敵機子彈打到我機
	    			//TODO 01產生爆炸動畫       
	    			
	    			//02-碰撞後子彈消失
	    			_enemy_Millsile.get(k).destory();
        			removeFromScene(_enemy_Millsile.get(k));
        			_enemy_Millsile.remove(k);
	    			
	    			//03-主機扣血
	    			if(_fighter._fighter_HP >0){
		    			_fighter._fighter_HP -= (int)(Math.random()*10+1);		    				
	    			}
	    			if(_fighter._fighter_HP <1){ //TODO 畫面殘留
	    				//敵機全部死亡	    				
	    				for(int t=0; t<_enemyArray.size();t++){
	    					_enemyArray.get(t).destory();
	    				}	
	    				//子彈清空  場景畫面停止/清空	    				
	    				for(int t=0; t<_enemy_Millsile.size();t++){//清子彈
	    					_enemy_Millsile.get(t).destory();
	    					removeFromScene(_enemy_Millsile.get(t));
	    					_enemy_Millsile.remove(t);
	    				}		    
	    				//主機死亡
	    				//跳出gameover
	    				gameOver();
	    			}
	    			
	    			
	    		}
	    	}
//	    	System.out.println("FPS: "+ FPS + "Scords: "+ scord);
    }
    
    //TODO FPS與分數(打敵機的分數計算)
    //TODO 血條
    //TODO 背景音樂
    
    
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
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		_fighter.keyPressed(e);		
		if(e.getKeyCode() ==KeyEvent.VK_SPACE)
		SpawnBullet();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		_fighter.keyReleased(e);	
	}	
	
//call fighter bullet class     
	protected void SpawnBullet(){    	
    //new bullet
		if(_fighter.isfighterAlive){
	    	_bullet fBullet = new _bullet(this, "res\\bullet.png", 12, 16);
	    	fBullet.setPosition(_fighter._x, _fighter._y);
	    	addToScene(fBullet);
	    	_fighter_Missile.add(fBullet); //array for collision
	//    	System.out.println("fBullet: "+_fighter_Missile.size());
	//    	System.out.println("begin size:"+_render_objects.size());
	    	
	    //check if bullet out of boundary
	    	Timer checkBoundary = new Timer();
	    	TimerTask bulletTask = new TimerTask() {
				@Override
				public void run() {
					if(fBullet.bullet_img != null){					
						removeFromScene(fBullet);
						_fighter_Missile.remove(fBullet);
						checkBoundary.cancel();//cancel timer when remove bullet
	//					System.out.println("after size:"+_render_objects.size());
					}
				}
			};
			checkBoundary.schedule(bulletTask, 200, 100);
	//    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
		}
    }

	protected void newEmemy(){	 
		Timer _new_enemy = new Timer();		
		TimerTask enemy2_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy2
				if(!isgameOver){
					SpawnEnemy2();	
				}
			}
		};
		TimerTask enemy3_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy3
				if(!isgameOver){
					SpawnEnemy3();
				}
			}
		};
		TimerTask enemy4_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy4
				if(!isgameOver){
					SpawnEnemy4();
				}
			}
		};
		TimerTask enemy1_Task = new TimerTask() {
			@Override
			public void run() {//生出enemy1
				if(!isgameOver){
					SpawnEnemy1();
				}
			}
		};
		
		//設定自動生出敵機的時間
		if(!isgameOver){
			_new_enemy.schedule(enemy1_Task, 1000*10, 1000*10);
			_new_enemy.schedule(enemy2_Task, 0, 1000* 5);
			_new_enemy.schedule(enemy3_Task, 1000*30, 1000*25);
			_new_enemy.schedule(enemy4_Task, 1000*20, 1000*15);
		}
	}

	private void SpawnEnemy2(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy2 = new _enemy(this, "res/enemy2.png", 65, 65);
		_enemy2.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy2);
		
		_enemy2.sendEnemy();//敵機new出來設定初始位置以後，再開始移動				
		_enemyArray.add(_enemy2); //array for collision
		
		//check if enemy out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask enemyTask = new TimerTask() {
			@Override
			public void run() {
				if(_enemy2.enemy_img != null){					
					removeFromScene(_enemy2);
					_enemyArray.remove(_enemy2);
					_enemy2.alive =false;
//					System.out.println("enemyQTY: "+_enemyArray.size());
					checkBoundary.cancel();//cancel timer when remove bullet
//					System.out.println("after size:"+_render_objects.size());
				}
			}
		};
		checkBoundary.schedule(enemyTask, 200, 100);
		
		//bullet連續射擊
		
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				if(_enemy2.alive){//敵機存活的話子彈才需要生出
					_bullet enemyBullet = new _bullet(_enemy2._scene, "res\\bullet_enemy.png", 12, 16);
					enemyBullet.setPosition(_enemy2.get_position().x, _enemy2.get_position().y);
					addToScene(enemyBullet);
					_enemy_Millsile.add(enemyBullet);			
					
					//check if bullet out of boundary
			    	Timer checkBoundary = new Timer();
			    	TimerTask bulletTask = new TimerTask() {
						@Override
						public void run() {
							if(enemyBullet.bullet_img != null){					
								removeFromScene(enemyBullet);
								_enemy_Millsile.remove(enemyBullet);
								checkBoundary.cancel();//cancel timer when remove bullet
	//							System.out.println("after size:"+_render_objects.size());
							}
						}
					};
					checkBoundary.schedule(bulletTask, 200, 100);
	//			    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				}else{
					keepShooting.cancel();
				}
			}
		};	
		keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
	}	
	
	private void SpawnEnemy3(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy3 = new _enemy(this, "res/enemy3.png", 65, 65);
		_enemy3.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy3);
		
		_enemy3.sendEnemy();//敵機new出來設定初始位置以後，再開始移動				
		_enemyArray.add(_enemy3); //array for collision
		
		//check if enemy out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask enemyTask = new TimerTask() {
			@Override
			public void run() {
				if(_enemy3.enemy_img != null){					
					removeFromScene(_enemy3);
					_enemyArray.remove(_enemy3);
					_enemy3.alive =false;
//					System.out.println("enemyQTY: "+_enemyArray.size());
					checkBoundary.cancel();//cancel timer when remove bullet
//					System.out.println("after size:"+_render_objects.size());
				}
			}
		};
		checkBoundary.schedule(enemyTask, 200, 100);
		
		//bullet連續射擊
		
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				if(_enemy3.alive){//敵機存活的話子彈才需要生出
					_bullet enemyBullet = new _bullet(_enemy3._scene, "res\\bullet_enemy.png", 12, 16);
					enemyBullet.setPosition(_enemy3.get_position().x, _enemy3.get_position().y);
					addToScene(enemyBullet);
					_enemy_Millsile.add(enemyBullet);			
					
					//check if bullet out of boundary
			    	Timer checkBoundary = new Timer();
			    	TimerTask bulletTask = new TimerTask() {
						@Override
						public void run() {
							if(enemyBullet.bullet_img != null){					
								removeFromScene(enemyBullet);
								_enemy_Millsile.remove(enemyBullet);
								checkBoundary.cancel();//cancel timer when remove bullet
	//							System.out.println("after size:"+_render_objects.size());
							}
						}
					};
					checkBoundary.schedule(bulletTask, 200, 100);
	//			    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				}else{
					keepShooting.cancel();
				}
			}
		};
		if(_enemy3.alive){
			keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
		}else{
			keepShooting.cancel();
		}
	}	
	
	private void SpawnEnemy4(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy4 = new _enemy(this, "res/enemy4.png", 65, 65);
		_enemy4.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy4);
		
		_enemy4.sendEnemy();//敵機new出來設定初始位置以後，再開始移動				
		_enemyArray.add(_enemy4); //array for collision
		
		//check if enemy out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask enemyTask = new TimerTask() {
			@Override
			public void run() {
				if(_enemy4.enemy_img != null){					
					removeFromScene(_enemy4);
					_enemyArray.remove(_enemy4);
					_enemy4.alive =false;
//					System.out.println("enemyQTY: "+_enemyArray.size());
					checkBoundary.cancel();//cancel timer when remove bullet
//					System.out.println("after size:"+_render_objects.size());
				}
			}
		};
		checkBoundary.schedule(enemyTask, 200, 100);
		
		//bullet連續射擊
		
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				if(_enemy4.alive){//敵機存活的話子彈才需要生出
					_bullet enemyBullet = new _bullet(_enemy4._scene, "res\\bullet_enemy.png", 12, 16);
					enemyBullet.setPosition(_enemy4.get_position().x, _enemy4.get_position().y);
					addToScene(enemyBullet);
					_enemy_Millsile.add(enemyBullet);			
					
					//check if bullet out of boundary
			    	Timer checkBoundary = new Timer();
			    	TimerTask bulletTask = new TimerTask() {
						@Override
						public void run() {
							if(enemyBullet.bullet_img != null){					
								removeFromScene(enemyBullet);
								_enemy_Millsile.remove(enemyBullet);
								checkBoundary.cancel();//cancel timer when remove bullet
	//							System.out.println("after size:"+_render_objects.size());
							}
						}
					};
					checkBoundary.schedule(bulletTask, 200, 100);
	//			    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				}else{
					keepShooting.cancel();
				}
			}
		};	
		if(_enemy4.alive){
			keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
		}else{
			keepShooting.cancel();
		}	}	
	
	private void SpawnEnemy1(){
		int randomShooting = 1000 * ((int)(Math.random()*10+1));
		_enemy _enemy1 = new _enemy(this, "res/enemy1.png", 65, 65);
		_enemy1.setPosition((int)(Math.random()*(main.WINDOWS_WIDTH-80)+80), 40);
		addToScene(_enemy1);
		
		_enemy1.sendEnemy();//敵機new出來設定初始位置以後，再開始移動				
		_enemyArray.add(_enemy1); //array for collision
		
		//check if enemy out of boundary
    	Timer checkBoundary = new Timer();
    	TimerTask enemyTask = new TimerTask() {
			@Override
			public void run() {
				if(_enemy1.enemy_img != null){					
					removeFromScene(_enemy1);
					_enemyArray.remove(_enemy1);
					_enemy1.alive =false;
//					System.out.println("enemyQTY: "+_enemyArray.size());
					checkBoundary.cancel();//cancel timer when remove bullet
//					System.out.println("after size:"+_render_objects.size());
				}
			}
		};
		checkBoundary.schedule(enemyTask, 200, 100);
		
		//bullet連續射擊
		
		Timer keepShooting = new Timer();
		TimerTask eneShooting = new TimerTask() {			
			@Override
			public void run() {
				if(_enemy1.alive){//敵機存活的話子彈才需要生出
					_bullet enemyBullet = new _bullet(_enemy1._scene, "res\\bullet_enemy.png", 12, 16);
					enemyBullet.setPosition(_enemy1.get_position().x, _enemy1.get_position().y);
					addToScene(enemyBullet);
					_enemy_Millsile.add(enemyBullet);			
					
					//check if bullet out of boundary
			    	Timer checkBoundary = new Timer();
			    	TimerTask bulletTask = new TimerTask() {
						@Override
						public void run() {
							if(enemyBullet.bullet_img != null){					
								removeFromScene(enemyBullet);
								_enemy_Millsile.remove(enemyBullet);
								checkBoundary.cancel();//cancel timer when remove bullet
	//							System.out.println("after size:"+_render_objects.size());
							}
						}
					};
					checkBoundary.schedule(bulletTask, 200, 100);
	//			    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
				}else{
					keepShooting.cancel();
				}
			}
		};	
		if(_enemy1.alive){
			keepShooting.schedule(eneShooting, 200, randomShooting);	//敵機連續射擊頻率
		}else{
			keepShooting.cancel();
		}	
	}
}