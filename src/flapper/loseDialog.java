package flapper;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class loseDialog {
	private int score;
	private JDialog dialog;
	private final ImageIcon backgroundGraphicTemp = new ImageIcon("src/flapper/resources/goes_hard.png");
    Image backgroundGraphic = backgroundGraphicTemp.getImage();
	public loseDisplay display;
	public static loseDialog loseDialog;


	public  loseDialog(){
		dialog = new JDialog();
		display = new loseDisplay();

		loseDialog = this;
		dialog.add(display);
		GridLayout experimentLayout = new GridLayout(7,3);
		dialog.add(display);

		dialog.setLayout(experimentLayout);

		dialog.add(new JTextField("dfadfas", 5));
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setSize(300, 400);
		dialog.setLocationRelativeTo(FlappyBird.flappyBird.frame);
		display.repaint();
        //open it
	}
	public void repaint(Graphics g){
		g.drawImage(backgroundGraphic, 0, 0, null);
	}

	public void setScore(int score){
		this.score = score;
	}

	/** Returns name of the person
	 * @return String with name of winner (or loser)
	 */
	public String getName(){

		return "janusz korwin m.";
	}

	public void show(){
		dialog.setTitle("score: " + score);
		dialog.setVisible(true);
	}

}
