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
    private int fighterX = main.WINDOWS_WIDTH / 2;
    private int fighterY = main.WINDOWS_HEIGHT - 60;
    
    private int mainW = main.WINDOWS_WIDTH;
    private int mainH = main.WINDOWS_HEIGHT;
    
    public MainScene() {
        _rect = new Insets(0, 0, main.WINDOWS_HEIGHT, main.WINDOWS_WIDTH);

        Sprite _sprite_bg = new Sprite(this, "res\\bg.png", main.WINDOWS_WIDTH, main.WINDOWS_HEIGHT);
        _sprite_bg.setPosition(main.WINDOWS_WIDTH / 2, main.WINDOWS_HEIGHT / 2);
        addToScene(_sprite_bg);

        int left = 80;
//        Sprite _enemy1 = new Sprite(this, "res/enemy1.png", 80, 80);
//        _enemy1.setPosition(left, main.WINDOWS_HEIGHT / 2);
//        addToScene(_enemy1);
//        left+=80;
//
//        Sprite _enemy2 = new Sprite(this, "res\\enemy2.png", 80, 80);
//        _enemy2.setPosition(left, main.WINDOWS_HEIGHT / 2);
//        addToScene(_enemy2);
//        left+=80;
//
//        Sprite _enemy3 = new Sprite(this, "res\\enemy3.png", 80, 80);
//        _enemy3.setPosition(left, main.WINDOWS_HEIGHT / 2);
//        addToScene(_enemy3);
//        left+=80;
//
//        Sprite _enemy4 = new Sprite(this, "res\\enemy4.png", 80, 80);
//        _enemy4.setPosition(left, main.WINDOWS_HEIGHT / 2);
//        addToScene(_enemy4);
//        left+=80;
//
//        Sprite _bullet = new Sprite(this, "res\\bullet.png", 16, 20);
//        _bullet.setPosition(left, main.WINDOWS_HEIGHT / 2);
//        addToScene(_bullet);
//     
        _fighter = new Fighter(this, "res\\fighter.png", 90, 60, 3);
        SpawnFighter();
        addToScene(_fighter);
   
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


	@Override
	public void keyPressed(KeyEvent e) {
		//if figher.x>0 then move
		
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT://left
//			System.out.println("left");
			_fighter.setPosition(_fighter.get_position().x - Velocity_Fighter, _fighter.get_position().y);
			break;
		case KeyEvent.VK_UP://up
			_fighter.setPosition(_fighter.get_position().x, _fighter.get_position().y - Velocity_Fighter);
			break;
		case KeyEvent.VK_RIGHT://right
			_fighter.setPosition(_fighter.get_position().x +Velocity_Fighter, _fighter.get_position().y);
			break;
		case KeyEvent.VK_DOWN://down
			_fighter.setPosition(_fighter.get_position().x, _fighter.get_position().y + Velocity_Fighter);
			break;
		case KeyEvent.VK_SPACE://space
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		
		
		
	}


	@Override
	public void keyTyped(KeyEvent e) {}
}
