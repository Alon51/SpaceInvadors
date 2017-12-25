import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
   
public class Obstacle extends JPanel {
   
   private Image display, burning_bricks;
 
   boolean isAlive;
   int x, y, lives;

   public Obstacle(Image image, int x, int y, int lives) {
   
      //The default Image for the player, or a new image:
      display = image == null ?  new ImageIcon(this.getClass().getResource("BS.gif")).getImage() : image;
      burning_bricks = new ImageIcon(this.getClass().getResource("fire.gif")).getImage();
      this.x = x;
      this.y = y;
      this.lives = lives;
      isAlive = true;
   }

   public void drawObstacles(Graphics g) {
   	
      if (isAlive){
         g.drawImage(display,        x, y, 95, 55, null); //55, 30
         //g.drawImage(burning_bricks, x, 475, 55, 30, null); 
      }
   }
}