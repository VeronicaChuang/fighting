package practice;

import java.awt.*;
import javax.swing.JApplet;

public class appletP extends JApplet implements Runnable {
    private int imageWidth;
    private int x1, x2;
    private Image scrollImage, offScreen;

    public void init() {
       //取得捲動畫面影像
        MediaTracker mediaTracker = new MediaTracker(this);
        scrollImage  = getImage(getDocumentBase(),"scroll.gif");
        mediaTracker.addImage(scrollImage,0);

        try {
            mediaTracker.waitForAll();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        offScreen = createImage(getWidth(), getHeight());

        imageWidth = scrollImage.getWidth(this);

        x1 = 0;
        x2 = -imageWidth;
    }

    public void start() {
       (new Thread(this)).start();
    }

    public void paint(Graphics g) {
        g.drawImage(offScreen, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void run() {
        Graphics gOffScreen = offScreen.getGraphics();

        while(true) {
            gOffScreen.drawImage(scrollImage, x1, 0, this);
            gOffScreen.drawImage(scrollImage, x2, 0, this);

            repaint();

            try {
                Thread.sleep(20);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            //更新影像位置(產生捲動效果)
            if(x1 == getWidth()) {
                x1 = -imageWidth;
            }
            else {
                x1++;
            }

            if(x2 == getWidth()) {
                x2 = -imageWidth;
            }
            else {
                x2++;
            }
        }
    }
}