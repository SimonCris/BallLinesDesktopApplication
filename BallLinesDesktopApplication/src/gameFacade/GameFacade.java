package gameFacade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import gameConfig.GameConfig;
import grid_elements.Ball;
import grid_elements.Cell;
import grid_elements.GridElement;
import grid_elements.Star;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.languages.asp.IllegalTermException;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import move.Move;
import panels.GridLay;
import panels.ScorePanel;

//Facade  per la gestione del gioco
public class GameFacade extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GridLay panelGrid = new GridLay();
	private ScorePanel scorePanel = new ScorePanel();
	private DefaultDirectedGraph<GridElement, DefaultEdge> graph;

	// DLV Classes
	private DesktopHandler handler;
	private InputProgram inputProgram;
	private OptionDescriptor od;
	private File file;

	public GameFacade() throws ObjectNotValidException, IllegalAnnotationException {

		// DLV SET
		handler = new DesktopHandler(new DLVDesktopService("dlv.exe"));

		inputProgram = new ASPInputProgram();
		inputProgram.addFilesPath("rules.dl");
		handler.addProgram(inputProgram);

		file = new File("rules.dl");

		// Registra le classi di DLV
		registerClass();

		// FRAME SET
		setTitle("Ball Lines Game");

		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(panelGrid, BorderLayout.CENTER);
		add(scorePanel, BorderLayout.WEST);

		setResizable(false);
		setVisible(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

	}

	// Metodo che registra le classi in DLV
	private void registerClass() throws ObjectNotValidException, IllegalAnnotationException {

		ASPMapper.getInstance().registerClass(Cell.class);
		ASPMapper.getInstance().registerClass(Ball.class);
		ASPMapper.getInstance().registerClass(Star.class);
		ASPMapper.getInstance().registerClass(Move.class);

	}

	// Metodo che gestisce il gioco
	public void gamePlay() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ObjectNotValidException,
			IllegalAnnotationException, IllegalTermException {

		while (true) {

			// elimino il file creato in precedenza
			file.delete();

			Writer output;

			// Creo nuovamente il file rules.dl
			output = new BufferedWriter(new FileWriter("rules.dl", true));
			output.write("");

			// aggiungo i nuovi fatti
			for (int x = 0; x < GameConfig.ROW; x++) {

				for (int y = 0; y < GameConfig.COLUMN; y++) {

					if (panelGrid.getCelle()[x][y].getGridElement() instanceof Ball) {
						output.append("ball(" + x + "," + y + ","
								+ panelGrid.getCelle()[x][y].getGridElement().getColor() + ")." + "\n");
					}

					if (panelGrid.getCelle()[x][y].getGridElement() instanceof Star) {
						output.append("star(" + x + "," + y + ","
								+ panelGrid.getCelle()[x][y].getGridElement().getColor() + ")." + "\n");

					}

				}

			}

			// Appendo le regole
			String rules = "\n\n\n% Un intorno conta le palline dello stesso colore attorno ad una Ball\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y1,C),X1=X+1,Y1=Y+1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X,Y1,C),Y1=Y+1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X,Y1,C),Y1=Y-1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y,C),X1=X+1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y,C),X1=X-1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y1,C),X1=X-1,Y1=Y-1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y1,C),X1=X+1,Y1=Y-1.\r\n"
					+ "intorno(X,Y,C):-ball(X,Y,C),ball(X1,Y1,C),X1=X-1,Y1=Y+1.\r\n"
					+ "isolata(X,Y,C):- not intorno(X,Y,C),ball(X,Y,C)." + "\n" + "%GUESS \n"
					+ "move(X,Y,C1,C2)|notMove(X,Y,C1,C2):-ball(X,Y,C),possoMuovere(X,Y,C1,C2),cellaConPallineVicine(C1,C2,C,N).\r\n"
					+ "\n" + "%CHECK \n" + "%può esserci esattamente una sola mossa \n" + "\n"
					+ ":-#count{X,Y,C1,C2:move(X,Y,C1,C2)}>1.\r\n" + ":-#count{X,Y,C1,C2:move(X,Y,C1,C2)}<1.\r\n"
					+ "%non è possibile muovere una ball su un altra ball \n"
					+ ":-move(X,Y,C1,C2),ball(C1,C2,C).\r\n \n"

					+ "%WEAK\n" + "%Tendo a formare gruppi allineati i più lunghi possibili\n %e per me è la cosa più importante, quindi ha priorità maggiore\n "

					+ ":~ notMove(X,Y,C1,C2),cellaConPallineVicine(C1,C2,C,N),possoMuovere(X,Y,C1,C2),ball(X,Y,C). [N:1]\r\n"
					+ "%In genere non è possibile muovere una palla se ha vicino palline dello stesso colore, ma ha una priorità più bassa della regola precedente\n"
					+ ":~move(X,Y,C1,C2),not isolata(X,Y,C),ball(X,Y,C). [1:2]\n";

			// conto i colori nella griglia
			for (String color : panelGrid.calcolaColori()) {

				for (int x = 0; x < GameConfig.ROW; x++) {
					for (int y = 0; y < GameConfig.COLUMN; y++) {

						if (panelGrid.getCelle()[x][y].getGridElement() instanceof Cell
								|| panelGrid.getCelle()[x][y].getGridElement() instanceof Star) {
							output.append(calcolaAdiacenza(panelGrid.getCelle()[x][y].getGridElement(), color));
						}

					}
				}

			}

			// genero un grafo che rappresenta la griglia corrente
			makeGrapth();

			output.append(generaPossoMuovere());
			output.append(rules);
			output.close();

			// Lancio dlv
			Output o = handler.startSync();

			// Controlli
			AnswerSets answerSets = (AnswerSets) o;

			if (answerSets.getAnswersets().size() == 0) {
				JOptionPane.showMessageDialog(this,
						"NON CI SONO PIU' ANSWER SET \n" + "PUNTI TOTALIZZATI: "
								+ Integer.parseInt(scorePanel.getScoreLabel().getText()) + " \n" + "LIVELLO RAGGIUNTO: "
								+ scorePanel.getLevelNumber().getText());
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}

			AnswerSet as = answerSets.getAnswersets().get(0);
			try {
				for (Object obj : as.getAtoms()) {

					// Se c'è una mossa la eseguo
					if (obj instanceof Move) {

						int ballX = ((Move) obj).getBallX();
						int ballY = ((Move) obj).getBallY();
						int moveX = ((Move) obj).getMoveX();
						int moveY = ((Move) obj).getMoveY();

						// Calcolo il cammino per quella mossa

						DijkstraShortestPath<GridElement, DefaultEdge> movePath = new DijkstraShortestPath<>(graph);
						GraphPath<GridElement, DefaultEdge> GraphmovePath;

						// Prendo il Cammino
						GraphmovePath = movePath.getPath(panelGrid.getCelle()[ballX][ballY].getGridElement(),
								panelGrid.getCelle()[moveX][moveY].getGridElement());

						// Ingrandisco la pallina di partenza
						cambiaImmagineStartVertex((Move) obj);

						// VISUALIZZO IL PERCORSO CON DEI PUNTINI GRIGI
						for (GridElement g : GraphmovePath.getVertexList()) {
							int x = g.getRow();
							int y = g.getColumn();

							if (g != GraphmovePath.getStartVertex() && g != GraphmovePath.getEndVertex()) {

								if (!(panelGrid.getCelle()[x][y].getGridElement() instanceof Star))
									panelGrid.getCelle()[x][y].setIcon(new ImageIcon("resources/percorso.png"));

							}

						}

						// Coloro la cella finale del colore corrispondente
						cambiaImmagineEndVertex((Move) obj);

						final JOptionPane pane = new JOptionPane(new JLabel("CONTINUA!", JLabel.CENTER));
						final JDialog d = pane.createDialog((JFrame) null, "Vai Avanti");
						d.setLocation(10, 10);
						d.setVisible(true);

						// Muovo la pallina
						panelGrid.moveElement((Move) obj);

						// Ripristino le celle usate per il percorso e le rendo senza immagine
						for (GridElement g : GraphmovePath.getVertexList()) {
							int x = g.getRow();
							int y = g.getColumn();

							if (g != GraphmovePath.getStartVertex() && g != GraphmovePath.getEndVertex()) {

								panelGrid.getCelle()[x][y].setIcon(null);

							}

						}

						for (Star s : panelGrid.getStarList()) {
							if (s.getRow() == moveX && s.getColumn() == moveY)
								panelGrid.getStarList().remove(s);
						}

					}

				}

			} catch (Exception e) {
				// Handle Exception
			}

			// Per ogni star crea una ball nella stessa posizione
			panelGrid.createBallsFromStars();

			// Eseguo mossa e aggiorno punteggio e livello corrente
			int newScore = panelGrid.removeBallsAligned();

			// AGGIORNO IL LABEL LATERALE
			int oldScore = Integer.parseInt(scorePanel.getScoreLabel().getText());
			oldScore += newScore;
			scorePanel.getScoreLabel().setText(String.valueOf(oldScore));

			scorePanel.getLevelLabel().setText(panelGrid.removedLineCount(scorePanel.getLevelLabel()).toString());

			// GESTISCO IL LIVELLO SUCCESSIVO
			if (!scorePanel.getLevelNumber().getText().equals(panelGrid.getCurrentLevel().toString())) {
				panelGrid.firstInitBalls();
				final JOptionPane pane = new JOptionPane(new JLabel("LEVEL UP", JLabel.CENTER));
				final JDialog d = pane.createDialog((JFrame) null, "Vai Avanti");
				d.setLocation(10, 10);
				d.setVisible(true);

			}

			scorePanel.getLevelNumber().setText(panelGrid.getCurrentLevel().toString());

			// GESTISCO IL NON AVERE PIU' CELLE LIBERE VISUALIZZO
			// I PUNTI TOTALIZZATI E IL LIVELLO RAGGIUNTO
			if (!(panelGrid.celleLibere())) {
				JOptionPane.showMessageDialog(this,
						"NON CI SONO PIU' ANSWER SET \n" + "PUNTI TOTALIZZATI: "
								+ Integer.parseInt(scorePanel.getScoreLabel().getText()) + " \n" + "LIVELLO RAGGIUNTO: "
								+ scorePanel.getLevelNumber().getText());
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}

			// ALTRIMENTI VADO AVANTI E
			// Creo altri 3 stars random
			panelGrid.initStars();

		}
	}

	public void makeGrapth() {
		graph = new DefaultDirectedGraph<>(DefaultEdge.class);

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {

				graph.addVertex(panelGrid.getCelle()[i][j].getGridElement());

				if (i - 1 >= 0) {
					graph.addVertex(panelGrid.getCelle()[i - 1][j].getGridElement());

					if (!(panelGrid.getCelle()[i - 1][j].getGridElement() instanceof Ball)) {
						graph.addEdge(panelGrid.getCelle()[i][j].getGridElement(),
								panelGrid.getCelle()[i - 1][j].getGridElement());
					}

				}

				if (j - 1 >= 0) {
					graph.addVertex(panelGrid.getCelle()[i][j - 1].getGridElement());

					if (!(panelGrid.getCelle()[i][j - 1].getGridElement() instanceof Ball)) {
						graph.addEdge(panelGrid.getCelle()[i][j].getGridElement(),
								panelGrid.getCelle()[i][j - 1].getGridElement());
					}

				}

				if (i + 1 < GameConfig.ROW) {
					graph.addVertex(panelGrid.getCelle()[i + 1][j].getGridElement());

					if (!(panelGrid.getCelle()[i + 1][j].getGridElement() instanceof Ball)) {
						graph.addEdge(panelGrid.getCelle()[i][j].getGridElement(),
								panelGrid.getCelle()[i + 1][j].getGridElement());
					}

				}

				if (j + 1 < GameConfig.COLUMN) {
					graph.addVertex(panelGrid.getCelle()[i][j + 1].getGridElement());

					if (!(panelGrid.getCelle()[i][j + 1].getGridElement() instanceof Ball)) {
						graph.addEdge(panelGrid.getCelle()[i][j].getGridElement(),
								panelGrid.getCelle()[i][j + 1].getGridElement());
					}

				}

			}
		}

	}

	// Funzionche che scorre tutte le celle e calcola tutti possibili cammini

	public String generaPossoMuovere() {
		DijkstraShortestPath<GridElement, DefaultEdge> path = new DijkstraShortestPath<>(graph);
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < GameConfig.ROW; i++) {
			for (int j = 0; j < GameConfig.COLUMN; j++) {
				if (panelGrid.getCelle()[i][j].getGridElement() instanceof Ball) {

					for (int l = 0; l < GameConfig.ROW; l++) {
						for (int m = 0; m < GameConfig.COLUMN; m++) {

							if (!(panelGrid.getCelle()[l][m].getGridElement() instanceof Ball)) {

								if (path.getPath(panelGrid.getCelle()[i][j].getGridElement(),
										panelGrid.getCelle()[l][m].getGridElement()) != null) {

									builder.append("possoMuovere(" + i + "," + j + "," + l + "," + m + ").\n");

								}

							}

						}
					}

				}
			}

		}

		return builder.toString();
	}

	// PRESO UN GRID ELEMENT E IL SUO COLORE CALCOLA IL NUMERO
	// DI ELEMENTI ADIACENTI CON LO STESSO COLORE IN TUTTI I VERSI
	// RESTITUISCE UN FATTO CHE VIENE APPESO ALLE REGOLE.
	public String calcolaAdiacenza(GridElement c, String color) {

		StringBuilder builder = new StringBuilder();
		int cont = 0;

		int riga, colonna;

		// controllo a destra
		if ((c.getColumn() + 1 < GameConfig.COLUMN
				&& panelGrid.getCelle()[c.getRow()][c.getColumn() + 1].getGridElement() instanceof Ball)
				|| (c.getColumn() + 1 < GameConfig.COLUMN
						&& panelGrid.getCelle()[c.getRow()][c.getColumn() + 1].getGridElement() instanceof Star)) {

			for (int i = c.getColumn() + 1; i < GameConfig.COLUMN; i++) {

				if ((panelGrid.getCelle()[c.getRow()][i].getGridElement() instanceof Ball
						&& panelGrid.getCelle()[c.getRow()][i].getGridElement().getColor().equals(color))
						|| (panelGrid.getCelle()[c.getRow()][i].getGridElement() instanceof Star
								&& panelGrid.getCelle()[c.getRow()][i].getGridElement().getColor().equals(color))) {

					cont++;
				} else {
					break;
				}
			}

		}

		// controllo a sinistra
		if ((c.getColumn() - 1 >= 0
				&& panelGrid.getCelle()[c.getRow()][c.getColumn() - 1].getGridElement() instanceof Ball)
				|| (c.getColumn() - 1 >= 0
						&& panelGrid.getCelle()[c.getRow()][c.getColumn() - 1].getGridElement() instanceof Star)) {

			for (int i = c.getColumn() - 1; i >= 0; i--) {

				if ((panelGrid.getCelle()[c.getRow()][i].getGridElement() instanceof Ball
						&& panelGrid.getCelle()[c.getRow()][i].getGridElement().getColor().equals(color))
						|| (panelGrid.getCelle()[c.getRow()][i].getGridElement() instanceof Star
								&& panelGrid.getCelle()[c.getRow()][i].getGridElement().getColor().equals(color))) {

					cont++;
				} else {
					break;
				}
			}

		}

		builder.append("cellaConPallineVicine(" + c.getRow() + "," + c.getColumn() + "," + color + "," + cont
				+ "). %des sin.\n");
		cont = 0;

		// controllo sopra
		if ((c.getRow() - 1 >= 0
				&& panelGrid.getCelle()[c.getRow() - 1][c.getColumn()].getGridElement() instanceof Ball)
				|| (c.getRow() - 1 >= 0
						&& panelGrid.getCelle()[c.getRow() - 1][c.getColumn()].getGridElement() instanceof Star)) {

			for (int i = c.getRow() - 1; i >= 0; i--) {

				if ((panelGrid.getCelle()[i][c.getColumn()].getGridElement() instanceof Ball
						&& panelGrid.getCelle()[i][c.getColumn()].getGridElement().getColor().equals(color))
						|| (panelGrid.getCelle()[i][c.getColumn()].getGridElement() instanceof Star
								&& panelGrid.getCelle()[i][c.getColumn()].getGridElement().getColor().equals(color))) {

					cont++;
				} else {
					break;
				}
			}

		}

		// controllo sotto
		if ((c.getRow() + 1 < GameConfig.ROW
				&& panelGrid.getCelle()[c.getRow() + 1][c.getColumn()].getGridElement() instanceof Ball)
				|| (c.getRow() + 1 < GameConfig.ROW
						&& panelGrid.getCelle()[c.getRow() + 1][c.getColumn()].getGridElement() instanceof Star)) {

			for (int i = c.getRow() + 1; i < GameConfig.ROW; i++) {

				if ((panelGrid.getCelle()[i][c.getColumn()].getGridElement() instanceof Ball
						&& panelGrid.getCelle()[i][c.getColumn()].getGridElement().getColor().equals(color))
						|| (panelGrid.getCelle()[i][c.getColumn()].getGridElement() instanceof Star
								&& panelGrid.getCelle()[i][c.getColumn()].getGridElement().getColor().equals(color))) {

					cont++;
				} else {
					break;
				}
			}

		}

		builder.append("cellaConPallineVicine(" + c.getRow() + "," + c.getColumn() + "," + color + "," + cont
				+ ").% sop sott.\n");
		cont = 0;

		// alto a sinistra
		riga = c.getRow() - 1;
		colonna = c.getColumn() - 1;

		while ((riga >= 0 && colonna >= 0 && panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Ball
				&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))
				|| (riga >= 0 && colonna >= 0 && panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Star
						&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))) {

			cont++;
			riga--;
			colonna--;

		}

		// basso a destra
		riga = c.getRow() + 1;
		colonna = c.getColumn() + 1;

		while ((riga < GameConfig.ROW && colonna < GameConfig.COLUMN
				&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Ball
				&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))
				|| (riga < GameConfig.ROW && colonna < GameConfig.COLUMN
						&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Star
						&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))) {

			cont++;
			riga++;
			colonna++;

		}

		builder.append("cellaConPallineVicine(" + c.getRow() + "," + c.getColumn() + "," + color + "," + cont
				+ ").% altSx bassDx.\n");
		cont = 0;

		// alto a destra
		riga = c.getRow() - 1;
		colonna = c.getColumn() + 1;

		while ((riga >= 0 && colonna < GameConfig.COLUMN
				&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Ball
				&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))
				|| (riga >= 0 && colonna < GameConfig.COLUMN
						&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Star
						&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))) {

			cont++;
			riga--;
			colonna++;

		}

		// basso a sinistra
		riga = c.getRow() + 1;
		colonna = c.getColumn() - 1;

		while ((riga < GameConfig.ROW && colonna >= 0
				&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Ball
				&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))
				|| (riga < GameConfig.ROW && colonna >= 0
						&& panelGrid.getCelle()[riga][colonna].getGridElement() instanceof Star
						&& panelGrid.getCelle()[riga][colonna].getGridElement().getColor().equals(color))) {

			cont++;
			riga++;
			colonna--;

		}

		builder.append("cellaConPallineVicine(" + c.getRow() + "," + c.getColumn() + "," + color + "," + cont
				+ "). %altDx bassSx.\n");

		return builder.toString();
	}

	public void cambiaImmagineStartVertex(Move obj) {

		int ballX = obj.getBallX();
		int ballY = obj.getBallY();

		if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("blue")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallBlu.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("red")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallRossa.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("yellow")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallGialla.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("black")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallNera.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("purple")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallViola.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("green")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallVerde.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("orange")) {
			panelGrid.getCelle()[ballX][ballY].setIcon(new ImageIcon("resources/moveBallArancione.png"));

		}

	}

	public void cambiaImmagineEndVertex(Move obj) {

		int moveX = obj.getMoveX();
		int moveY = obj.getMoveY();
		int ballX = obj.getBallX();
		int ballY = obj.getBallY();

		if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("blue")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoBlu.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("red")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoRosso.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("yellow")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoGiallo.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("black")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoNero.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("purple")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoViola.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("green")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoVerde.png"));

		} else if (panelGrid.getCelle()[ballX][ballY].getGridElement().getColor().equals("orange")) {
			panelGrid.getCelle()[moveX][moveY].setIcon(new ImageIcon("resources/sfondoArancione.png"));

		}
	}

}
