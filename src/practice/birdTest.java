package practice;
import java.io.*;

class bird {                                                                    // 鳥
        int x,y;                                                                 // 鳥位置

        void exploding() {
                System.out.println("在(" + x + "," + y +")的鳥被摧毀" );
        }
}

class bullet {                                                          // 子彈
        int x,y;                                                         // 子彈位置
        int stepX,stepY;                                              // 子彈移動速度(向量)

        void move() {                                                 // 依移動速度到下一位置
                x += stepX;
                y += stepY;
                System.out.println("子彈移到(" + x + "," + y + ")");
        }
}

class hunter {                                                         // 獵人
        int x,y;                                                         // 獵人位置

        bullet  fire(int stepX,int stepY) {                    // 產生一顆子彈
                bullet  c = new bullet();
                c.stepX = stepX;
                c.stepY = stepY;
                c.x = x;
                c.y = y;

                return c;
        }
}

class Battlefield {                                // 戰場
        int width,height;                          // 戰場大小
        bird i;                                                          // 在戰場中放上鳥 i
        hunter  f;                                                      // 在戰場中放上獵人 f
        bullet  c;                                                       // 在戰場中放上子彈 c

        boolean hitbird(bullet c) {                                  // 是否擊中鳥
                if((c.x == i.x) && (c.y == i.y)) {                  // 若擊中
                        i.exploding();                                 // 鳥爆炸
                        i = null;                                         // 清除鳥 i 
                return true;                                             // 傳回擊中了
                }
                return false;                                                      //否則 傳回沒有擊中
        }

        boolean outOfField(bullet c) {
                if(c.x < 0 || c.y < 0)                         // 超過左邊界或上邊界
                        return true;
                if(c.x >= width || c.y >= height)         // 超過右邊界或下邊界
                        return true;
        return false;
        }
}

public class birdTest {

        public static void main(String[] argv) throws IOException{
                Battlefield b = new Battlefield();                                 // 產生戰場
                b.width = 5;                                                 // 設為5X5的大小
                b.height = 5;


                BufferedReader br =  new BufferedReader(new InputStreamReader(System.in));
                                                // 用來讀取輸入資料


                // 產生鳥
                b.i = new bird();

                // 取得使用者輸入的鳥位置
                System.out.println("請輸入鳥的位置：");
                System.out.print("x->");
                b.i.x = java.lang.Integer.parseInt(br.readLine());
                System.out.print("y->");
                b.i.y = java.lang.Integer.parseInt(br.readLine());

                // 產生獵人
                b.f = new hunter();

                // 取得使用者輸入的獵人位置
                System.out.println("請輸入獵人的位置：");
                System.out.print("x->");
                b.f.x = java.lang.Integer.parseInt(br.readLine());
                System.out.print("y->");
                b.f.y = java.lang.Integer.parseInt(br.readLine());

                // 取得使用者輸入的子彈移動速度
                System.out.println("請輸入子彈的移動速度：");
                System.out.print("x->");
                int x = java.lang.Integer.parseInt(br.readLine());
                System.out.print("y->");
                int y = java.lang.Integer.parseInt(br.readLine());

                // 發射子彈
                b.c = b.f.fire(x,y);

                do {                                // 移動子彈，並測試是否越過邊界
                        b.c.move();
                        if(b.outOfField(b.c) || b.hitbird(b.c))
                        b.c = null;                                         // 清除子彈
                } while (b.c != null);
                System.out.println("遊戲結束！");
        }
}