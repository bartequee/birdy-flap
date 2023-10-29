package flapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

/**
 * Main class of the game.
 */

public class FlappyBird implements ActionListener, KeyListener {
    // variables
    protected static FlappyBird flappyBird;
    private fileParse parser;
    private Display display;
    private Dimension dim;
    private Timer timer;
    private Rectangle pipeDown;
    private Rectangle pipeUp;
    private Rectangle bird;
    private Rectangle banner;
    private int score;
    private int birdSpeed;
    private Font arcadeFont;
    private int step;
    private int starty;

    // graphics
    private Image backgroundGraphic;
    private Image groundGraphic;
    private Image birdGraphic;
    private Image pipeUpGraphic;
    private Image pipeDownGraphic;

    // flags
    private boolean stop;
    private boolean isPressedUp;
    private boolean isPressedDown;
    private boolean moveBanner;
    private boolean scoreAdded;
    private boolean inMenu = true;

    // constants
    private final int WIDTH = 600;
    private final int HEIGHT = 800;

    /**
     * Constructor of the game.
     */
    public FlappyBird() {
        JFrame frame = new JFrame("Flap you");
        loadGraphics();
        this.timer = new Timer(20, this);
        this.display = new Display();
        this.dim = Toolkit.getDefaultToolkit().getScreenSize();
        parser = new fileParse();

        this.pipeDown = new Rectangle(265, 500, 90, HEIGHT);
        this.pipeUp = new Rectangle(265, -560, 90, HEIGHT);
        this.bird = new Rectangle(-30, 350, 30, 30);
        this.banner = new Rectangle(-100, 100, 150, 60);

        frame.add(display);
        frame.setIconImage(birdGraphic);
        frame.addKeyListener(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
                dim.height / 2 - frame.getSize().height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        flappyBird = this;

        restart();
    }

    private void loadGraphics() {
        // this way we don't have to specify the separate paths for windows and macs
        groundGraphic = new ImageIcon(
                "src" + File.separator + "flapper" + File.separator
                        + "resources" + File.separator + "ground.png")
                .getImage();
        backgroundGraphic = new ImageIcon(
                "src" + File.separator + "flapper" + File.separator
                        + "resources" + File.separator + "background.png")
                .getImage();
        birdGraphic = new ImageIcon(
                "src" + File.separator + "flapper" + File.separator
                        + "resources" + File.separator + "bird.png")
                .getImage();
        pipeUpGraphic = new ImageIcon(
                "src" + File.separator + "flapper" + File.separator
                        + "resources" + File.separator + "pipe_up.png")
                .getImage();
        pipeDownGraphic = new ImageIcon(
                "src" + File.separator + "flapper" + File.separator
                        + "resources" + File.separator + "pipe_down.png")
                .getImage();
        try {
            arcadeFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src" + File.separator + "flapper"
                            + File.separator + "resources" + File.separator + "arcade_font.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(arcadeFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for the game loop.
     */
    public void actionPerformed(ActionEvent e) {
        if (!stop) {
            display.repaint();

            if (isPressedDown) {
                down();
            }
            if (isPressedUp) {
                up();
            }
            if ((isPressedDown || isPressedUp) && inMenu) {
                inMenu = false;
            }

        } else {
            stopper();
        }
    }

    private void stopper() {
        stop = true;
        timer.stop();
        bird.x = -30;

        JPanel panel = new JPanel();
        panel.setSize(new Dimension(300, 70));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
        panel.setBackground(new Color(76, 154, 215));

        JLabel label = new JLabel(String.format(
            "<html><center><h1><b>%2d</b></h1>"
                + "Input your name<br>to save your score!</center></html>",
            score));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(arcadeFont.deriveFont(14f));
        panel.add(label);

        JTextField nameField = new JTextField();
        nameField.setColumns(5);
        nameField.setFont(arcadeFont.deriveFont(14f));
        panel.add(nameField);

        Object[] options = { "Save", "Discard" };
        UIManager.put("OptionPane.minimumSize", new Dimension(300, 70));
        UIManager.put("OptionPane.background", new ColorUIResource(76, 154, 215));
        UIManager.put("Panel.background", new ColorUIResource(76, 154, 199));

        int choiceSave = JOptionPane.showOptionDialog(null, panel, "Save high score!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[1]);
        // 1 is discard, 0 is save
        switch (choiceSave) {
            case 1:
                break;
            case 0:
                parser.add(nameField.getText().toUpperCase(), score);
                parser.write();
                break;
            default:
                break;
        }

        UIManager.put("OptionPane.minimumSize", new Dimension(10, 10));

        int choice = JOptionPane.showConfirmDialog(null,
                String.format("Score:%2d \nDo you want to play again?", score),
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.CANCEL_OPTION,
                new ImageIcon("src/flapper/resources/bird.png"));
        switch (choice) {
            case JOptionPane.YES_OPTION:
                restart();
                break;
            case JOptionPane.NO_OPTION:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    private void restart() {
        // Reset the game variables
        isPressedDown = false;
        isPressedUp = false;
        inMenu = true;
        stop = false;
        score = 0;
        birdSpeed = 5;
        step = 0;
        moveBanner = false;
        scoreAdded = false;

        // reset position of sprites
        pipeDown.y = 500;
        pipeUp.y = -560;
        bird.y = 350;
        banner.x = -105;

        // start the game loop again
        parser = new fileParse();
        timer.start();
        jump();
    }

    private void birdMove() {
        if (bird.intersects(pipeDown) || bird.intersects(pipeUp)) {
            bird.x = 300;
            stop = true;
        }

        bird.x += birdSpeed;

        // ~ once in 10 ticks jump. as the jump has to be as smooth
        // as possible, it is split and its progress is monitored
        // using the <step> flag. each jump starts from step=0
        Random rand = new Random();
        if (rand.nextInt(12) == 1) {
            if (!(bird.x > 215 && bird.x < 310)) {
                step = 0;
                jump();
            }
        }
        if (bird.y > 600) {
            step = 0;
            jump();
        }
        if (step >= 1) {
            jump();
        }
        if (moveBanner && banner.x <= 600) {
            banner.x += 10;
        } else if (banner.x > 600) {
            moveBanner = false;
            banner.x = -105;
        }
        if (bird.y >= HEIGHT - 100) {
            jump();
        }
        if (bird.x > pipeUp.x + pipeUp.width && !scoreAdded) {
            score += 1;
            scoreAdded = true;
            increaseDifficulty();
        } else if (bird.x >= WIDTH) {
            bird.x = -30;
            bird.y = rand.nextInt(220, 480);
            step = 0;
            scoreAdded = false;
            jump();
        }
    }

    private void jump() {
        if (step == 0) {
            starty = bird.y;
        }
        bird.y = starty + (int) Math.pow((step - 10), 2) / 3 - 33;
        step += 1;
    }

    private void increaseDifficulty() {
        if (score % 5 == 0 && (score % 2 != 0 | score > 50)) {
            birdSpeed += 2;
            System.out.println("popierdalacz 3000");
            moveBanner = true;
        } else if (score % 10 == 0 && score < 51) {
            pipeDown.y -= 20;
            pipeUp.y += 20;
            System.out.println("sie popiesci to sie zmiesci");
            moveBanner = true;
        }
    }

    /**
     * Drawing the textures of the game and main graphics.
     */
    protected void repaint(Graphics g) {
        g.drawImage(backgroundGraphic, 0, 0, null);
        g.drawImage(pipeUpGraphic, pipeUp.x, pipeUp.y, null);
        g.drawImage(pipeDownGraphic, pipeDown.x, pipeDown.y, null);
        g.drawImage(groundGraphic, 0, HEIGHT - groundGraphic.getHeight(null), null);
        g.drawImage(birdGraphic, bird.x, bird.y, null);

        if (inMenu) {
            g.setFont(arcadeFont.deriveFont(60f));
            g.drawString("Birdy     Flap", 36, 120);
            g.setFont(arcadeFont.deriveFont(20f));
            g.drawString("Press up or down to start", 110, 320);
            g.drawString("W-S   Up-Down   K-J", 185, 380);
            leaderboard(g);
        } else {
            g.setFont(arcadeFont.deriveFont(20f));
            g.drawString("lvl up!", banner.x, banner.y);
            g.setFont(arcadeFont.deriveFont(40f));
            g.drawString("PTS  " + score, 15, 50);

            birdMove();
        }

    }

    private void leaderboard(Graphics graphics) {        
        graphics.setFont(arcadeFont.deriveFont(20f));
        graphics.drawString("Leaderboard", 200, 450);
        graphics.setFont(arcadeFont.deriveFont(18f));

        Dictionary<String, Integer> a = parser.getDict();
        Enumeration<String> key = a.keys();
        
        int y = 520;
        for (int i = 0; i < 5; i += 1) {
            String keyy = key.nextElement();
            graphics.drawString(String.format("%2d. %s %2d", i + 1,
                    keyy, a.get(keyy)), 60, y);
            y += 20;
        }
        y = 520;

        for (int i = 5; i < 10; i += 1) {
            String keyy = key.nextElement();
            graphics.drawString(String.format("%2d. %s %2d", i + 1,
                    keyy, a.get(keyy)), 400, y);
            y += 20;
        }
    }

    private void up() {
        if (pipeDown.y <= 0) {
            return;
        }
        pipeDown.y -= 15;
        pipeUp.y -= 15;
    }

    private void down() {
        if (pipeDown.y >= HEIGHT + 200) {
            return;
        }
        pipeDown.y += 15;
        pipeUp.y += 15;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_UP)
                || (e.getKeyCode() == KeyEvent.VK_W)
                || (e.getKeyCode() == KeyEvent.VK_K)) {
            isPressedUp = true;
        }
        if ((e.getKeyCode() == KeyEvent.VK_DOWN)
                || (e.getKeyCode() == KeyEvent.VK_S)
                || (e.getKeyCode() == KeyEvent.VK_J)) {

            isPressedDown = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if ((e.getKeyCode() == KeyEvent.VK_UP)
                || (e.getKeyCode() == KeyEvent.VK_W)
                || (e.getKeyCode() == KeyEvent.VK_K)) {
            isPressedUp = false;
        }

        if ((e.getKeyCode() == KeyEvent.VK_DOWN)
                || (e.getKeyCode() == KeyEvent.VK_S)
                || (e.getKeyCode() == KeyEvent.VK_J)) {
            isPressedDown = false;
        }
    }

    public static void main(String[] args) {
        FlappyBird birdie = new FlappyBird();
    }
}