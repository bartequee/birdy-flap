package flapper;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;



public class FlappyBird implements ActionListener, KeyListener{

    public static FlappyBird flappyBird;


    public Display display; 

    public Rectangle pipeDown; 
    public Rectangle pipeUp;
    public Rectangle ground;
    public Rectangle grass;
    public Rectangle bird;

    final int WIDTH = 600, HEIGHT = 800;

    public FlappyBird() {
        JFrame frjame = new JFrame("Flap you");
        flappyBird = this;
        
        Timer timer = new Timer(20, this);

        display = new Display();
        frjame.add(display);

        pipeDown = new Rectangle(265, 500, 90, HEIGHT);
        pipeUp = new Rectangle(265, -560, 90, HEIGHT);
        ground = new Rectangle(0, 700, WIDTH, 100);
        grass = new Rectangle(0, 700, WIDTH, 20);

        bird = new Rectangle(-30, 350, 30, 30);

        frjame.addKeyListener(this);
        frjame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frjame.setSize(WIDTH, HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frjame.setLocation(dim.width/2-frjame.getSize().width/2, dim.height/2-frjame.getSize().height/2);
        frjame.setResizable(false);

        // frjame.getContentPane().setBackground(Color.blue);
        frjame.setVisible(true);

        timer.start();

    //     this.setTitle("flap");
    //     this.setSize(WIDTH, HEIGHT);

    //     this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        display.repaint();
        if(isPressedDown) {
            down();
        }
        if(isPressedUp) {
            up();
        }

        
     }

    public void repaint(Graphics g) {


        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);


        g.setColor(new Color(103, 212, 77, 255));
		g.fillRect(pipeDown.x, pipeDown.y, pipeDown.width, pipeDown.height);
        g.fillRect(pipeUp.x, pipeUp.y, pipeUp.width, pipeUp.height);

        g.setColor(new Color(124, 82, 52, 255));
        g.fillRect(ground.x, ground.y, ground.width, ground.height);

        //set new color to natural looking green
        g.setColor(new Color(0, 153, 0, 255));
        g.fillRect(grass.x, grass.y, grass.width, grass.height);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        if (bird.x < WIDTH){
            bird.x += 5;

        }else{
            // TODO dodac opoznienei ptaka
            Random rand = new Random();
            bird.x = -30;
            bird.y = rand.nextInt(HEIGHT - 100);
        }

        
    }

    public static void main(String[] args) {
        FlappyBird birdie = new FlappyBird();
        
    }
    public void up() {
        if(pipeDown.y < 0) {
            return;
        }
        System.out.println("up");
        pipeDown.y -= 15;
        pipeUp.y -= 15;
    }
    public void down() {
        if(pipeDown.y > HEIGHT) {
            return;
        }
        System.out.println("down");
        pipeDown.y += 15;
        pipeUp.y += 15;
    }
    boolean isPressedUp = false;
    boolean isPressedDown = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            isPressedUp = true;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            isPressedDown = true;
        }

        }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("kurwa");
    }
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("relesas");
        isPressedUp = false;
        isPressedDown = false;
        
    }



}


