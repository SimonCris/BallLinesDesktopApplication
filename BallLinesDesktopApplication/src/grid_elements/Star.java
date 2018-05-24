package grid_elements;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("star")
public class Star extends GridElement {

	@Param(0)
	private int row;
	@Param(1)
	private int column;
	@Param(2)
	private String color;


	public Star(int r, int c, String ballColor) {
		this.row = r;
		this.column = c;
		this.color = ballColor;

		
	}

	public Star() {

	}

	@Override
	public Integer getRow() {
		return this.row;
	}

	@Override
	public void setRow(Integer r) {
		this.row = r;

	}

	@Override
	public Integer getColumn() {
		return this.column;
	}

	@Override
	public void setColumn(Integer c) {
		this.column = c;
	}

	@Override
	public String getColor() {
		return this.color;
	}

	@Override
	public void setColor(String c) {
		this.color = c;
	}

	@Override
	public String toString() {

		return "Star( " + getRow() + " , " + getColumn() + ")";

	}

	
}