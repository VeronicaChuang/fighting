package practice;

public class rect {
	int x, y;
	boolean right =false;
	
	public rect(int startX, int startY) {
		x = startX;
		y = startY;
	}
	
	public void move(){
		if(x==700)
			right = true;
		if(x==0)
			right =false;
		if(right)
			x--;
		else
			x++;
	}

}
