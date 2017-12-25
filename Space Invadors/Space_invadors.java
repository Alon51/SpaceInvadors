/**
 * © All rights reserved for Alon Butbul, a copy and/or implementing
 * of that class or any other class in this project without a written 
 * consent from Alon Butbul, will results in a serious issue and will 
 * lead to further actions in court. 
 * 
 * @author Alon Butbul 
 * @version 1.5
 * 
 * The Space_invadors class is used to create a game in which the
 * user will be able to interact with the program using the java 
 * GUI interface.
 * 
 * That particular class extending and implementing other classes 
 * such as JFrame to construct a full functioning interactive this.
 * */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.*;
import java.awt.CardLayout;
import java.awt.BorderLayout;

import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Space_invadors extends JFrame{

	// The background sound for the game:
   private static URL mainURL = Space_invadors.class.getResource("backSound.wav");
   private static AudioClip mainClip = Applet.newAudioClip(mainURL);

   private JPanel panelContainer = new JPanel(); 
   private Game gameObj = new Game(null, null, null); 
   private StartMenus sm = new StartMenus();
   private CardLayout cardForPanels = new CardLayout();
   
   /**
   * Constractor of the main class
   */
   public Space_invadors(){
   
      super("The game");
   
     //Adjusting the window:
      frameAdjust();
      
      panelContainer.setLayout(cardForPanels);  
      //When you add panels to the container it will display the 
      //first panel depends on which one you added first.
      panelContainer.add(sm,      "1");
      panelContainer.add(gameObj, "2");
      //cardForPanels.show(panelContainer, "1"); <-- No need, it will automatically happen.
   }

   /**
   * frameAdjust is a method that will set the window to its initial size. That
   * size is fixed and will not be changes through the game.
   */
   private void frameAdjust(){
   
      add(panelContainer);
      setTitle("Space Invaders");
      this.pack();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800, 800);
      setResizable(false);
      setLocationRelativeTo(null);
   }
 
 /**
 * The runGameLoop contain a thread to excute the gameRun method which contain 
 * the loop of the game.
 * 
 * @param playerImage    - a varible to let the players choose their player image 
 * @param enemyImage	    - a varible to let the players choose their enemy image
 * @param obstaclesImage - a varible to let the players choose their obstacle image
 * */
   public void runGameLoop(ImageIcon playerImage, ImageIcon enemyImage, ImageIcon obstaclesImage){ 
   
      Thread loop = 
         new Thread(){
            public void run(){
               gameObj.gameRun(playerImage, enemyImage, obstaclesImage);
            }
         };
         
      loop.start();
   }
   
 /**
 The introduction windows for the user to choose his spaceship
 */
   private class StartMenus extends JPanel implements ActionListener{
         
      private URL[] players = { Space_invadors.class.getResource("menuePlayer1.png"),
                  		        Space_invadors.class.getResource("menuePlayer2.png"),
                  		        Space_invadors.class.getResource("menuePlayer3.png"),
                  		        Space_invadors.class.getResource("menuePlayer4.png") };
                        
      private JButton[] playerButtons = {new JButton("Blue Knight", new ImageIcon(players[0])),
                           		        new JButton("Terminator",  new ImageIcon(players[1])),
                           		        new JButton("Red Eagle",   new ImageIcon(players[2])),
                           		        new JButton("Phantom",     new ImageIcon(players[3])), 
                                         new JButton("DEFAULT")};
                                         
      public StartMenus() {
         
      	// Menu one - Introduction:
         JLabel welcome = new JLabel("Hello player and wellcome! \n" + 
                                     "Please choose your desired spaceship from the following menue!");
         welcome.setFont(new Font("Arial", Font.BOLD, 18));
         JOptionPane.showMessageDialog(null, welcome, "WELCOME", JOptionPane.WARNING_MESSAGE);
         
         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
         add(Box.createRigidArea(new Dimension(0,70)));
                   
      	// Setting the color of each button:
         for (JButton x : playerButtons) {
         
            x.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
         
            if (x == playerButtons[0])
               playerButtons[0].setForeground(Color.BLUE);
            else if (x == playerButtons[1])
               playerButtons[1].setForeground(Color.YELLOW);
            else if (x == playerButtons[2])
               playerButtons[2].setForeground(Color.RED);
            else if (x == playerButtons[3])
               playerButtons[3].setForeground(Color.WHITE);
            else
               playerButtons[4].setForeground(Color.BLACK);
            
            x.setContentAreaFilled(false);
                 
            // Assigning actionListener to each buttom:
            x.addActionListener(this);
            
            //Align the buttons in the center:
            x.setAlignmentX(CENTER_ALIGNMENT);
             
            //Add the buttons to the panel:
            add(x, BorderLayout.CENTER);
         }
      
         //Keeping the buttons together:
         //add(Box.createVerticalGlue());
      }
      
      @Override
      protected void paintComponent(Graphics g) {
      
         super.paintComponent(g);
         g.drawImage(new ImageIcon(getClass().getResource("BackGray.jpg")).getImage(), 0, 0, getWidth(), getHeight(), this);
      }
      
      public void actionPerformed(ActionEvent e){
      
         cardForPanels.show(panelContainer, "2");
         
         if (e.getActionCommand() == "Blue Knight")
            runGameLoop(new ImageIcon(players[0]), null, null);
         else if (e.getActionCommand() == "Terminator")
            runGameLoop(new ImageIcon(players[1]), null, null);
         else if (e.getActionCommand() == "Red Eagle")
            runGameLoop(new ImageIcon(players[2]), null, null);
         else if (e.getActionCommand() == "Phantom")
            runGameLoop(new ImageIcon(players[3]), null, null);
         else//Default
            runGameLoop(null, null, null);
      }
   }

   private class Game extends JPanel implements KeyListener {
   
      private Image background; 
      private ImageIcon deadMeat = new ImageIcon(this.getClass().getResource("Explosion.png")); // Add the explosion!!!!
      
      private static final int NUM_OF_TARGETS_COL = 10;// Can't be less than 10
      private static final int NUM_OF_TARGETS_ROW = 3; // was 3
      private static final int NUM_OF_OBSTACLES = 7; // was 7
   
      private int targetsVelocity = 0; // initial velocity of targets
      private int TARGET_LIVES = 1; // minimum life for the target
      private int TARGET_SPEED = 1; // minimum speed for the target
      // 2D array to hold more than 1 row of enemy:
      private Target[][] targets = new Target[NUM_OF_TARGETS_ROW][NUM_OF_TARGETS_COL]; 
   
      private Player player;
      private int PLAYER_LIVES = 3; // minimum life for the player
   
      private Obstacle[] obstacles = new Obstacle[NUM_OF_OBSTACLES];
   
      private boolean isAnyAlive = false; // Used with the player's bullets to determine if they are excited on the screen
      private boolean isAnyAliveEnemyBullets = false; // Used for the enemy's bullets
      private boolean enemyAlive = false; // If the player didn't kill all the targets then a different menue will be shown
   
      private URL enemyDie = Space_invadors.class.getResource("enemyDying.wav");
      private URL playerDie = Space_invadors.class.getResource("playerDying.wav");
      private URL enemyS = Space_invadors.class.getResource("enemyShoot.wav");
      private URL playerS = Space_invadors.class.getResource("playerShoot.wav");
      private AudioClip enemyClip = Applet.newAudioClip(enemyDie);
      private AudioClip playerClip = Applet.newAudioClip(playerDie);
      private AudioClip enemyShoot = Applet.newAudioClip(enemyS);
      private AudioClip playerShoot = Applet.newAudioClip(playerS);
   
      private JComboBox optionsMenue;
      private URL mainURL = Space_invadors.class.getResource("backSound.wav");
      private AudioClip mainClip = Applet.newAudioClip(mainURL);
      
      private int score = 0;
      private String difficultyLevel = "";
   
      private float interpolation;
      private int fps = 60;
      private int frameCount = 0;
   
      //private boolean backgroundBool = true;
   	/**
   	 * The constractor of the class will instantiate the obstacle, target,
   	 * Bullet and the player classes in order to create full functioning
   	 * this. Also, will create a new gameObject for the background image.
   	 * 
   	 * @param obstaclesIcon - Passing an image parameter to replace the default one
   	 *  for the obstacle view.
   	 * @param enemyIcon - Passing an image parameter to replace the default one
   	 *  for the enemy view.
   	 * @param playerIcon - Passing an image parameter to replace the default one
   	 *  for the player view.
   	 */
      public Game(ImageIcon playerIcon, ImageIcon enemyIcon, ImageIcon obstaclesIcon) {
         
      	// enemyDie = Space_invadors.class.getResource("Dying1.wav");
      	// enemyClip = Applet.newAudioClip(enemyDie);
      	/*
      	 * prg = new JProgressBar(0,100); prg.setValue(100);
      	 * prg.setStringPainted(true); prg.setEnabled(true); add(prg);
      	 * //prg.setString("100");
      	 */
      
      	// Add the background:
         background = new ImageIcon(getClass().getResource("backGray.jpg")).getImage();
         
      	// Creates obstacles
         int obstacleX = 25, lives = 5; // each brick has 5 lives
         for (int i = 0; i < obstacles.length; i++, obstacleX += 100) {
         
            if (obstaclesIcon == null)
               obstacles[i] = new Obstacle(null, obstacleX + 25, 500, lives);
            else
               obstacles[i] = new Obstacle(obstaclesIcon.getImage(), obstacleX + 50, 500, lives);
         
            this.add(obstacles[i]);
         }
      
      	// Create targets
         int targetX = 0, targetY = 50;//The spaces bet the targets
      
         for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
         
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++, targetX += 50) {
            
               if (enemyIcon == null)
                  targets[a][i] = new Target(null, targetX, targetY, TARGET_LIVES);
               else
                  targets[a][i] = new Target(enemyIcon.getImage(), targetX, targetY, TARGET_LIVES);
            
               this.add(targets[a][i]); // <<<<<<----------------------------------------------------------------<<<<<<<<<<<<<<<<<<
            }
         
            targetY += 50;
            targetX = 0;
         }
      
      	// Create the player:
         if (playerIcon == null)
            player = new Player(null, 350, 650, PLAYER_LIVES, 0);
         else
            player = new Player(playerIcon.getImage(), 350, 650, PLAYER_LIVES, 0);
      
         this.add(player); // Adds player to the panel
         
         this.addKeyListener(this); // Adds keyListener to the panel
         
         //this.setFocusable(true);
      }
   
   	/**
   	 * The paint method is used to paint an gameObject as a graphical user
   	 * interface. The paintComponent method will be called from the repaint
   	 * method.
   	 * 
   	 * @param g - The specified graphic gameObject to paint in the window
   	 *            (self-customize one)
   	 */
      @Override
      protected void paintComponent(Graphics g) {
      
      	// The background of the window will be drawn manually, therefore no
      	// need for:
         super.paintComponent(g); //Passes the graphics context off to the
      	// component's UI delegate, which paints the panel's background
        
         g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
            
         player.drawPlayer(g); // draws player on the screen
      
         for (int i = 0; i < obstacles.length; i++)
            obstacles[i].drawObstacles(g); // draws obstacles on the screen
      
         for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) { // draws targets on the screen
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
            
               targets[a][i].drawTarget(g);
               
            	// Drawing the bullets for the enemy:
               for (Iterator<Bullet> it = targets[a][i].bulletsE.iterator(); it.hasNext(); /* NO arg */){
               
               /*
               THE FOLOWING STATMENT CAUSING A PROBLEM IN THE PROGRAM WHEN THE USER KILL THE MOST RIGHT ENEMY.
               */
                  try{
                     
                     Bullet temp = it.next(); // <--------
                     //System.out.println(temp);
                     //System.out.println(it.next());
                     temp.drawBullet(g);
                  }
                  catch(Exception ConcurrentModificationException){
                     System.out.println("Exception ----> " + ConcurrentModificationException + " in paintComponent method");
                     System.exit(0);
                  }
               }
            }
         }
      
      	// Drawing the bullets for the player:
         for (Iterator<Bullet> it = player.bulletsP.iterator(); it.hasNext();) {// bullets.iterator()
            Bullet b = it.next();
            b.drawBullet(g); // draws player bullets on the screen from the Player class
         }
      
         // Drawing the info line at the top of the window:
         g.setColor(Color.BLACK);
         g.setFont(new Font(null, Font.BOLD, 30));
         g.fillRect(0,0, 800,30);
         
         if(player.lives == 1){
            g.setColor(Color.RED);
            g.drawString("Player's lives:", 10, 25); // (The string to draw, x, y)
            g.drawString(Integer.toString(player.lives), 210, 25);
         }
         else{
            g.setColor(Color.WHITE);
            g.drawString("Player's lives:", 10, 25); // (The string to draw, x, y)
            g.drawString(Integer.toString(player.lives), 210, 25);
         }
         
         g.setColor(Color.WHITE);
         
         g.drawString("Score:", 250, 25); // (The string to draw, x, y)
         g.drawString(Integer.toString(score), 350, 25);
      
         g.drawString("Level of difficulty:", 400, 25); // (The string to draw, x, y)
         g.drawString(difficultyLevel, 660, 25);
      
         g.drawLine(0,30, 800,30);
         
         g.dispose();// Disposes of this graphics context and releases any system resources that it is using
         
         frameCount++;//Counting the frames or refrashment
      }
   
   	/**
   	 * moveObjects method is used to move the gameObjects on the screen, in this
   	 * case the gameObjects are the bullets/targets. The method will not paint
   	 * the gameObjects, it will only move them to a different location on the
   	 * panel.
   	 */
      public void moveObjects() {
      
      /*Creating a local variable to direct the enemy on the y-value, 
        after the enemy reached every edge, it will go down ten levels:*/
         int moveY = 0;
      
      	/*
      	 * The interface Iterator<E> is a linked list that takes a type
      	 * Bullet (from the Bullet class) and iterate among the elements
      	 * inside the linked list that the Bullet class has. E - The type of
      	 * elements returned by this iterator (Bullet gameObject)
      	 * 
      	 * The "it" is a pointer pointing to the head of the list and as
      	 * long that .iterator returns another element the for loop will
      	 * continue
      	 */
      	// Moving the player bullets:
         for (Iterator<Bullet> it = player.bulletsP.iterator(); it.hasNext();/* NO arg */) {
         
            Bullet tempBullet = it.next(); // pull out a Bullet gameObjects from the list; 1 each time
            isAnyAlive = tempBullet.isAlive ? true : false;
            tempBullet.y = tempBullet.y - 13; // move the bullet 13 pixels up each repaint()
         }
      
      	// ----------------------------------------------------------------------------------------------------------------------------------
      	// Check if the targets got to either one of the sides:
         if (targets[0][NUM_OF_TARGETS_COL - 1].x == 750) { // targets move in relation to the far right target
         
            targetsVelocity = -1 * TARGET_SPEED; // targets move left
            moveY = 10; // targets go down one row
         } 
         else if (targets[0][NUM_OF_TARGETS_COL - 1].x == 450) { // targets move in relation to the far left target
            
            targetsVelocity = TARGET_SPEED; // targets move right
            moveY = 10; // targets go down one row
         }
      	// ----------------------------------------------------------------------------------------------------------------------------------
         
         for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
            
            	// ---------------------------------------------------------------------------------------
               for (Iterator<Bullet> it = targets[a][i].bulletsE.iterator(); it.hasNext();/* NO arg */) {
               
                  Bullet temp = it.next();
                  isAnyAliveEnemyBullets = temp.isAlive ? true : false;
                  temp.y += 6;
               }
            	// ---------------------------------------------------------------------------------------
            
               targets[a][i].x = targets[a][i].x + targetsVelocity; // move the targets to either left or right
               targets[a][i].y = targets[a][i].y + moveY; // move the targets down
            }
         }
      }
   
   	/**
   	 * The anyHit method measuring the bullet and the target x and y axis to
   	 * report if ther's a hit or not.
   	 */
      public void anyHit() { // compares each bullet x,y to each target x,y
         
      	// -----------------------------THE PLAYER BULLET-------------------------------------------
         for (Iterator<Bullet> it = player.bulletsP.iterator(); it.hasNext(); /* NO args */) {
         
         // Pulling out 1 bullet gameObject from the list at a time:
            Bullet tempBullet = it.next(); 
            
            if (tempBullet.isAlive) { 
            
            // If bullet is still on the screen Check the position of the
            // bullet corresponding to the target:
               for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
                  for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
                  
                     if (targets[a][i].isAlive) { // If the enemy is still in the game
                     
                        //boolean hit = false;
                     
                     	// Checking for matching locations:
                        if (tempBullet.x >= targets[a][i].x && tempBullet.x <= targets[a][i].x + 50
                         && tempBullet.y >= targets[a][i].y && tempBullet.y <= targets[a][i].y + 50){
                         
                           //hit = true; // <-- If there is a hit
                           
                           // Delete the bullet from the screen:
                           tempBullet.isAlive = false;
                        
                        // If the target had more than 0, subtract 1:
                           if (targets[a][i].lives > 0) {
                              targets[a][i].lives -= 1;
                           }
                           
                           // if target has 0 lives, delete the icon from the screen:
                           if (targets[a][i].lives == 0) {
                              targets[a][i].isAlive = false;
                              enemyClip.play();
                           }
                           
                           //Increase the player score by 2:
                           score += 2;
                        
                        }
                        
                        /*if (hit) {// If the bullet hit the target:
                        
                        // Delete the bullet from the screen:
                           tempBullet.isAlive = false;
                        
                        // If the target had more than 0, subtract 1:
                           if (targets[a][i].lives > 0) {
                              targets[a][i].lives -= 1;
                           }
                           
                           // if target has 0 lives, delete the icon from the screen:
                           if (targets[a][i].lives == 0) {
                              targets[a][i].isAlive = false;
                              enemyClip.play();
                           }
                        }*/
                     }
                     
                  // if bullet flew off the screen without hitting any targets:
                     if (tempBullet.isAlive && tempBullet.y <= 0) 
                        tempBullet.isAlive = false;
                  }
               }
            
            	// Check the position of the bullet corresponding to the obstacle:
               for (int i = 0; i < obstacles.length; i++) {
               
                  boolean hit = false;
               
                  if (obstacles[i].isAlive) {
                     
                     // Taking the mesurments of the obstacle and compare it to the bullet possitoin:
                     if (tempBullet.x >= obstacles[i].x - 10 && tempBullet.x <= obstacles[i].x + 60
                      && tempBullet.y >= obstacles[i].y && tempBullet.y <= obstacles[i].y + 40)
                        hit = true;
                  
                     if (hit) {
                        tempBullet.isAlive = false;
                        obstacles[i].lives -= 1; // reduces the brick life by 1;
                     }
                     if (obstacles[i].lives == 0) {
                        obstacles[i].isAlive = false; // brick dies after 5 hits
                     }
                  }
               }
            }
         }
      
      	// -----------------------------THE ENEMY BULLET-------------------------------------------
         for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
            
               for (Iterator<Bullet> it = targets[a][i].bulletsE.iterator(); it.hasNext();/* NO arg */) {
               
               	// Checking the position of the BULLET of EACH ENEMY:
                  Bullet temp = it.next();
               
                  if (temp.isAlive) {
                  
                     boolean hit = false;
                  
                  	// Check if one of the bullets of the enemy match the location of the player:
                     if (temp.x >= player.x && temp.x <= player.x + 50&& 
                         temp.y >= player.y && temp.y <= player.y + 50) {
                        hit = true;
                     }
                     if (hit) {// If the bullet hit the Player:
                        temp.isAlive = false;// The enemy's bullet is deleted from the screen
                     
                        if (player.lives > 0) {// If the Player had more than 0, subtract 1
                           player.lives -= 1;
                        }
                     }
                  }
               
               	// If there was no hit:
                  if (temp.isAlive && temp.y >= 800){ //if bullet flew off the screen without hitting any targets
                     isAnyAliveEnemyBullets = false;// ?????????????????????? NOT SURE!!!!!!!!!!!!!!!!
                     temp.isAlive = false;
                  }
               }
            }
         }
      }
   
      public boolean isGameOver() { // Checks if alive targets are left
      
         boolean gameOver = true;
      
         Dead: for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
            
               if (targets[a][i].isAlive) {
                  gameOver = false;
               
               	// If the player didn't kill all the targets he/she
               	// automatically lose
                  if (targets[a][i].y == 500) {
                  
                     gameOver = true;
                     enemyAlive = true;
                  
                     playerClip.play();
                  
                     break Dead;
                  }
               }
            }
         }
      
      	// ADD HERE THE CASE WHEN THE USER GOT KILLED BY THE ENEMY
         if (player.lives == 0) {
            playerClip.play();
            JOptionPane.showMessageDialog(null, "YOU'RE A DEAD MEAT!!!");
         
            gameOver = true;
         }
      
         return gameOver;
      }
   
      public void enemyShoot() {
      
         for (int a = 0; a < NUM_OF_TARGETS_ROW; a++) {
            for (int i = 0; i < NUM_OF_TARGETS_COL; i++) {
               
               // if there is an enemy at the corrent location AND there
               // is no bullets on the screen from the enemy then:
               if (targets[a][i].isAlive && !isAnyAliveEnemyBullets) {
                  enemyShoot.play();
                  // Releases a bullet from where the player currently is:
                  Bullet enemyBullet = new Bullet(targets[a][i].x + 10, targets[a][i].y + 35, 1);
                   // Add the bullet to the list of Bullets:
                  targets[a][i].bulletsE.add(enemyBullet); 
                  // Add the bullet to the screen:
                  this.add(enemyBullet); 
               }
            }
         }
      }
   
      public void startMenue() {
      
      	// JOptionPane to inform about selcting the level"
         JLabel difficulty = new JLabel("Please choose a level of dificulty from the menue");
         difficulty.setFont(new Font("Arial", Font.BOLD, 18));
         JOptionPane.showMessageDialog(null, difficulty, "DIFFICULTY", JOptionPane.WARNING_MESSAGE);
      
      	// Create the options for the player:
         String[] options = { "Grandma (Easy)", "Player (Mid)", "KILL ZONE (Hard)" };
         JComboBox optionsMenue = new JComboBox(options);
         optionsMenue.setEditable(false);
         optionsMenue.setFont(new Font("Arial", Font.BOLD, 18));
         JOptionPane.showMessageDialog(null, optionsMenue, "SELECT YOUR LEVEL!", JOptionPane.QUESTION_MESSAGE);
      
      	// Adjust the difficulty accordingly:
         switch (optionsMenue.getSelectedIndex()) {
         
            case 0:// Easy
               {
                  TARGET_LIVES = 1;
                  TARGET_SPEED = 1;
                  difficultyLevel = "Easy"; //To display the level on the screen
                  break;
               }
            case 1:// Mid
               {
                  TARGET_LIVES = 2;
                  TARGET_SPEED = 2;
                  difficultyLevel = "Mid";
                  break;
               }
            case 2:// Hard
               {
                  TARGET_LIVES = 3;
                  TARGET_SPEED = 3;
                  difficultyLevel = "Hard";
                  break;
               }
         }
      }
   
      @Override public void keyTyped(KeyEvent e) {/* not in use */}
      @Override public void keyReleased(KeyEvent e) {/* not in use */}
      @Override public void keyPressed(KeyEvent e) {
      
         //System.out.println("KEYBOARD");
      
         int code = e.getKeyCode();
      
         if (code == KeyEvent.VK_RIGHT) {
         
            if (!(player.x >= 700)) {
               player.x = player.x + 10;
            }
         }
         
         else if (code == KeyEvent.VK_LEFT) {
         
            if (!(player.x <= 0)) {
               player.x = player.x - 10;
            }
         }
         
         // releases a bullet each time SPACE pressed									
         else if (code == KeyEvent.VK_SPACE) {                                           
         // Checks that there is no bullets in the screen before shooting new one:
            if (!isAnyAlive) { 
               playerShoot.play();
               // Releases a bullet from where the player currently is:
               Bullet bullet = new Bullet(player.x + 29, player.y + 0, 0); 
               player.bulletsP.add(bullet); // add the bullet to the list of Bullets
               this.add(bullet); // add the bullet to the screen
            }
         }
      }
      
      private void drawGame(float interpolation){
      
         this.interpolation = interpolation;
         gameObj.repaint();
      }
    
      public void gameRun(ImageIcon player, ImageIcon enemy, ImageIcon obstacles) {
         
         //If the user pressed the default then I'll have null pointer exception
         if(player != null)
            this.player = new Player(player.getImage(), 350, 650, PLAYER_LIVES, 0);
         
         //That's the most importat statment, that will make the foucuse on the Game panel:
         this.requestFocus();
         
         //A variable to control the loop of the game:
         boolean staryContinue = true;
         
      //This value would probably be stored elsewhere.
         final double GAME_HERTZ = 30.0;
      //Calculate how many ns each frame should take for our target game hertz.
         final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
      //At the very most we will update the game this many times before a new render.
      //If you're worried about visual hitches more than perfect timing, set this to 1.
         final int MAX_UPDATES_BEFORE_RENDER = 5;
      //We will need the last update time.
         double lastUpdateTime = System.nanoTime();
      //Store the last time we rendered.
         double lastRenderTime = System.nanoTime();
      
      //If we are able to get as high as this FPS, don't render again.
         final double TARGET_FPS = 60;
         final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
      
      //Simple way of finding FPS.
         int lastSecondTime = (int) (lastUpdateTime / 1000000000);
         
         //Calling the start menu to add some more functionality to the user:
         startMenue();
      
         End: while(staryContinue){
          
            double now = System.nanoTime();
            int updateCount = 0;
         
            if (staryContinue){ //!paused
            
             //Do as many game updates as we need to, potentially playing catchup.
               while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER ){
               
                  gameObj.moveObjects();
                  gameObj.enemyShoot();
                  gameObj.anyHit();
                  if (isGameOver()){
                     
                     if(!enemyAlive){
                     
                        int input = JOptionPane.showConfirmDialog(null, 
                           "WELL DONE!!! \nYou killed them all! \nWanna go for anouther round?!", "GOD BLESS AMERICA", 2);
                     
                     /*IF THE PLAYER WON*/
                        if (input == 0){
                        
                           JOptionPane.showMessageDialog(null, "YOU ARE THE MAN!!!\n" + "Select a difficulty level in the following menu\n" + "HAVE FUN!!!");
                           startMenue();
                           //dispose();//Closing the window
                           
                           gameObj = new Game(null,null,null); // Starting a new session 
                           runGameLoop(null,null,null);
                        } 
                        else{
                           JOptionPane.showMessageDialog(null, "See you next time!!");
                           mainClip.stop();
                           dispose();//Closing the window
                           break End;
                        }
                     }
                     /*IF THE PLAYER LOSE*/
                     else{
                     
                        int input = JOptionPane.showConfirmDialog(null, "HEY GRANDPA! WHERE'S YOUR GLASSES?! Wanna try again?!", "Don't be a loser", 2);
                     
                        if (input == 0){
                           JOptionPane.showMessageDialog(null, "DO YOUR BEST THIS TIME!\n" + "Select a difficulty level in the following menu");
                        
                           //optionsMenue = startMenue(0);
                        
                           switch(optionsMenue.getSelectedIndex()){
                           
                              case 0:
                                 {
                                 
                                    TARGET_LIVES = 1;
                                    TARGET_SPEED = 1;
                                    break;
                                 }
                              case 1:
                                 {
                                 
                                    TARGET_LIVES = 2;
                                    TARGET_SPEED = 2;
                                    break;
                                 }
                              case 2:
                                 {
                                 
                                    TARGET_LIVES = 3;
                                    TARGET_SPEED = 3;
                                    break;
                                 }
                           }
                        
                           dispose();//Closing the window
                           gameObj = new Game(null,null,null); // Starting a new session 
                           //playGame(gameObj);
                        } 
                        else{
                           JOptionPane.showMessageDialog(null, "Tutoring sessions are in room N3029, good luck!");
                           mainClip.stop();
                           dispose();//Closing the window
                           break;
                        }
                     }
                  } 
                  
               //---------------------------------------------------------------------------
               
                  lastUpdateTime += TIME_BETWEEN_UPDATES;
                  updateCount++;
               }
            
            //If for some reason an update takes forever, we don't want to do too many catchups:
               if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
                  lastUpdateTime = now - TIME_BETWEEN_UPDATES;
               
            //Render. To do so, we need to calculate interpolation for a smooth render.
               float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
               drawGame(interpolation);
               lastRenderTime = now;
            
            //Update the frames we got.
               int thisSecond = (int) (lastUpdateTime / 1000000000);
               
               if (thisSecond > lastSecondTime){
               
                  System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                  fps = frameCount;
                  frameCount = 0;
                  lastSecondTime = thisSecond;
               }
            
            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
               while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES){
               
                  Thread.yield();
               
               //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
               //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
               //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                  try {Thread.sleep(1);} 
                  catch(Exception e) {} 
               
                  now = System.nanoTime();
               }
            }
         }
         System.exit(0);
      }
   }

   public static void main(String[] args) {
   
   	// Improving the basic java look and feel:
   	// DO NOT MOVE IT TO SOMEWHERE ELSE
      try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} 
      catch (Exception e){e.printStackTrace();}
   
      //Playing the main clip for the game:
      mainClip.loop();
   
      Space_invadors sn = new Space_invadors();
      sn.setVisible(true);
   }
}