package move;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

// Classe che gestisce la mossa di una pallina in una determinata cella
@Id("move")
public class Move {

	// Coordinata X della palla da muovere
	@Param(0)
	private int ballX;

	// Coordinata Y della palla da muovere
	@Param(1)
	private int ballY;

	// Coordinata X della nuova cella in cui muovere la palla
	@Param(2)
	private int moveX;

	// Coordinata Y della nuova cella in cui muovere la palla
	@Param(3)
	private int moveY;

	public Move() {

	}

	public Move(int ballX, int ballY, int moveX, int moveY) {

		this.ballX = ballX;
		this.ballY = ballY;
		this.moveX = moveX;
		this.moveY = moveY;

	}

	@Override
	public String toString() {

		return "Move( " + getBallX() + " , " + getBallY() + " , " + getMoveX() + " , " + getMoveY() + ")";

	}

	public int getBallX() {
		return ballX;
	}

	public void setBallX(int ballX) {
		this.ballX = ballX;
	}

	public int getBallY() {
		return ballY;
	}

	public void setBallY(int ballY) {
		this.ballY = ballY;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

}
