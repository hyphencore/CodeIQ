package kyoukaisen;

public class Piece {
	boolean exist;


	Piece() {
		exist = false;
	}

	Piece(boolean e) {
		exist = e;
	}

	public void setEnable() {
		exist = true;
	}

	public void setDisable() {
		exist = false;
	}

	public boolean isEnable() {
		return exist;
	}

	public boolean isDisable() {
		return !exist;
	}
}
