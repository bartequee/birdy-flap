package flapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import javax.swing.JTextField;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener {

    public static FlappyBird flappyBird;
    public Display display;
    public Timer timer;

    public Rectangle pipeDown;
    public Rectangle pipeUp;
    public Rectangle ground;
    public Rectangle grass;
    public Rectangle bird;
    public int cycle;
    public int score;
    public int birdSpeed;

    final ImageIcon frameIcon = new ImageIcon("src\\flapper\\resources\\bird.png");
    final ImageIcon backgroundGraphicTemp = new ImageIcon("src\\flapper\\resources\\background.png");
    Image backgroundGraphic = backgroundGraphicTemp.getImage();
    final ImageIcon groundGraphicTemp = new ImageIcon("src\\flapper\\resources\\ground.png");
    Image groundGraphic = groundGraphicTemp.getImage();
    final ImageIcon birdGraphicTemp = new ImageIcon("src\\flapper\\resources\\bird.png");
    Image birdGraphic = birdGraphicTemp.getImage();
    final ImageIcon pipeUpGraphicTemp = new ImageIcon("src\\flapper\\resources\\pipe_up.png");
    Image pipeUpGraphic = pipeUpGraphicTemp.getImage();
    final ImageIcon pipeDownGraphicTemp = new ImageIcon("src\\flapper\\resources\\pipe_down.png");
    Image pipeDownGraphic = pipeDownGraphicTemp.getImage();

    protected boolean stop = false;
    private boolean isPressedUp = false;
    private boolean isPressedDown = false;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    final int WIDTH = 600, HEIGHT = 800;

    
    public FlappyBird() {
        JFrame frame = new JFrame("Flap you");

        flappyBird = this;
        timer = new Timer(20, this);
        display = new Display();

        // InputStream is = FlappyBird.class.getResourceAsStream("resources\\arcade_font.ttf");
        // Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        // Font scoreFont = font.deriveFont(12f);

        pipeDown = new Rectangle(265, 500, 90, HEIGHT);
        pipeUp = new Rectangle(265, -560, 90, HEIGHT);
        ground = new Rectangle(0, 700, WIDTH, 100);
        grass = new Rectangle(0, 700, WIDTH, 20);
        bird = new Rectangle(-30, 350, 30, 30);

        frame.add(display);
        frame.addKeyListener(this);
        
        frame.setIconImage(frameIcon.getImage());
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        restart();
    }

    public void actionPerformed(ActionEvent e) {
        if (!stop) {
            display.repaint();
            if (isPressedDown) {
                down();
            }
            if (isPressedUp) {
                up();
            }
        } else {
            stopper();
        }
        cycle+=1;
    }

    public void stopper() {
        stop = true;
        System.out.println("stop");
        timer.stop();
        bird.x = -30;
        int choice = JOptionPane.showConfirmDialog(null, String.format("Score:%2d \nDo you want to play again?", score), 
            "Game Over", JOptionPane.YES_NO_OPTION);
        switch(choice){
            case JOptionPane.YES_OPTION:    // Restart
                restart();
                break;
            case JOptionPane.NO_OPTION:     // Quit game
                System.exit(0);
                break;
        }
    }

    public void restart() {
        // Reset the game variables
        cycle = 1;
        isPressedDown = false;
        isPressedUp = false;
        stop = false;
        pipeDown.x = 265;
        pipeDown.y = 500;
        pipeUp.x = 265;
        pipeUp.y = -560;
        score = 0;
        birdSpeed = 5;

        // Restart the timer
        timer.start();
    }
    public int jump = 0;
    public int fallSpeed = 1;
    public int raiseSpeed = 10;


    public void birdMove() {    //TODO: rewrite movementu
        Random rand = new Random();

        // Move the bird to the right
        bird.x += birdSpeed;

        // If the bird goes off the screen, reset it
        if (bird.x >= WIDTH) {
            bird.x = -30;
            bird.y = rand.nextInt(HEIGHT - 100);
            score += 1;
            increaseDifficulty();
            return;
        }

        // If the bird is too high, move it down
        if (bird.y <= 100) {
            bird.y += fallSpeed/5;
        }
        // If the bird is too low, move it up   
        if (bird.y >= HEIGHT - 200) {
            jump += 20;
            raiseSpeed = rand.nextInt(10, 15);
            fallSpeed = 1;
        }
        if (raiseSpeed <= 0) {
            raiseSpeed = 1;
        }
         
        if (bird.x >= 240 && bird.x <= 280) {
            bird.y += fallSpeed/5;
            return;
        }
        // After the bird jumps, start falling; if it is jumping then smoothly raise the bird

        if (jump == 0) {
            bird.y += fallSpeed/4;
        } else {
            bird.y -= raiseSpeed;
            jump -= 1;
        }

        // jump - or not
        if(cycle % rand.nextInt(40, 60) == 0){
            jump = rand.nextInt(15, 20);
            raiseSpeed = rand.nextInt(10, 15);
            fallSpeed = 1;

        }
        raiseSpeed -= 1;
        fallSpeed += 1;
    }
    
    public void increaseDifficulty(){
        if (score%5 == 0 && (score%2 != 0 | score > 50)){
            birdSpeed += 2;
            System.out.println("popierdalacz 3000");
        } else if (score%10 == 0 && score < 51){
            pipeDown.y -= 20;
            pipeUp.y += 20;
            System.out.println("sie popiesci to sie zmiesci");
        }
    }

    public void repaint(Graphics g) {   //TODO: grafika

        g.drawImage(backgroundGraphic, 0, 0, null);

        // g.setColor(new Color(103, 212, 77, 255));
        // g.fillRect(pipeDown.x, pipeDown.y, pipeDown.width, pipeDown.height);
        // g.fillRect(pipeUp.x, pipeUp.y, pipeUp.width, pipeUp.height);
        g.drawImage(pipeUpGraphic, pipeUp.x, pipeUp.y, null);
        g.drawImage(pipeDownGraphic, pipeDown.x, pipeDown.y, null);

        g.drawImage(groundGraphic, 0, HEIGHT - groundGraphic.getHeight(null), null);

        //TODO: ten pierdolony font dodac
        g.setColor(Color.black);
        g.setFont(new Font("Comic Sans MS", 1, 60));
        //g.setFont(scoreFont);
        g.drawString(score+"", 15, 60);

        g.drawImage(birdGraphic, bird.x, bird.y, null);

        if (bird.intersects(pipeDown) || bird.intersects(pipeUp)) {
            System.out.println("collision");
            bird.x = 300;
            stop = true;
        }

        birdMove();
    }

    public static void main(String[] args) {
        FlappyBird birdie = new FlappyBird();
    }

    public void up() {
        if (pipeUp.y <= -HEIGHT) {
            return;
        }
        System.out.println("up");
        pipeDown.y -= 15;
        pipeUp.y -= 15;
    }

    public void down() {
        if (pipeDown.y >= HEIGHT - 100) {
            return;
        }
        System.out.println("down");
        pipeDown.y += 15;
        pipeUp.y += 15;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            isPressedUp = true;
        } if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isPressedDown = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("relesas");
        if (e.getKeyCode() ==  KeyEvent.VK_UP) {
            isPressedUp = false;
        } if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isPressedDown = false;
        }
    }
}