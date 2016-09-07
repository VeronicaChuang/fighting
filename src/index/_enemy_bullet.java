package index;

import java.awt.image.BufferedImage;

public class _enemy_bullet extends Sprite {
	private int newX =0;
	private int newY =0;
	
	
	public _enemy_bullet(MainScene scene, String img_path, int width, int height) {
		super(scene, img_path, width, height);
	}

	public _enemy_bullet(MainScene scene, BufferedImage img, int width, int height) {
		super(scene, img, width, height);
	}
	
	protected void move(){
		newX = this.get_position().x;//取得enemy的位置
		newY = this.get_position().y;
		
		////set new bullet position after fire with timer		
		this.setPosition(newX, (newY -= MainScene.Velocity_Bullet));
	}

}
