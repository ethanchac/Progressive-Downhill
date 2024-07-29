import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
//import sun.audio.*;


public class panel extends JPanel implements ActionListener, KeyListener, MouseListener{
    char direction = 'W';
    int size = 50;
    int units = (1000 * 1000) / size;
    public int x = 500;
    public int y = 750;
    Timer timer;
    boolean alive = false;
    int ScreenID = 0;
    JLabel startLabel = new JLabel();

    //Images
    BufferedImage startscreenL = null;
    BufferedImage startscreenB = null;
    BufferedImage helpscreenL = null;
    BufferedImage helpscreenB = null;
    BufferedImage shipIMG = null;
    BufferedImage enemyIMG = null;
    BufferedImage bulletIMG = null;
    BufferedImage skyIMG = null;
    BufferedImage losescreen = null;
    BufferedImage victoryscreen = null;
    BufferedImage helpscreen = null;

    //Screen 1
    JButton startButton = new JButton("Start");

    //Screen 2
    public boolean shoot = false;
    public boolean hit = false;
    public int damage;
    public int damageDebuff = 1;
    public int bullX = x;
    public int bullY = y;
    public int aimCount = 0;
    public int level = 1;
    public int count = 0;
    public int tileL = 8;
    public int enemyY = -400;
    public int enemyX = -100;
    public int aimY = 0;
    public int aimX = -100;
    public int[][] enemies = new int[4][8];
    public Enemy[][] layout = new Enemy[4][8];
    public Enemy[][] aimLayout = new Enemy[7][7];
    public int[][] aimL = new int[7][7];
    public int Rtimer = 300;
    public int randX = 0;
    public int randY = 0;
    public int mouseX;
    public int mouseY;
    public boolean startHover = false;
    public boolean helpHover = false;
    

    panel(){
        this.setPreferredSize(new Dimension(1300, 1000));
        //this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        this.setBackground(Color.white);
        this.addKeyListener(this);
        this.setLayout(null);
        this.addMouseListener(this);
        startButton.setSize(400, 200);
        startButton.setLocation(0, 0);
        startButton.addActionListener(this);
        enemies();
        start();
        for(int j = 0; j < 4; j++){
            for(int i =0; i < 8; i ++){
                if(enemies[j][i] == 1){
                    layout[j][i] = new Enemy(enemyX, enemyY, 10, 100);
                }else{
                    layout[j][i] = new Enemy(-1000,-5000,10,0);
                }
                enemyX += 1000/8;
            }
            enemyY += 110;
            enemyX = 0;
        }
        aim();
        for(int j = 0; j < 7; j++){
            for(int i =0; i < 7; i ++){
                if(aimL[j][i] == 1){
                    aimLayout[j][i] = new Enemy(aimX, aimY, 10, 100);
                    System.out.println(aimLayout[j][i].x);
                }else{
                    aimLayout[j][i] = new Enemy(-1000,-1000,10,0);
                }
                aimX += 1000/8;
            }
            aimX = 0;
            aimY += 110;
        }
    }
    public void start(){
        alive = true;
        timer = new Timer(1000/60, this);
        timer.start();
    }
    public void reset(){
        level = 1;
    }
    public void wincheck(){
        if(count == 16){
            enemyX = 0;
            enemyY = -500;
            for(int k = 0; k < 4; k++){
                for(int m = 0; m < 8; m++){
                    layout[k][m] = new Enemy(-1000,-5000,10,0);
                }
            }
            for(int k = 0; k < 4; k++){
                for(int m = 0; m < 8; m++){
                    enemies[k][m] = 0;
                }
            }
            enemies();
            for(int j = 0; j < 4; j++){
                for(int i =0; i < 8; i ++){
                    if(enemies[j][i] == 1){
                        layout[j][i] = new Enemy(enemyX, enemyY, 10, 100);
                    }else{
                        layout[j][i] = new Enemy(-1000,-5000,10,0);
                    }
                    enemyX += 1000/8;
                }
                enemyY += 120;
                enemyX = 0;
            }
            count = 0;
            level++;
            if(level % 2 == 0){
                System.out.println("DEBUFF");
                damageDebuff++;
            }
        }
    }
    public void damage(){
        damage = 100/damageDebuff;
        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 8; i++){
                if(bullX > layout[j][i].x && bullX < layout[j][i].x + 100 && bullY > layout[j][i].y && bullY < layout[j][i].y + 100){
                    layout[j][i].health -= damage;
                    shoot = false;
                    if(layout[j][i].health <= 0){
                        layout[j][i].x = -90000;
                        layout[j][i].y = -90000;
                        count++;
                        System.out.println(count);
                    }
                }
            }
        }
    }
    public void aim(){
        randX = (int)(Math.random() * 6) + 1;
        randY = (int)(Math.random() * 6) + 1;
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if(i == randX && j == randY){
                    aimL[i][j] = 1;
                }
            }
        }
        System.out.println(randX + " " +randY);
        System.out.println(Arrays.deepToString(aimL));
    }
    public void aimReset(){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                aimLayout[i][j] = new Enemy(-1000,0,10,0);
            }
        }
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                aimL[i][j] = 0;
            }
        }
    }
    public void enemies(){
        HashSet<Integer> set = new HashSet<>();
        int enemiesNum = 4;
        for(int j = 0; j < 4; j++){
            set.clear();
            for(int i = 0; i < enemiesNum; i++){
                int rand = (int)(Math.random() * 7) + 1;
                while(set.contains(rand)){
                    rand = (int)(Math.random() * 7);
                }
                set.add(rand);
            }
            for(int k = 0; k < tileL; k++){
                if(set.contains(k)){
                    enemies[j][k] = 1;
                }
            }
        }
        System.out.println(Arrays.deepToString(enemies));
    }
    public static void music(String file){
        try{
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            try{
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }catch(Exception e){

            }
        }catch(Exception e){
            System.out.println("No file");
        }
        
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == startButton){
            ScreenID++;
        }
        if(e.getSource() == timer){
            if(ScreenID == 1){
                if(shoot == true && bullY > 0){
                    bullY -= 20;
                }else{
                    shoot = false;
                    bullY = y;
                    bullX = x;
                }
                for(int j = 0; j < 4; j++){
                    for(int i = 0; i < 8; i++){
                        if(layout[j][i].y < 1000){
                            // += 1
                            layout[j][i].y += 1;
                        }else{
                            ScreenID = 2;
                        }
                    }
                }
                damage();
                if(level % 4 ==0){
                    if(Rtimer > 0){
                        Rtimer -= 5;
                    }else{
                        ScreenID = 2;
                    }
                } 
            }
            wincheck(); 

        }
        repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        SwingUtilities.convertPointFromScreen(b, this);
        mouseX = (int) b.getX();
        mouseY = (int) b.getY();
        switch(ScreenID){
            case 0:
                //System.out.println(mouseX+" "+mouseY);
                if(startscreenL == null){
                    try{
                        startscreenL = ImageIO.read(new File("startL.png"));
                    }catch(IOException e){
                        System.out.println("no image");
                    }
                }
                if(startscreenB == null){
                    try{
                        startscreenB = ImageIO.read(new File("startB.png"));
                    }catch(IOException e){
                        System.out.println("no image");
                    }
                }
                if(helpscreenL == null){
                    try{
                        helpscreenL = ImageIO.read(new File("helpL.png"));
                    }catch(IOException e){
                        System.out.println("no image");
                    }
                }
                if(helpscreenB == null){
                    try{
                        helpscreenB = ImageIO.read(new File("helpB.png"));
                    }catch(IOException e){
                        System.out.println("no image");
                    }
                }
                g.drawImage(helpscreenB, 400, 500, null);
                if(startHover == true){
                    g.drawImage(startscreenL, 400, 250, null);
                    System.out.println("draw start light");
                }else{
                    g.drawImage(startscreenB, 400, 250, null);
                }
                //this.add(startButton);
                break;
            case 1:
                if(level == 10){
                    ScreenID = 3;
                }
                g.setColor(Color.white);
                if(shipIMG == null){
                    try{
                        shipIMG = ImageIO.read(new File("ship.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                if(enemyIMG == null){
                    try{
                        enemyIMG = ImageIO.read(new File("enemy.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                if(bulletIMG == null){
                    try{
                        bulletIMG = ImageIO.read(new File("fireball.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                if(skyIMG == null){
                    try{
                        skyIMG = ImageIO.read(new File("sky.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                //g.fillRect(x, y, size, size);
                g.drawImage(skyIMG, 0,-250,null);
                g.drawImage(skyIMG, 0, 300,null);
                g.fillRect(1000, 0, 300, 1000);
                g.setColor(Color.black);
                g.setFont(new Font("TimesRoman", Font.BOLD, 30)); 
                g.drawString("Level: "+level, 1050, 50);
                //g.drawString("Shots to Kill: "+(100/damage), 1050, 100);
                if(level % 4 != 0){
                    for(int j = 0; j < 4; j++){
                        for(int i = 0; i < 8; i++){
                            g.drawImage(enemyIMG, layout[j][i].x, layout[j][i].y, null);
                            g.drawImage(shipIMG, x, y, null);
                            g.drawImage(bulletIMG, bullX, bullY, null);
                        }        
                    }
                }else if(level % 4 == 0){
                    g.drawString("Timer: ", 1000, 350);
                    g.setColor(Color.red);
                    g.fillRect(1000, 450, Rtimer, 30);
                    for(int j = 0; j < 7; j++){
                        for(int i = 0; i < 7; i++){
                            g.drawImage(enemyIMG, aimLayout[j][i].x, aimLayout[j][i].y, null);
                        }        
                    }
                    if(hit == true){
                        aimReset();
                        aim();
                        aimX = -100;
                        aimY = 0;
                        for(int j = 0; j < 7; j++){
                            for(int i =0; i < 7; i ++){
                                if(aimL[j][i] == 1){
                                    aimLayout[j][i] = new Enemy(aimX, aimY, 10, 100);
                                    System.out.println(aimLayout[j][i].x);
                                }else{
                                    aimLayout[j][i] = new Enemy(-1000,0,10,0);
                                }
                                aimX += 1000/8;
                            }
                            aimX = -100;
                            aimY += 110;
                        }
                        hit = false;
                        Rtimer = 300;
                        aimCount++;
                    }
                    if(aimCount == 8){
                        level++;
                        aimCount = 0;
                    }
                }
                break;
            //Lose screen
            case 2:
                if(losescreen == null){
                    try{
                        losescreen = ImageIO.read(new File("lose.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                g.drawImage(losescreen,0,0,null);
                break;
            //victory screen
            case 3:
                if(victoryscreen == null){
                    try{
                        victoryscreen = ImageIO.read(new File("victory.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                g.drawImage(victoryscreen, 0, 0, null);
                music("victory.wav");
                break;
            //help
            case 4:
                if(helpscreen == null){
                    try{
                        helpscreen = ImageIO.read(new File("help.png"));
                    }catch(IOException e){
                        System.out.println("none");
                    }
                }
                g.drawImage(helpscreen,0,0,null);
                break;
        }

    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W && y > 0){
            y -= 20;       
        }else if(e.getKeyCode() == KeyEvent.VK_A && x > 0){
            x -= 20;
        }else if(e.getKeyCode() == KeyEvent.VK_S && y < 950){
            y += 20;
        }else if(e.getKeyCode() == KeyEvent.VK_D && x < 950){
            x += 20;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shoot = true;
        }
    }
    public void keyReleased(KeyEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
        //System.out.println("Clitck");
    }
    public void mousePressed(MouseEvent e) {
        if(ScreenID == 0){
            if(mouseX > 400 && mouseX < 900 && mouseY > 250 && mouseY < 450){
                System.out.println("Clicked");
                ScreenID ++;
                this.remove(startButton);
            }
            if(mouseX > 400 && mouseX < 900 && mouseY > 500 && mouseY < 700){
                System.out.println("Clicked");
                ScreenID = 4;
                this.remove(startButton);
            }
        }
        if(ScreenID == 2){
            if(mouseX > 779 && mouseX < 1179 && mouseY > 566 && mouseY < 625){
                //reset();
                //ScreenID = 0;

            }
        }
        if(ScreenID == 4){
            if(mouseX > 1008 && mouseX < 1213 && mouseY > 57 && mouseY < 92){
                ScreenID = 0;
            }
        }
        if(level % 4 == 0){
            //System.out.println("mouse is at (x): "+mouseX);
            //System.out.println("mouse is at (y): "+mouseY);
            for(int j = 0; j < 7; j++){
                for(int i = 0; i < 7; i++){
                    if(mouseX > aimLayout[j][i].x && mouseX < aimLayout[j][i].x + 100 && mouseY > aimLayout[j][i].y && mouseY < aimLayout[j][i].y + 100){
                        System.out.println("hit");
                        damage = 100/damageDebuff;
                        aimLayout[j][i].health -= damage;
                        System.out.println(aimLayout[j][i].health);
                        if(aimLayout[j][i].health <= 0){
                            aimLayout[j][i].x = -90000;
                            aimLayout[j][i].y = -90000;
                            hit = true;
                            //System.out.println(count);
                        }
                    }
                }        
            }
        } 
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}
