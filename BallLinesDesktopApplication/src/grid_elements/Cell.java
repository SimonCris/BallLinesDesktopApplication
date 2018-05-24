package grid_elements;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cell")
public class Cell extends GridElement {

	@Param(0)
	private int row;

	@Param(1)
	private int column;

	public Cell() {
	}

	public Cell(int r, int c) {

		this.row = r;
		this.column = c;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		return "Cell( " + getRow() + " , " + getColumn() + ")";

	}

	@Override
	public Integer getRow() {
		// TODO Auto-generated method stub
		return this.row;
	}

	@Override
	public void setRow(Integer r) {
		// TODO Auto-generated method stub
		this.row = r;
	}

	@Override
	public Integer getColumn() {
		// TODO Auto-generated method stub
		return this.column;
	}

	@Override
	public void setColumn(Integer c) {
		// TODO Auto-generated method stub
		this.column = c;
	}

	@Override
	public String getColor() {
		return super.getColor();
	}

	@Override
	public void setColor(String c) {
		// TODO Auto-generated method stub
		super.setColor(c);
	}

}
