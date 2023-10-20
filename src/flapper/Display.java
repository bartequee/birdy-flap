package flapper;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Display extends JPanel {
    // private static final long serialVersionID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
    }

    
}
