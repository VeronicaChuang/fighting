package index;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by matt1201 on 2016/8/8.
 */
public class Animation extends Sprite{
    public interface StatusListener{
        void onCompleted(Animation animation);
    }

    protected int _frame_index = 0;
    protected int _frame_count;
    protected StatusListener _listener = null;
    protected java.util.List<Sprite> _frames = new LinkedList<>();

    @Override
    public BufferedImage getImg() {
        Sprite result = _frames.get(_frame_index);
        _frame_index++;

        if(_frame_index>=_frames.size()-1) {
            _frame_index = 0;
            if (_listener != null)
                _listener.onCompleted(this);
        }

        return result.getImg();
    }

    public Animation(MainScene scene, StatusListener listener, String img_path, int width, int height, int frame_count){
        super(scene, img_path, width, height);

        _listener = listener;
        _frame_count = frame_count;

        try {
            BufferedImage stream = ImageIO.read(new File(img_path));
            int frame_width = stream.getWidth() / frame_count;
            int frame_height = stream.getHeight();
//            System.out.println("pic width: "+stream.getWidth()+" frame_width: "+frame_width+" frame_height: "+frame_height);

            int left =0;
            int top = 0;
            for(int i=0; i<frame_count; i++) {//把連圖剪成一個一個圖
                BufferedImage img_frame = cropImage(stream, new Rectangle(left, top, frame_width, frame_height));
                left+=frame_width;
                _frames.add(new Sprite(scene, img_frame, width, height));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected BufferedImage cropImage(BufferedImage src, Rectangle rect) {//取得裁切成正方形後的圖
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }
}
