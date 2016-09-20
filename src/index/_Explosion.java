package index;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class _Explosion extends Animation {	

	public _Explosion(MainScene scene, StatusListener listener, String img_path, int width, int height,
			int frame_count) {
		super(scene, listener, img_path, width, height, frame_count);
		
	}	
	
	@Override
	protected BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height/3);
		return dest;
	}

}
