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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
    public int score = 0;

    protected boolean stop = false;
    private boolean isPressedUp = false;
    private boolean isPressedDown = false;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    final int WIDTH = 600, HEIGHT = 800;

    public FlappyBird() {
        JFrame frjame = new JFrame("Flap you");

        flappyBird = this;
        timer = new Timer(20, this);
        display = new Display();

        pipeDown = new Rectangle(265, 500, 90, HEIGHT);
        pipeUp = new Rectangle(265, -560, 90, HEIGHT);
        ground = new Rectangle(0, 700, WIDTH, 100);
        grass = new Rectangle(0, 700, WIDTH, 20);

        bird = new Rectangle(-30, 350, 30, 30);

        frjame.add(display);
        frjame.addKeyListener(this);

        frjame.setSize(WIDTH, HEIGHT);
        frjame.setLocation(dim.width / 2 - frjame.getSize().width / 2, dim.height / 2 - frjame.getSize().height / 2);
        frjame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frjame.setResizable(false);
        frjame.setVisible(true);
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
        int choice = JOptionPane.showConfirmDialog(null, String.format("Score:%2d \nDo you want to restart the game?", score), "Game Over",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Restart the game
            restart();
        } else {
            // Exit the game
            System.exit(0);
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

        // Restart the timer
        timer.start();
    }
    public int jump = 0;
    public int fallSpeed = 1;
    public int raiseSpeed = 10;


    public void birdMove() {
        Random rand = new Random();

        // Move the bird to the right
        bird.x += 5;

        // If the bird goes off the screen, reset it
        if (bird.x >= WIDTH) {
            bird.x = -30;
            bird.y = rand.nextInt(HEIGHT - 100);
            score += 1;
            return;
        }

        // If the bird is too high, move it down
        if(bird.y <= 100){
            bird.y += fallSpeed/5;
        }
        // If the bird is too low, move it up   
        if(bird.y >= HEIGHT - 200){
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

        if(jump == 0){
            bird.y += fallSpeed/4;
        }else{
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

    public void repaint(Graphics g) {

        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(103, 212, 77, 255));
        g.fillRect(pipeDown.x, pipeDown.y, pipeDown.width, pipeDown.height);
        g.fillRect(pipeUp.x, pipeUp.y, pipeUp.width, pipeUp.height);

        g.setColor(new Color(124, 82, 52, 255));
        g.fillRect(ground.x, ground.y, ground.width, ground.height);

        // set new color to natural looking green
        g.setColor(new Color(0, 153, 0, 255));
        g.fillRect(grass.x, grass.y, grass.width, grass.height);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        if (bird.intersects(pipeDown)|| bird.intersects(pipeUp) ) {
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
        if (pipeDown.y < 0) {
            return;
        }
        System.out.println("up");
        pipeDown.y -= 15;
        pipeUp.y -= 15;
    }

    public void down() {
        if (pipeDown.y > HEIGHT) {
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
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isPressedDown = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("relesas");
        isPressedUp = false;
        isPressedDown = false;
    }

}