package flapper;

import java.awt.Graphics;

import javax.swing.JPanel;

public class loseDisplay extends JPanel{

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        loseDialog.loseDialog.repaint(g);
    }

    
}

