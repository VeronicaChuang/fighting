import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class scannerT {

	public static void main(String[] args) {
		try{
			int x = System.in.read();
			System.out.println(x);
		}catch(Exception a){
			System.out.println("xx");
		}
		
//		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("type:");
//		String text;
//		try {
//			text = buf.readLine();
//			System.out.println("輸入內容: "+text);
//			buf.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		Scanner scan = new Scanner(System.in);
//		String a = scan.nextLine();
//		System.out.println(a);

	}

}
