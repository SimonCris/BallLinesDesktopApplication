package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameConfig.GameConfig;

public class ScorePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BufferedImage scorePanelBackground;
	private JButton scoreLabel;
	private JButton scoreTitle;
	private JButton levelLabel;
	private JButton levelTitle;
	private JButton levelNumberTitle;
	private JButton levelNumber;

	public ScorePanel() {



		this.setPreferredSize(new Dimension(400, 600));
		// this.add(scoreLabel);
		this.setBackground(Color.BLACK);

		this.setLayout(new GridLayout(6, 1, 6, 6));

		scoreLabel = new JButton("0");
		scoreTitle = new JButton(new ImageIcon("resources/Score.png"));
		levelLabel = new JButton("Level Value");
		levelTitle = new JButton(new ImageIcon("resources/LinesToDelete.png"));
		levelNumberTitle = new JButton(new ImageIcon("resources/CurrentLevel.png"));
		levelNumber = new JButton("Level Number");

		scoreTitle.setForeground(Color.RED);
		scoreTitle.setVisible(true);
		this.add(scoreTitle);

		scoreLabel.setForeground(Color.RED);
		scoreLabel.setText("0");
		scoreLabel.setFont(new Font("Engravers Mt", Font.PLAIN, 40));
		scoreLabel.setVisible(true);
		this.add(getScoreLabel());

		levelTitle.setForeground(Color.RED);
		levelTitle.setVisible(true);
		this.add(levelTitle);

		levelLabel.setForeground(Color.RED);
		levelLabel.setVisible(true);
		levelLabel.setFont(new Font("Engravers Mt", Font.PLAIN, 40));
		levelLabel.setText(GameConfig.LEVELLINES.toString());
		this.add(levelLabel);
		
		levelNumberTitle.setForeground(Color.RED);
		levelNumberTitle.setVisible(true);
		this.add(levelNumberTitle);
		
		levelNumber.setForeground(Color.RED);
		levelNumber.setText("1");
		levelNumber.setFont(new Font("Engravers Mt", Font.PLAIN, 40));
		levelNumber.setVisible(true);
		this.add(levelNumber);

		this.setVisible(true);

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.drawImage(scorePanelBackground, 0, 0, this);

	}

	public JButton getScoreTitle() {
		return scoreTitle;
	}

	public void setScoreTitle(JButton scoreTitle) {
		this.scoreTitle = scoreTitle;
	}

	public JButton getLevelLabel() {
		return levelLabel;
	}

	public void setLevelLabel(JButton levelLabel) {
		this.levelLabel = levelLabel;
	}

	public JButton getLevelTitle() {
		return levelTitle;
	}

	public void setLevelTitle(JButton levelTitle) {
		this.levelTitle = levelTitle;
	}

	public JButton getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(JButton scoreLabel) {
		this.scoreLabel = scoreLabel;
	}

	public JButton getLevelNumberTitle() {
		return levelNumberTitle;
	}

	public void setLevelNumberTitle(JButton levelNumberTitle) {
		this.levelNumberTitle = levelNumberTitle;
	}

	public JButton getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(JButton levelNumber) {
		this.levelNumber = levelNumber;
	}

}
