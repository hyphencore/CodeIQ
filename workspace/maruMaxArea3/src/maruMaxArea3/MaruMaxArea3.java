package maruMaxArea3;

import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
 * (1) ○×を含むリストとマップを生成する
 * (2) ○リストから3つを選ぶ（ただし、他の○×を含まないもの）
 * (3) 選択した○以外の○をすべて×にしマップ全体の有効エリアを求める
 * (4) 有効エリア内で、最大の面積（形状は長方形）を求める
 * (5) (2)～(4)を、取りうる3つの全○パターンで面積調査する。
 */

/*
 * Map Class: 2次元配列で各点の○×何も無しを状態保持するもの
 *   右方向が +x軸、下方向が +y軸 とする。A-Z、a-z を 0-25 で対応付ける
 */
class Map {
	static byte NONE = 0; // area各点の状態
	static byte MARU = 1;
	static byte BATSU = 2;

	static byte LEFT_UPPER = 0;	// 方向
	static byte UPPER = 1;
	static byte RIGHT_UPPER = 2;
	static byte RIGHT = 3;
	static byte RIGHT_LOWER = 4;
	static byte LOWER = 5;
	static byte LEFT_LOWER = 6;
	static byte LEFT = 7;
	byte[][] area;			// 26 x 26 のmap
	int xSize, ySize;

	// constructor
	Map(int xs, int ys) {
		area = new byte[ys][xs];
		xSize = xs;
		ySize = ys;
		for (int y=0 ; y<ySize ; y++)
			for (int x=0 ; x<xSize ; x++)
				area[y][x] = NONE;
	}

	// clear()
	public void clear() {
		for (int y=0 ; y<ySize ; y++)
			for (int x=0 ; x<xSize ; x++)
				area[y][x] = NONE;
	}

	// 現在のインスタンスのマップを内容コピーして返す
	public Map copy() {
		Map mp;

		mp = new Map(xSize, ySize);
		for (int y=0 ; y<ySize ; y++)
			for (int x=0 ; x<xSize ; x++)
				mp.area[y][x] = area[y][x];
		return mp;
	}

	// 指定座標を○セットする
	public void setMaru(int x, int y) {
		area[y][x] = MARU;
	}

	// 指定座標を×セットする
	public void setBatsu(int x, int y) {
		area[y][x] = BATSU;
	}

	/*
	 * setArea: 指定範囲内をstatの状態で埋める。
	 */
	public void setArea(int strX, int strY, int endX, int endY, byte stat) {
		int x, y;

		for (y=strY ; y<=endY ; y++)
			for (x=strX ; x<=endX ; x++)
				area[y][x] = stat;
	}

	/*
	 * getMaxAreaSize: 有効エリア内の最大面積を求める
	 */
	public int getMaxValidAreaSize() {
		int x, y, x2, xCount, yCount, strY, endY, maxS = 0;

		for (x=0 ; x<xSize ; x++) {
			xCount = 0; yCount = 0; strY = -1; endY = -1;
			for (y=0 ; y<ySize ; y++) {
				if (area[y][x] != BATSU) {
					if (strY < 0) strY = y;
					yCount++;
					endY = y;
				}
			}
			// 最大Y幅を保てるxの最大幅を求める
			if (yCount > 0) {
				for (x2=x ; x2<xSize ; x2++) {
					if ((area[strY][x2] != BATSU) && (area[endY][x2] != BATSU)) {
						xCount++;
					}
				}
				if (maxS < xCount*yCount) maxS = xCount*yCount;
			}
		}

		return maxS;
	}

	/*
	 * setInvalidArea: 指定した点（×）の各方向に対して無効エリアをセットする。
	 */
	public void setInvalidArea(Point pt, byte dir) {
		int strX = 0, strY = 0, endX = 0, endY = 0;

		if (dir == LEFT_UPPER) {
			endX = pt.x;
			endY = pt.y;
		} else if (dir == UPPER) {
			endX = xSize-1;
			endY = pt.y;
		} else if (dir == RIGHT_UPPER) {
			strX = pt.x;
			endX = xSize-1;
			endY = pt.y;
		} else if (dir == RIGHT) {
			strX = pt.x;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == RIGHT_LOWER) {
			strX = pt.x;
			strY = pt.y;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == LOWER) {
			strY = pt.y;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == LEFT_LOWER) {
			strY = pt.y;
			endX = pt.x;
			endY = ySize-1;
		} else {
			endX = pt.x;
			endY = ySize-1;
		}
		setArea(strX, strY, endX, endY, BATSU);
	}

	/*
	 * 半角→全角変換: debug print用に全て全角表示にする変換
	 */
	public String hankakuAlphabetToZenkakuAlphabet(String s) {
	    StringBuffer sb = new StringBuffer(s);
	    for (int i = 0; i < s.length(); i++) {
	    	char c = s.charAt(i);
	    	if (c >= 'a' && c <= 'z') {
	    		sb.setCharAt(i, (char)(c - 'a' + 'ａ'));
	        } else if (c >= 'A' && c <= 'Z') {
	        	sb.setCharAt(i, (char)(c - 'A' + 'Ａ'));
	        }
	    }
	    return sb.toString();
	}

	/*
	 * print(): debug print 現在のareaの状態を表示する
	 *
	 * 　ＡＢＣ・・・Ｚ
	 * ａ
	 * ｂ
	 * ｃ　○
	 * ：　　×
	 * ｚ　　　　　　○
	 *
	 * の表記印字
	 */
	public void print() {
		byte[] alphabet = new byte[26];
		byte[] alphabet2 = new byte[1];
		String alphabetStr;

		for (byte x=0 ; x<area.length ; x++) {
			alphabet[x] = (byte)(x + 65);
		}
		try {
			alphabetStr = new String(alphabet, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		System.out.print("　");
		System.out.println( hankakuAlphabetToZenkakuAlphabet(alphabetStr));

		for (int y=0 ; y<area.length ; y++) {
			alphabet2[0] = (byte)(y + 97);
			try {
				System.out.print( hankakuAlphabetToZenkakuAlphabet(new String(alphabet2, "US-ASCII")));
			} catch (UnsupportedEncodingException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return;
			}
			for (int x=0 ; x<area.length ; x++) {
				if (area[y][x] == NONE) System.out.print("　");
				if (area[y][x] == MARU) System.out.print("○");
				if (area[y][x] == BATSU) System.out.print("×");
			}
			System.out.println("");
		}
	}

}

class MaruMaxArea3 {
	public static int MAX_X_SIZE = 26;
	public static int MAX_Y_SIZE = 26;

	/*
	 * init:  入力リストから、○×リスト、Mapを生成する。
	 *        アルファベットを0-25のインデックス数値に変換
	 */
	public static void init(String[] mList, String[] bList,
			ArrayList<Point> maruList, ArrayList<Point> batsuList, Map m) {
		int i, xIndex, yIndex;
		byte[] wk;
		String pt;

		// ○リスト生成、マップに○セット
		maruList.clear();
		for (i=0 ; i<mList.length ; i++) {
			pt = mList[i];
			wk = pt.getBytes();
			xIndex = wk[0] - "A".getBytes()[0];
			yIndex = wk[1] - "a".getBytes()[0];
			maruList.add(new Point(xIndex, yIndex));
			m.setMaru(xIndex, yIndex);
		}

		// ×リスト生成、マップに×セット
		batsuList.clear();
		for (i=0 ; i<bList.length ; i++) {
			pt = bList[i];
			wk = pt.getBytes();
			xIndex = wk[0] - "A".getBytes()[0];
			yIndex = wk[1] - "a".getBytes()[0];
			batsuList.add(new Point(xIndex, yIndex));
			m.setBatsu(xIndex, yIndex);
		}
	}

	/*
	 * getMaxMaruAreaSizeBySelected: 選択された○を含む最大領域を求める
	 */
	public static int getMaxMaruAreaSizeBySelected(List<Point> maruList,
			List<Point> batsuList,
			List<Point> selectedMaruList,
			Map mp,
		int n,
		Point p0,
		Point p2) {
		Iterator<Point> it;
		Point pt;
		Map tmpMap = mp.copy(); // copy
		ArrayList<Point> tmpBatsuList = new ArrayList<Point>(batsuList.subList(0, batsuList.size()));

		// (1) 選択された○以外の○を×にマッピング
		it = maruList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			// 未選択○を×に
			if (!selectedMaruList.contains(pt)) {
				tmpMap.setBatsu(pt.x, pt.y);
				tmpBatsuList.add(pt);
			}
		}

		// (2) 無効エリアを×にマッピング
		it = tmpBatsuList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			if ((pt.x < p0.x) && (pt.y < p0.y)) tmpMap.setInvalidArea(pt, Map.LEFT_UPPER);
			if ((pt.x >= p0.x) && (pt.x <= p2.x) && (pt.y < p0.y)) tmpMap.setInvalidArea(pt, Map.UPPER);
			if ((pt.x > p2.x) && (pt.y < p0.y)) tmpMap.setInvalidArea(pt, Map.RIGHT_UPPER);
			if ((pt.x > p2.x) && (pt.y >= p0.y) && (pt.y <= p2.y)) tmpMap.setInvalidArea(pt, Map.RIGHT);
			if ((pt.x > p2.x) && (pt.y > p2.y)) tmpMap.setInvalidArea(pt, Map.RIGHT_LOWER);
			if ((pt.x >= p0.x) && (pt.x <= p2.x) && (pt.y > p2.y)) tmpMap.setInvalidArea(pt, Map.LOWER);
			if ((pt.x < p0.x) && (pt.y > p2.y)) tmpMap.setInvalidArea(pt, Map.LEFT_LOWER);
			if ((pt.x < p0.x) && (pt.y >= p0.y) && (pt.y <= p2.y)) tmpMap.setInvalidArea(pt, Map.LEFT);
		}
tmpMap.print();
		// (3) 有効エリア内の最大面積を求める
		return	tmpMap.getMaxValidAreaSize();
	}

	/*
	 * 選択○の最小のエリアを求める。左上をP0 右下をP2
	 */
	public static void getMinMaruAreaBySelected(List<Point> selectedMaruList,
			Point p0,
			Point p2) {
		Iterator<Point> it;
		Point pt;

		p0.x = Integer.MAX_VALUE;
		p0.y = Integer.MAX_VALUE;
		p2.x = -1;
		p2.y = -1;
		it = selectedMaruList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			if (p0.x > pt.x) p0.x = pt.x;
			if (p0.y > pt.y) p0.y = pt.y;
			if (p2.x < pt.x) p2.x = pt.x;
			if (p2.y < pt.y) p2.y = pt.y;
		}
	}

	/*
	 * 選択○以外の○×含有チェック。
	 */
	public static boolean isValiableSelectedMaru(List<Point> orgMaruList,
			List<Point> orgBatsuList,
			List<Point> selectedMaruList,
			Point p0,
			Point p2) {
		Iterator<Point> it;
		Point pt;

		// 選択以外の○含有チェック
		it = orgMaruList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			if (!selectedMaruList.contains(pt)) {
				if (pt.x >= p0.x && pt.x <= p2.x && pt.y >= p0.y && pt.y <= p2.y)
					return false;
			}
		}
		// 全ての×含有チェック
		it = orgBatsuList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			if (!selectedMaruList.contains(pt)) {
				if (pt.x >= p0.x && pt.x <= p2.x && pt.y >= p0.y && pt.y <= p2.y)
					return false;
			}
		}
		return true;
	}

	/*
	 * getMaxMaruAreaSize: 指定された数(n)の○を再帰的に選択する。
	 */
	public static int getMaxMaruAreaSize(List<Point> orgMaruList,		// 全○リスト
			List<Point> orgBatsuList,		// 全×リスト
			List<Point> restMaruList,	// 残りの○候補リスト
			List<Point> selectedMaruList,	// 選択した○リスト
			Map mp,						// 全○×を反映させた大元のマップ
			int n,						//
			int maxS) {					// 現時点での最大面積
		int retMaxS;
		Point p0, p2;

		if (selectedMaruList.size() == n) { // 必要数だけ○選択
			p0 = new Point(0, 0); p2 = new Point(0, 0);
			getMinMaruAreaBySelected(selectedMaruList, p0, p2);	// 選択○の最小エリアの左上P0,右下P2座標を求める
			if (isValiableSelectedMaru(orgMaruList, orgBatsuList, selectedMaruList, p0, p2)) {
				return getMaxMaruAreaSizeBySelected(orgMaruList, orgBatsuList, selectedMaruList, mp, n, p0, p2);
			} else { // 選択しきれてない
				return -1;
			}
		}
		if (restMaruList == null) return -1;		// 次に選択すべき○がない
		if (restMaruList.size() <= 0) return -1;

		/*
		 * restMaruListの先頭を採用する場合と、採用しない場合で再帰的に選択し、最大面積を調査する
		 * 採用
		 */
		selectedMaruList.add(restMaruList.get(0)); // ○リストの先頭を選択
		retMaxS = getMaxMaruAreaSize(orgMaruList,
				orgBatsuList,
				restMaruList.subList(1, restMaruList.size()),
				selectedMaruList,
				mp,
				n,
				maxS);
		if (maxS < retMaxS) maxS = retMaxS;	// 今までの最大面積を越えたらそれを採用
		selectedMaruList.remove(selectedMaruList.size()-1);	// ○リストから消す
		/*
		 * 採用しない
		 */
		retMaxS = getMaxMaruAreaSize(orgMaruList,
				orgBatsuList,
				restMaruList.subList(1, restMaruList.size()),
				selectedMaruList,
				mp,
				n,
				maxS);
		if (maxS < retMaxS) maxS = retMaxS;

		return maxS;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n, maxS;
		String[] mList, bList, ret;
		ArrayList<Point> maruList = new ArrayList<Point>(),
			batsuList = new ArrayList<Point>(),
			selectedMaruList = new ArrayList<Point>();
		Map mp = new Map(MAX_X_SIZE, MAX_Y_SIZE);

		while (cin.hasNext()) {
			mp.clear();	maruList.clear(); batsuList.clear(); selectedMaruList.clear();
			ret = cin.nextLine().split(" ");
			n = Integer.parseInt(ret[0]);
			mList = ret[1].split(","); bList = ret[2].split(",");
			init(mList, bList, maruList, batsuList, mp);
			mp.print();
			maxS = getMaxMaruAreaSize(maruList, batsuList, maruList, selectedMaruList, mp, n, 0);
			if (maxS <= 0) System.out.println("-");
			else System.out.println(maxS);
		}
		cin.close();
	}

}
