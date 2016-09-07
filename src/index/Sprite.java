package index;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Matt on 2016/8/8.
 */
public class Sprite {
    private int _width = 0;
    private int _height = 0;

    protected BufferedImage _img;
    protected int _x =0;
    protected int _y =0;
    protected MainScene _scene = null;

    public BufferedImage getImg(){return _img;}
    public int get_width(){return _width;}
    public int get_height(){return _height;}
    public int get_x(){return _x;}
    public int get_y(){return _y;}
    //設定元件與網格邊界之距離, 使用 Insets(int top, int left, int bottom, int right)
//    public Insets get_boundary(){return new Insets(_x-(_width/2), _y-(_height/2), _x+(_width/2), _y+(_height/2));}
    public Insets get_boundary(){return new Insets(_y-(_height/2),_x-(_width/2), _y+(_height/2), _x+(_width/2));}

    //point 放座標(x,y)
    public Point get_position(){return new Point(_x, _y);}

    public Sprite(MainScene scene, String img_path, int width, int height){
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(img_path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initial(scene, img, width, height);
    }

    public Sprite(MainScene scene, BufferedImage img, int width, int height){
        initial(scene, img, width, height);
    }

    private void initial(MainScene scene, BufferedImage image, int width, int height){
        _scene = scene;
        _img = image;

        _width = width;
        _height = height;
    }

    public void setPosition(int x, int y){
        _x = x;
        _y = y;
    }
}
