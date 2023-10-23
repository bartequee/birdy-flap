package flapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    public int score;
    Font customFont;


    private ImageIcon frameIcon;
    private Image backgroundGraphic;
    private Image groundGraphic;
    private Image birdGraphic;
    private Image pipeUpGraphic;
    private Image pipeDownGraphic;

    private ImageIcon groundGraphicTemp;
    private ImageIcon backgroundGraphicTemp;
    private ImageIcon birdGraphicTemp;
    private ImageIcon pipeUpGraphicTemp;
    private ImageIcon pipeDownGraphicTemp;

    protected boolean stop = false;
    private boolean isPressedUp = false;
    private boolean isPressedDown = false;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    final int WIDTH = 600, HEIGHT = 800;
    public int step = 0;
    public int starty = 0;

    public FlappyBird() {
        JFrame frame = new JFrame("Flap you");
        loadGraphics();
        flappyBird = this;
        timer = new Timer(20, this);
        display = new Display();

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

    public void loadGraphics(){
            frameIcon = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "bird.png");
            groundGraphicTemp = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "ground.png");
            backgroundGraphicTemp = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "background.png");
            birdGraphicTemp = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "bird.png");
            pipeUpGraphicTemp = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "pipe_up.png");
            pipeDownGraphicTemp = new ImageIcon("src" + File.separator + "flapper" + File.separator + "resources" + File.separator + "pipe_down.png");

        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/flapper/resources/arcade_font.ttf")).deriveFont(40f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }
        backgroundGraphic = backgroundGraphicTemp.getImage();
        groundGraphic = groundGraphicTemp.getImage();
        birdGraphic = birdGraphicTemp.getImage();
        pipeUpGraphic = pipeUpGraphicTemp.getImage();
        pipeDownGraphic = pipeDownGraphicTemp.getImage();
    }

    public void actionPerformed(ActionEvent e) {
        if (!stop) {

            display.repaint();
            // an optimized pipe-movement
            // updates every tick which provides variable
            // response speed, depending on tick rate,
            // allows user to move pipes quickly
            if (isPressedDown) {
                down();
            }
            if (isPressedUp) {
                up();
            }

        } else {
            stopper();
        }
    }

    public void stopper() {
        stop = true;
        timer.stop();
        bird.x = -30;
        int choice = JOptionPane.showConfirmDialog(null, String.format("Score:%2d \nDo you want to play again?", score),
                "Game Over", JOptionPane.YES_NO_OPTION);
        switch (choice) {
            case JOptionPane.YES_OPTION: // Restart
                restart();
                break;
            case JOptionPane.NO_OPTION: // Quit game
                System.exit(0);
                break;
        }
    }

    public void restart() {
        // Reset the game variables
        isPressedDown = false;
        isPressedUp = false;
        stop = false;
        score = 0;
        birdSpeed = 7;
        step = 0;
        

        // reset position of sprites
        pipeDown.x = 265;
        pipeDown.y = 500;
        pipeUp.x = 265;
        pipeUp.y = -560;
        bird.y = 350;

        // start the game loop again
        timer.start();
        jump();
    }

    public int birdSpeed = 5;

    public void birdMove() {
        Random rand = new Random();

        // ~ once in 10 ticks jump. as the jump has to be as smooth
        // as possible, it is split and its progress is monitored
        // using the <step> flag. each jump starts from step=0
        if (rand.nextInt(12) == 1) {
            if (! (bird.x > 250 && bird.x < 350)) {
                step = 0;

                jump();
            }
        }
        if(bird.y > 600){
            step = 0;
            jump();
        }
        // if the jump is not being done in this tick, proceed to
        // the jump initiated before
        if (step >= 1) {
            jump();
        }
        // move bird to the right with increaseing speed
        bird.x += birdSpeed;

        // control so that the bird doesn't fly out of bounds
        if (bird.y >= HEIGHT - 100) {
            jump();
        }

        // reset the bird after going out of field
        if (bird.x >= WIDTH) {
            bird.x = -30;
            bird.y = rand.nextInt(200, 500);
            step = 0;
            score += 1;
            increaseDifficulty();

            jump();
        }

    }

    public void jump() {
        if (step == 0) {
            starty = bird.y;
        }
        bird.y = starty + (int) Math.pow((step - 10), 2) / 3 - 33;
        step += 1;
    }

    public void increaseDifficulty() {
        if (score % 5 == 0 && (score % 2 != 0 | score > 50)) {
            birdSpeed += 2;
            System.out.println("popierdalacz 3000");
        } else if (score % 10 == 0 && score < 51) {
            pipeDown.y -= 20;
            pipeUp.y += 20;
            System.out.println("sie popiesci to sie zmiesci");
        }
    }

    public void repaint(Graphics g) { // TODO: grafika
         

        g.drawImage(backgroundGraphic, 0, 0, null);

        // g.setColor(new Color(103, 212, 77, 255));
        // g.fillRect(pipeDown.x, pipeDown.y, pipeDown.width, pipeDown.height);
        // g.fillRect(pipeUp.x, pipeUp.y, pipeUp.width, pipeUp.height);
        g.drawImage(pipeUpGraphic, pipeUp.x, pipeUp.y, null);
        g.drawImage(pipeDownGraphic, pipeDown.x, pipeDown.y, null);

        g.drawImage(groundGraphic, 0, HEIGHT - groundGraphic.getHeight(null), null);

        // TODO: ten pierdolony font dodac
        g.setColor(Color.black);
        // g.setFont(new Font("Comic Sans MS", 1, 60));
        g.setFont(customFont);
        // g.setFont(scoreFont);
        g.drawString(score + "", 15, 60);

        g.drawImage(birdGraphic, bird.x, bird.y, null);

        if (bird.intersects(pipeDown) || bird.intersects(pipeUp)) {
            bird.x = 300;
            stop = true;
        }

        birdMove();
    }


    public void up() {
        if (pipeDown.y <= 0) {
            return;
        }
        pipeDown.y -= 15;
        pipeUp.y -= 15;
    }

    public void down() {
        if (pipeDown.y >= HEIGHT + 200) {
            return;
        }
        pipeDown.y += 15;
        pipeUp.y += 15;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_W)) {
            isPressedUp = true;
        }
        if ((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S)) {
            isPressedDown = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if ((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_W)) {
            isPressedUp = false;
        }

        if ((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_S)){
            isPressedDown = false;
        }
    }

     public static void main(String[] args) {
        FlappyBird birdie = new FlappyBird();
    }
}