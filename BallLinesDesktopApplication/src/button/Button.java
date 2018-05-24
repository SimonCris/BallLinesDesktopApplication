package button;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import grid_elements.Ball;
import grid_elements.Cell;
import grid_elements.GridElement;
import grid_elements.Star;
import panels.GridLay;

public class Button extends JButton implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean giaPremuto = false;

	// elemento del gioco che si interfaccerà con dlv
	private GridElement gridElement;

	public Button(GridLay l, int x, int y) {
		this.setEnabled(true);
		this.addMouseListener(this);
		
		gridElement = new Cell(x,y);

	}

	public Color getColor() {
		return this.getBackground();
	}

	public void setColor(Color color) {
		this.setBackground(color);
	}

	public ImageIcon getImage() {
		return this.getImage();
	}



	public Boolean getGiaPremuto() {
		return giaPremuto;
	}

	public void setGiaPremuto(Boolean giaPremuto) {
		this.giaPremuto = giaPremuto;
	}

	public GridElement getGridElement() {
		return gridElement;
	}

	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
		this.setGridElementIcon(gridElement);

	}

	public void setGridElementIcon(GridElement elem) {

		if (elem instanceof Ball) {
			setImageFromBall(elem);
		} else if (elem instanceof Star)
			setImageFromStar(elem);

	}

	public void setImageFromBall(GridElement elem) {
		switch (elem.getColor()) {
		case "blue":
			this.setIcon(new ImageIcon("resources/pallinaBlu.png"));
			break;
		case "red":
			this.setIcon(new ImageIcon("resources/pallinaRossa.png"));
			break;
		case "green":
			this.setIcon(new ImageIcon("resources/pallinaVerde.png"));
			break;
		case "yellow":
			this.setIcon(new ImageIcon("resources/pallinaGialla.png"));
			break;
		case "orange":
			this.setIcon(new ImageIcon("resources/pallinaArancione.png"));
			break;
		case "black":
			this.setIcon(new ImageIcon("resources/pallinaNera.png"));
			break;
		case "purple":
			this.setIcon(new ImageIcon("resources/pallinaViola.png"));
			break;

		}
	}

	public void setImageFromStar(GridElement elem) {
		switch (elem.getColor()) {
		case "blue":
			this.setIcon(new ImageIcon("resources/asteriscoBlu.png"));
			break;
		case "red":
			this.setIcon(new ImageIcon("resources/asteriscoRosso.png"));
			break;
		case "green":
			this.setIcon(new ImageIcon("resources/asteriscoVerde.png"));
			break;
		case "yellow":
			this.setIcon(new ImageIcon("resources/asteriscoGiallo.png"));
			break;
		case "orange":
			this.setIcon(new ImageIcon("resources/asteriscoArancione.png"));
			break;
		case "black":
			this.setIcon(new ImageIcon("resources/asteriscoNero.png"));
			break;
		case "purple":
			this.setIcon(new ImageIcon("resources/asteriscoViola.png"));
			break;

		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
