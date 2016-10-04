package index;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.sql.Time;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.omg.stub.java.rmi._Remote_Stub;

import index.Animation.StatusListener;

/**
 * Created by Matt on 2016/8/8.
 */
public class MainScene implements KeyListener{
	public static final int Velocity_Fighter = 4;
    public static final int Velocity_Bullet = 7;
    public static final int Velocity_Bullet_Enemy = 3;
    public static final int Velocity_Enemy = 1;
    
    private static final int BulletHitEnemy = 5; //主機子彈傷害力
    private static final int craftbump = 10; //飛機互撞扣血量
    
    private List<RenderLayer> _render_objects = new CopyOnWriteArrayList<>();
    private Insets _rect = null;
    public Insets get_rect(){return _rect;}

    private Fighter _fighter;
    private Sprite _sprite_bg1, _sprite_bg2;    
    protected int ny2= 750;  
    
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
	ArrayList<_bullet> _fighter_Missile;
	ArrayList<_enemy> _enemyArray;
	ArrayList<_bullet> _enemy_Millsile;
	_Explosion _explosion = new _Explosion(this, null, "res\\explosion.png", 100, 100, 15);
	
	//hpbar
	protected Sprite hpBarBG = new Sprite(this,"res\\hp_bg.png",100,5);
	protected Sprite hpBarLive = new Sprite(this,"res\\hp_value.png", 100, 5);
	
	//bullet time check
	double old=0;
	double pass = 500;
	
	//show Scores
	private _showText showScord;
	private _showFPS giveFPS;
	
	//music
	music playMusic = new music();
	int i =0;
	
	
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
     
        _fighter = new Fighter(this, "res\\fighter.png", 88, 58, 3);
        SpawnFighter();
        addToScene(_fighter);
        
        newEmemy();    
        
    	addToScene(hpBarBG);
    	addToScene(hpBarLive);
    	
    	//new scord text
    	showScord = new _showText(this, 150, 100); //int width, int height
    	showScord.setPosition(80, 30);
    	showScord.setScore("0");//呼叫setString才會用graphic畫出
    	addToScene(showScord);
    	
    	//show fps
    	
    	giveFPS = new _showFPS(this, 150, 100); //int width, int height
    	giveFPS.setPosition(80, 55);
    	giveFPS.setFPS("0");//呼叫setString才會將問劑用graphic畫出
    	addToScene(giveFPS);
    	    	
    }     
     
    //重置飛機位置
    private void SpawnFighter(){
        _fighter.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT - 65);
      
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
	    fighterHPBar();//秀血條
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
		        	int fpsInt = (int)FPS;
		        	String f = String.valueOf(fpsInt);
		        	
		        	giveFPS.setFPS(f);
		        	System.out.println("FPS: "+(int)FPS);
		        }
	        }
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

  //if fighter dead clear all sprite in render objects and show GameOver
    protected void gameOver(){
    	isgameOver = true;
    	_fighter.isfighterAlive = false;
    	main.clearSprite();
    	removeFromScene(_fighter);
    	_render_objects.clear();    	
    	
    	playMusic.menuMusic();
    	playMusic.bgPlay();
    	
    	//show game over
    	Sprite over = new Sprite(this, "res\\gameOver.png", 320, 90);
    	over.setPosition((main.WINDOWS_WIDTH/2), (main.WINDOWS_HEIGHT/2));
    	addToScene(over);
    }
        
    protected void checkCollision(){ 
    	//fighter bullet
    	Rectangle fighterR = _fighter.getBound();            //主機rect
	    	for(int i=0; i<_fighter_Missile.size();i++){     //主機子彈
	    		Rectangle fBullet = _fighter_Missile.get(i).getBound();
	    			    		
	    		for(int j=0; j<_enemyArray.size();j++){      //敵機
	        		Rectangle enemy= _enemyArray.get(j).getBound();
	        		if(fBullet.intersects(enemy)){           //01-子彈打到敵機要做的事.
	        			//01產生爆炸動畫      	 
	        			_explosion.setPosition(_fighter_Missile.get(i)._x, _fighter_Missile.get(i)._y);
	        	        addToScene(_explosion);
	        			StatusListener listener = new StatusListener() {		
	        				@Override
	        				public void onCompleted(Animation animation) {
	        					removeFromScene(_explosion);	
	        				}
	        			};
	        			_explosion._listener = listener; 
	        			
	        			//01-2 音效
	        			playMusic.explosionMusic();
	        	        
	        			//02我方子彈消失
	        			_fighter_Missile.get(i).destory();
	        			removeFromScene(_fighter_Missile.get(i));
	        			_fighter_Missile.remove(i);
//	        			System.out.println("_fighter_Missile: "+_fighter_Missile.size());
	        			
	        			//03扣血        			
	        			if(_enemyArray.get(j)._enemy_HP>0){        				
	        				_enemyArray.get(j)._enemy_HP -= BulletHitEnemy; 
//	        				System.out.println("hp: "+_enemyArray.get(j)._enemy_HP);
	        			}
	        			if(_enemyArray.get(j)._enemy_HP<1){
	        				//給分數
	        				scord += 18;
	        				String s = Integer.toString(scord);
	        				showScord.setScore(s);//show scord
	        				
	        				
	        				//移除死機
	        				_enemyArray.get(j).destory();
	        				removeFromScene(_enemyArray.get(j));
	        				_enemyArray.remove(j);
	        				
	        				//音效
		        			playMusic.enemyCrashMusic();
	        			}
//	        			System.out.println("eme num: "+_enemyArray.size());
	        		}  
	        	}	
	    	}
	    	
	    	for(int j=0; j<_enemyArray.size();j++){            //敵機
	    		Rectangle enemy= _enemyArray.get(j).getBound();    		
		    		if(fighterR.intersects(enemy)){            //02-敵機撞我機
		    			//01產生爆炸動畫     	 
	        			_explosion.setPosition(_fighter._x, _fighter._y);
	        	        addToScene(_explosion);
	        			StatusListener listener = new StatusListener() {		
	        				@Override
	        				public void onCompleted(Animation animation) {
	        					removeFromScene(_explosion);	
	        				}
	        			};
	        			_explosion._listener = listener;	        			
	        	
	        			//01-2 音效
	        			playMusic.explosionMusic();
	        			
		    			//02主機扣血
		    			if(_fighter._fighter_HP >0){
		    				_fighter._fighter_HP -= craftbump; //主機被敵機撞扣血量
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
			    				_enemyArray.get(j)._enemy_HP -= craftbump;  //敵機被主機撞扣血量
//			    				System.out.println("hp: "+_enemyArray.get(j)._enemy_HP);
			    			}
			    			if(_enemyArray.get(j)._enemy_HP<1){
			    				//給分數
			    				scord += 9;
			    				String s = Integer.toString(scord);
		        				showScord.setScore(s);//show scord
			    				//移除死機
			    				_enemyArray.get(j).destory();
			    				removeFromScene(_enemyArray.get(j));
			    				_enemyArray.remove(j);
			    				
			    				//音效
			        			playMusic.enemyCrashMusic();
			    			}
		    			}
//		    			System.out.println("eme num: "+_enemyArray.size());
		    		}    		
	    	}
	    	
	    	for(int k=0; k<_enemy_Millsile.size();k++){  //敵機子彈
	    		Rectangle eBullet = _enemy_Millsile.get(k).getBound();
	    		
	    		if(fighterR.intersects(eBullet)){         //03-敵機子彈打到我機
	    			//01產生爆炸動畫       
	    			_explosion.setPosition(_enemy_Millsile.get(k)._x, _enemy_Millsile.get(k)._y);
        	        addToScene(_explosion);
        			StatusListener listener = new StatusListener() {		
        				@Override
        				public void onCompleted(Animation animation) {
        					removeFromScene(_explosion);	
        				}
        			};
        			_explosion._listener = listener; 
        			
        			//01-2 音效
        			playMusic.explosionMusic();
        			
	    			//02-碰撞後子彈消失
	    			_enemy_Millsile.get(k).destory();
        			removeFromScene(_enemy_Millsile.get(k));
        			_enemy_Millsile.remove(k);
	    			
	    			//03-主機扣血
	    			if(_fighter._fighter_HP >0){
		    			_fighter._fighter_HP -= (int)(Math.random()*10+1);	//敵機子彈傷害力	    				
	    			}
	    			if(_fighter._fighter_HP <1){ 
	    				//敵機全部拿掉	    				
	    				for(int t=0; t<_enemyArray.size();t++){
	    					_enemyArray.get(t).destory();
	    				}	
	    				//子彈清空  場景畫面停止/清空	    				
	    				for(int t=0; t<_enemy_Millsile.size();t++){
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
    
    
//血條
    protected void fighterHPBar(){    			
    	hpBarBG.setPosition(_fighter._x, _fighter._y+main.HP_Position);    	
    	hpBarLive.setPosition(_fighter._x, _fighter._y+main.HP_Position);
    	
    	//resize according with fighter hp
    	hpBarLive.setWidth(_fighter._fighter_HP); 
    	
    	//fix green bar position on the left
    	if(_fighter._fighter_HP!=100){
    		hpBarLive.setPosition((hpBarBG._x-((100-_fighter._fighter_HP)/2+1)),_fighter._y+30);
    	}
    	
    	if(hpBarLive.get_width()<=0){// if fighter dead, remove hp bars
    		removeFromScene(hpBarBG);
    		removeFromScene(hpBarLive);
    	}
    }

      
    
//Scrolling bg1 and bg2
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
			double a1 = System.currentTimeMillis();
				_bullet fBullet = new _bullet(this, "res\\bullet.png", 12, 16);
		    	fBullet.setPosition(_fighter._x, _fighter._y);
		    	addToScene(fBullet);
		    	pass = a1-old;
		    	old =a1;//現在時間給old
//		    	System.out.println("pass-02 "+pass);//距離上次發射時間
		    	
		    	_fighter_Missile.add(fBullet); //array for collision
		    	
		    	//01-2 音效
    			playMusic.fighterFireMusic();
		    	
		    //check if bullet out of boundary
		    	Timer checkBoundary = new Timer();
		    	TimerTask bulletTask = new TimerTask() {
					@Override
					public void run() {
						if(fBullet.bullet_img != null){					
							removeFromScene(fBullet);
							_fighter_Missile.remove(fBullet);
							checkBoundary.cancel();      //cancel timer when bullet removed
		//					System.out.println("after size:"+_render_objects.size());
						}
					}
				};
				checkBoundary.schedule(bulletTask, 200, 100);
		//    	System.out.println("mainScene hashcode: "+fBullet.hashCode());
//			}else{
//				pass =500;
//		}
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
			_new_enemy.schedule(enemy1_Task, 1000*10, 1000*8);
			_new_enemy.schedule(enemy2_Task, 0, 1000* 3);
			_new_enemy.schedule(enemy3_Task, 1000*30, 1000*20);
			_new_enemy.schedule(enemy4_Task, 1000*20, 1000*11);
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