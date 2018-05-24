package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import button.Button;
import gameConfig.GameConfig;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.ButtonUI;

import grid_elements.Ball;
import grid_elements.Cell;
import grid_elements.GridElement;
import grid_elements.Star;
import move.Move;

public class GridLay extends JPanel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Matrice di bottoni
	private Button[][] celle;

	private ArrayList<Star> starList = new ArrayList<Star>();

	private ArrayList<Button> buttonsRemoveMovedBalls;
	private Integer removedLinesCounter;
	private Integer numberOfColor;
	private Integer currentLevel;

	public GridLay() throws HeadlessException {

		super();

		buttonsRemoveMovedBalls = new ArrayList<Button>();
		setRemovedLinesCounter(0);
		setNumberOfColor(2);
		currentLevel = 1;

		celle = new Button[GameConfig.ROW][GameConfig.COLUMN];

		// Assegno al panel un GridLayout
		this.setLayout(new GridLayout(GameConfig.ROW, GameConfig.COLUMN, 1, 1));

		// Inizializzo la griglia
		for (int x = 0; x < GameConfig.ROW; x++) {

			for (int y = 0; y < GameConfig.COLUMN; y++) {
				// Creo un nuovo bottone nella matrice
				celle[x][y] = new Button(this, x, y);
				celle[x][y].setPreferredSize(new Dimension(20, 20));
				celle[x][y].setEnabled(true);

				celle[x][y].setGridElement(new Cell(x, y));

				// Aggiungo il bottone al panel
				this.add(celle[x][y]);

			}
		}

		firstInitBalls();
		initStars();

		this.setBackground(Color.BLACK);
		setVisible(true);

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

	}

	// Metodo che svuota la griglia
	public void clear() {
		for (int x = 0; x < GameConfig.ROW; x++) {

			for (int y = 0; y < GameConfig.COLUMN; y++) {

				// Aggiungo il bottone al panel
				celle[x][y].setIcon(null);

			}

		}

	}

	// Metodo che muove la pallina ricevuta in input una Move
	public void moveElement(Move move) {

		

		// prendo la pallina in posizione x, y
		GridElement movingBall = celle[move.getBallX()][move.getBallY()].getGridElement();

		// svuoto le celle dove era contenuta la pallina
		celle[move.getBallX()][move.getBallY()].setIcon(null);
		celle[move.getBallX()][move.getBallY()].setGridElement(new Cell(move.getBallX(), move.getBallY()));

		movingBall.setRow(move.getMoveX());
		movingBall.setColumn(move.getMoveY());
		// sposto la pallina e la inserisco nella nuova posizione
		celle[move.getMoveX()][move.getMoveY()].setGridElement(movingBall);

	}

	// Metodo che inizializza random gli asterischi in funzione del livello
	public void initStars() {

		for (int i = 0; i < 3; i++) {
			int x, y, col;
			// x,y,col sono posizione e colore della star che vado a posizionare
			x = (int) (Math.random() * 8);
			y = (int) (Math.random() * 8);
			col = (int) (Math.random() * numberOfColor);

			while (!(celle[x][y].getGridElement() instanceof Cell)) {
				// se la cella x,y è già occupata da un'altra pallina cambio

				x = (int) (Math.random() * 9);
				y = (int) (Math.random() * 9);
			}

			// set
			Star s = new Star(x, y, randomCol(col));
			starList.add(s);
			celle[x][y].setGridElement(s);
		}

	}

	// Metodo che inizializza le palline
	public void firstInitBalls() {
		for (int i = 0; i < 5; i++) {
			int x, y, col;

			// x,y,col sono posizione e colore della ball che vado a posizionare

			x = (int) (Math.random() * 8);
			y = (int) (Math.random() * 8);
			col = (int) (Math.random() * numberOfColor);

			while (!(celle[x][y].getGridElement() instanceof Cell)) {
				// se la cella x,y è già occupata da un'altra pallina cambio
				x = (int) (Math.random() * 9);
				y = (int) (Math.random() * 9);
			}

			// set
			celle[x][y].setGridElement(new Ball(x, y, randomCol(col)));

		}
	}

	// Metodo che controlla se ci sono 5 o piu palline orizzontalmente
	public int CheckHorizontalBalls() {

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				if (celle[i][j].getGridElement() instanceof Ball) {
					removeBallsInHorizontalLine((Ball) celle[i][j].getGridElement());
				}
			}
		}

		return 0;

	}

	// Metodo che controlla se ci sono 5 o piu palline verticalmente
	public int CheckVerticalBalls() {

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				if (celle[i][j].getGridElement() instanceof Ball) {
					removeBallsInVerticalLine((Ball) celle[i][j].getGridElement());
				}
			}
		}

		return 0;

	}

	// Metodo che controlla se ci sono 5 o piu palline sulla diagonale principale
	public int CheckPrincipalDiagonalBalls() {

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				if (celle[i][j].getGridElement() instanceof Ball) {
					removeBallPrincipalDiagonalLine(((Ball) celle[i][j].getGridElement()));
				}
			}
		}

		return 0;

	}

	// Metodo che controlla se ci sono 5 o piu palline sulla diagonale principale
	public int CheckSecondaryDiagonalBalls() {

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				if (celle[i][j].getGridElement() instanceof Ball) {
					removeBallSecondaryDiagonalLine(((Ball) celle[i][j].getGridElement()));
				}
			}
		}

		return 0;

	}

	// Metodo che rimuove le palline controllando in tutte le direzioni e ritorna il
	// punteggio della mossa
	public Integer removeBallsAligned() {

		CheckSecondaryDiagonalBalls();
		CheckHorizontalBalls();
		CheckPrincipalDiagonalBalls();
		CheckVerticalBalls();
		return removeBallsfromButtons();

	}

	// Metodo che posizione una pallina al posto di una cella
	public void createBallsFromStars() {

		for (Star s : starList) {

			celle[s.getRow()][s.getColumn()].setGridElement(new Ball(s.getRow(), s.getColumn(), s.getColor()));

		}

		starList.clear();
	}

	/*
	 * Funzione che presa una pallina, valuta la sua riga e cerca di rimuovere 4 o
	 * più palline dello stesso colore sulla sua riga
	 */
	public void removeBallsInHorizontalLine(Ball b) {
		ArrayList<Button> buttonsFromSingleBall = new ArrayList<Button>();
		int x = b.getRow();
		int y = b.getColumn();
		while (celle[x][y].getGridElement() instanceof Ball
				&& (celle[x][y].getGridElement().getColor().equals(b.getColor()))) {

			buttonsFromSingleBall.add(celle[x][y]);

			if (y < GameConfig.COLUMN - 1) {
				y++;
			} else
				break;
		}

		// Se trova una linea da rimuovere, inserisce i buttons nella lista dei
		// rimovibili
		// e incrementa il contatore delle linee rimosse
		if (buttonsFromSingleBall.size() >= 4) {

			for (Button button : buttonsFromSingleBall) {
				buttonsRemoveMovedBalls.add(button);

			}

			removedLinesCounter++;

		} else
			buttonsFromSingleBall.clear();
	}

	/*
	 * Funzione che presa una pallina, valuta la sua riga e cerca di rimuovere 4 o
	 * più palline dello stesso colore sulla sua colonna
	 */
	public void removeBallsInVerticalLine(Ball b) {
		ArrayList<Button> buttonsFromSingleBall = new ArrayList<Button>();

		int x = b.getRow();
		int y = b.getColumn();
		while (celle[x][y].getGridElement() instanceof Ball
				&& (celle[x][y].getGridElement().getColor().equals(b.getColor()))) {

			buttonsFromSingleBall.add(celle[x][y]);

			if (x < GameConfig.ROW - 1) {
				x++;
			} else
				break;
		}

		// Se trova una linea da rimuovere, inserisce i buttons nella lista dei
		// rimovibili
		// e incrementa il contatore delle linee rimosse
		if (buttonsFromSingleBall.size() >= 4) {

			for (Button button : buttonsFromSingleBall) {
				buttonsRemoveMovedBalls.add(button);
			}

			removedLinesCounter++;

		} else
			buttonsFromSingleBall.clear();
	}

	/*
	 * Funzione che presa una pallina, valuta la sua diagonale principale e cerca di
	 * rimuovere 4 o più palline dello stesso colore sulla sua diagonale
	 */
	public void removeBallPrincipalDiagonalLine(Ball b) {
		ArrayList<Button> buttonsFromSingleBall = new ArrayList<Button>();

		int x = b.getRow();
		int y = b.getColumn();
		while (celle[x][y].getGridElement() instanceof Ball
				&& (celle[x][y].getGridElement().getColor().equals(b.getColor()))) {

			buttonsFromSingleBall.add(celle[x][y]);

			if (y < GameConfig.COLUMN - 1 && x < GameConfig.ROW - 1) {
				y++;
				x++;
			} else
				break;
		}

		// Se trova una linea da rimuovere, inserisce i buttons nella lista dei
		// rimovibili
		// e incrementa il contatore delle linee rimosse
		if (buttonsFromSingleBall.size() >= 4) {

			for (Button button : buttonsFromSingleBall) {
				buttonsRemoveMovedBalls.add(button);

			}

			removedLinesCounter++;

		} else
			buttonsFromSingleBall.clear();
	}

	/*
	 * Funzione che presa una pallina, valuta la sua diagonale secondaria e cerca di
	 * rimuovere 4 o più palline dello stesso colore sulla sua diagonale
	 */
	public void removeBallSecondaryDiagonalLine(Ball b) {
		ArrayList<Button> buttonsFromSingleBall = new ArrayList<Button>();

		int x = b.getRow();
		int y = b.getColumn();
		while (celle[x][y].getGridElement() instanceof Ball
				&& (celle[x][y].getGridElement().getColor().equals(b.getColor()))) {

			buttonsFromSingleBall.add(celle[x][y]);

			if (y > 0 && x < GameConfig.ROW - 1) {
				y--;
				x++;
			} else
				break;
		}

		// Se trova una linea da rimuovere, inserisce i buttons nella lista dei
		// rimovibili
		// e incrementa il contatore delle linee rimosse
		if (buttonsFromSingleBall.size() >= 4) {

			for (Button button : buttonsFromSingleBall) {
				buttonsRemoveMovedBalls.add(button);
			}

			removedLinesCounter++;

		} else
			buttonsFromSingleBall.clear();
	}

	/*
	 * Prendo la lista di tutti i bottoni nei quali devo eliminare le Balls e
	 * rimetto delle celle Vuote, prendendo la posizione dal button corrente.
	 */
	public Integer removeBallsfromButtons() {

		Integer score;

		for (Button b : buttonsRemoveMovedBalls) {
			int x = b.getGridElement().getRow();
			int y = b.getGridElement().getColumn();
			b.setGridElement(new Cell(x, y));

			b.setIcon(null);

		}

		score = buttonsRemoveMovedBalls.size() * GameConfig.BALLVALUE;

		buttonsRemoveMovedBalls.clear();
		return score;

	}

	// Svuoto la griglia
	public void clearGrid() {

		for (int x = 0; x < GameConfig.ROW; x++) {

			for (int y = 0; y < GameConfig.COLUMN; y++) {
				// Creo un nuovo bottone nella matrice

				celle[x][y].setGridElement(new Cell(x, y));
				celle[x][y].setIcon(null);

			}
		}

	}

	// Aggiorna le linee rimosse e se ho raggiunto un nuovo livello incremento i
	// colori
	// e svuoto la griglia
	public Integer removedLineCount(JButton levelLabel) {

		Integer levelNumber = Integer.parseInt(levelLabel.getText());
		levelNumber -= removedLinesCounter;

		if (levelNumber <= 0) {

			levelNumber = 10;

			if (numberOfColor < 7) {
				numberOfColor++;
				currentLevel++;
			}

			clearGrid();

		}

		removedLinesCounter = 0;

		return levelNumber;

	}

	public String randomCol(int x) {

		switch (x) {
		case 0:
			return "blue";
		case 1:
			return "red";
		case 2:
			return "yellow";
		case 3:
			return "green";
		case 4:
			return "orange";
		case 5:
			return "purple";
		case 6:
			return "black";

		}

		return null;

	}

	public Set<String> calcolaColori() {

		Set<String> colori = new HashSet<String>();

		for (int x = 0; x < GameConfig.ROW; x++) {
			for (int y = 0; y < GameConfig.COLUMN; y++) {

				if (celle[x][y].getGridElement() instanceof Ball) {

					colori.add(celle[x][y].getGridElement().getColor());

				}

			}
		}

		return colori;

	}

	public Button[][] getCelle() {
		return celle;
	}

	public void setCelle(Button[][] celle) {
		this.celle = celle;
	}

	public Integer getRemovedLinesCounter() {
		return removedLinesCounter;
	}

	public void setRemovedLinesCounter(Integer removedLinesCounter) {
		this.removedLinesCounter = removedLinesCounter;
	}

	public Integer getNumberOfColor() {
		return numberOfColor;
	}

	public void setNumberOfColor(Integer numberOfColor) {
		this.numberOfColor = numberOfColor;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				s.append(celle[i][j].getGridElement().toString() + "<->");
			}
			s.append("\n");
		}
		return s.toString();

	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	public ArrayList<Star> getStarList() {
		return starList;
	}

	public void setStarList(ArrayList<Star> starList) {
		this.starList = starList;
	}

	public boolean celleLibere() {

		int cont = 0;

		for (int x = 0; x < GameConfig.ROW; x++) {
			for (int y = 0; y < GameConfig.COLUMN; y++) {

				if (celle[x][y].getGridElement() instanceof Cell)
					cont++;

				if (cont >= 3)
					return true;

			}
		}

		return false;
	}

}
