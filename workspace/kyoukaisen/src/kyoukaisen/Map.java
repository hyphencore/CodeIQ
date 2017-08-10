package kyoukaisen;

public class Map {
	int xSize, ySize;	//　このMapの縦横サイズ
	Piece[][] ps; 		// (x, y), 左上が(0, 0)としたときの各Piece保持配列

	// 行と列単位の有無マップを作る。この方が行、列単位で一気にチェックできる。入力と同じ考え方。
	//　16進数2桁分で1行または1列分を保持する
	int[] RowEnable, ColumnEnable;

	public enum Direction {Top, Left, Bottom, Right};	//　各Pieceの辺

	// constructor
	Map(int xs, int ys) {
		int i, j;
		ps = new Piece[xs][ys];
		for (i=0 ; i<xs ; i++) {
			for (j=0 ; j<ys ; j++) {
				ps[i][j] = new Piece(false);
			}
		}
		ColumnEnable = new int[xs];
		RowEnable = new int[ys];
		for (i=0 ; i<xs ; i++) ColumnEnable[i] = 0;
		for (j=0 ; j<ys ; j++) RowEnable[j] = 0;
		xSize = xs;
		ySize = ys;
	}

	// 1行分の数字をセット
	public void setLineEnable(int line, int y) {
		int x, bit, mask;
		for (x=0 ; x<xSize ; x++) { // lineからpieceへの分解
			mask = 0x0001;
			bit = (line >> (xSize-x-1)) & mask;
			if (bit == 1) this.setEnable(x, y);
		}
	}

	// 1pieceだけの有りセット。行と列のマップも同時に生成する。
	public void setEnable(int x, int y) {
		ps[x][y].setEnable();
		RowEnable[y] = RowEnable[y] | (1 << (xSize-x-1));
		ColumnEnable[x] = ColumnEnable[x] | (1 << (ySize-y-1));
	}

	// 1pieceだけの無しセット。行と列のマップも同時に生成する。
	public void setDisable(int x, int y) {
		ps[x][y].setDisable();
		RowEnable[y] = RowEnable[y] & (~(1 << (xSize-x-1)));
		ColumnEnable[x] = ColumnEnable[x] & (~(1 << (ySize-y-1)));
	}

	// 指定方向に隣接するpieceが存在しているかを返す。1つずつのチェックは不要だが念のため（未使用）
	public boolean isContiguous(int x, int y, Direction d) {
		if (d == Direction.Top) {
			if (y < 1) return true;
			return ps[x][y-1].isEnable();
		}
		if (d == Direction.Left) {
			if (x < 1) return true;
			return ps[x-1][y].isEnable();
		}
		if (d == Direction.Bottom) {
			if (y >= ySize-1) return true;
			return ps[x][y+1].isEnable();
		}
		if (d == Direction.Right) {
			if (x >= xSize-1) return true;
			return ps[x+1][y].isEnable();
		}
		return false;
	}

	// 1行分まとめてカウント。yのPieceのDirection隣接チェックする
	public int countRowContiguous(int y, Direction d) {
		int i, count = 0;
		int lineBit = 0;

		if (d == Direction.Top) {
			if (y == 0) return 0; // 最上段は0
			lineBit = RowEnable[y] & (RowEnable[y] ^ RowEnable[y-1]);
		} else if (d == Direction.Bottom) {
			if (y == ySize-1) return 0; // 最下段は0
			lineBit = RowEnable[y] & (RowEnable[y] ^ RowEnable[y+1]);
		}

		// そのlineの1の数を数える
		for (i=0 ; i<xSize ; i++) {
			if ((lineBit & 0x0001) == 1) count++;
			lineBit = lineBit >> 1;
		}
		return count;
	}

	// 1列分まとめてカウント。x列のPieceのRight隣接チェックする
	public int countColumnContiguous(int x, Direction d) {
		int i, count = 0;
		int lineBit = 0;

		if (d == Direction.Left) {
			if (x == 0) return 0; // 最左は0
			lineBit = ColumnEnable[x] & (ColumnEnable[x] ^ ColumnEnable[x-1]);
		} else if (d == Direction.Right) {
			if (x == xSize-1) return 0; // 最右は0
			lineBit = ColumnEnable[x] & (ColumnEnable[x] ^ ColumnEnable[x+1]);
		}

		// その列の1の数を数える
		for (i=0 ; i<ySize ; i++) {
			if ((lineBit & 0x0001) == 1) count++;
			lineBit = lineBit >> 1;
		}
		return count;
	}

	// 指定辺の数をカウントする
	public int countContiguous(Direction d) {
		int i;
		int count = 0;

		if (d == Direction.Top || d == Direction.Bottom) {
			for (i=0 ; i<ySize ; i++) count += countRowContiguous(i, d);
		} else {
			for (i=0 ; i<xSize ; i++) count += countColumnContiguous(i, d);
		}
		return count;
	}
}
