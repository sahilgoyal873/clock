import java.awt.Canvas;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author SAHIL
 */
public class clock extends Hands {

    public static void main(String[] s) {
        clock c = new clock();
        Container contentPane = c.f.getContentPane();
        contentPane.add(c);
        Second sec = new Second(c,c, "second hand");
    }
}

class ClockDesign extends Canvas implements ActionListener {

    JFrame f;
    Point centre;
    int radius;

    ClockDesign() {
        f = new JFrame("clock");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setBounds(450, 50, 450, 450);
        f.setVisible(true);

        centre = new Point();
        centre.x = 200;
        centre.y = 200;

        radius = 150;


    }

    @Override
    public void paint(Graphics g) {

        //g.drawOval(centre.x - radius-5, centre.y - radius-5, 2 * radius+10, 2 * radius+10);
        drawStars(centre.x, centre.y, radius-1, g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void drawStars(int x, int y, int radius, Graphics g) {
        // outline for clock
        Point star = new Point();
        double angle = 0;
        String[] s = new String[]{"3","4","5","6","7","8","9","10","11","12","1","2"};
        int j =0;
        for (int i = 0; i < 60; i++) {
            star.x = (int) (x + radius * (Math.cos(Math.toRadians(angle))));
            star.y = (int) (y + radius * (Math.sin(Math.toRadians(angle))));


            
            if(angle%30==0)
             g.drawString(s[j++], star.x, star.y);
            else
             g.drawString(".", star.x, star.y);
            
            angle += 6;

        }
    }
}

class Hands extends ClockDesign {

    Point sec_end;
    double sec_angle;
    
    Point min_end;
    double min_angle;
    
    Point hour_end;
    double hour_angle;
    
    Hands() {
        // intialize to current time
        System.out.println("Start time = "+new Date().toString());
        
        sec_end = new Point();
        sec_end.x = centre.x + radius;
        sec_end.y = centre.y;
        if(new Date().getSeconds()>=15)
        sec_angle = (double)((new Date().getSeconds()-15)*6);
        else
        sec_angle = (double)(360-(new Date().getSeconds())*6);
       
        System.out.println(sec_angle);
        
        min_end = new Point();
        min_end.x = centre.x + radius-20;
        min_end.y = centre.y;
        if(new Date().getMinutes()>=15)
        min_angle = (double)((new Date().getMinutes()-15)*6);
        else
        min_angle = (double)(360-(new Date().getMinutes())*6);
        
        System.out.println(min_angle);
        
        hour_end = new Point();
        hour_end.x = centre.x + radius-20;
        hour_end.y = centre.y;
        int hour;
        if(new Date().getHours()>12)
            hour=(new Date().getHours())-12;
        else
            hour=new Date().getHours();
         if(hour<3){   
          hour_angle = (double)(((12-hour)*30)+(new Date().getMinutes()/2));
          if(hour==0)
              hour_angle=270;
         }else
        hour_angle = (double)(((hour-3)*30)+(new Date().getMinutes()/2));
         
         System.out.println(hour);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(centre.x, centre.y, sec_end.x, sec_end.y);
        g.drawLine(centre.x, centre.y, min_end.x, min_end.y);
        g.drawLine(centre.x, centre.y, hour_end.x, hour_end.y);
        
//        System.out.println(sec_angle);
//        System.out.println(min_angle);
//        System.out.println(hour_angle);
        
        
    }

    public void secondadvance() {
        sec_end.x = (int) (centre.x + radius * (Math.cos(Math.toRadians(sec_angle))));
        sec_end.y = (int) (centre.x + radius * (Math.sin(Math.toRadians(sec_angle))));
        if (sec_angle < 360) {
            sec_angle += 6;
        } else {
            sec_angle = 0;
        }


    }
    
     public void minuteadvance() {
        min_end.x = (int) (centre.x + (radius-20) * (Math.cos(Math.toRadians(min_angle))));
        min_end.y = (int) (centre.x + (radius-20) * (Math.sin(Math.toRadians(min_angle))));
        if (min_angle < 360) {
            min_angle += 0.1;
        } else {
            min_angle = 0;
        }


    }
     
     public void houradvance() {
        hour_end.x = (int) (centre.x + (radius-40) * (Math.cos(Math.toRadians(hour_angle))));
        hour_end.y = (int) (centre.x + (radius-40) * (Math.sin(Math.toRadians(hour_angle))));
        if (hour_angle < 360) {
            hour_angle += (double)(0.1/12);
        } else {
            hour_angle = 0;
        }


    }
}

class Second extends Thread {

    ClockDesign c;
    Hands h;

    Second(ClockDesign c,Hands h, String s) {
        super(s);
        this.c = c;
        this.h=h;
        start();
    }

    @Override
    public void run() {
        c.repaint();
        while (true) {
            try {
                Thread.sleep(1000);
                h.secondadvance();  
                h.minuteadvance();
                h.houradvance();
                h.repaint();
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }

        }
    }
}
