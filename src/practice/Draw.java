package practice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Draw {
	public static void main(String[] args) {
		JFrame frame = new JFrame("collision");
		frame.setVisible(true);
		frame.setSize(800, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Paint());
	}
}
class Paint extends JPanel implements ActionListener{
		rect rect1;
		rect rect2;
		Timer time;
		
		public Paint(){
			rect1 = new rect(0,75);
			rect2 = new rect(700, 75);
			time = new Timer();
		}
		
		public void actionPerformed(ActionEvent e) {
			rect1.move();
			rect2.move();
			repaint();
		}
		public void paint(Graphics g){
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.black);
			g2d.fillRect(rect1.x, rect2.y, 100, 50);
			g2d.setColor(Color.red);
			g2d.fillRect(rect2.x, rect2.y, 100, 50);
		}
		
}
	


