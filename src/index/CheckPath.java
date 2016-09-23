package index;

import java.io.File;

public class CheckPath {
	public static void main(String[] args) {
		String filename = "bgMusic.mp3";
		File file = new File(filename);
		System.out.println(file.getParent());   //C:/test/innertest
		System.out.println(file.getName());    //test.text
		System.out.println(file.exists());
	}

}
