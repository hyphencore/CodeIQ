package maruMaxArea;

import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
 * 全体の戦略
 *
 * (1) 入力より、○×を含むリストとマップを生成する
 * (2) ○リストから、3つを選ぶ（ただし、その3つで構成するエリア内に他の○×がないもの）
 * (3) 選択した○以外の○をすべて×として扱い、マップ全体での有効エリアを求める※1：有効エリアの考え方は以下記述
 * (4) 有効エリア内で、最大の面積（形状は長方形）を求める
 * (5) (2)～(4)を、取りうる3つの全○パターンで面積調査し、最大面積を出力する。
 *
 * 有効エリアについて
 * 　選択した3つの○を含み、かつ、×を含まないようなエリアを考えたとき、
 * どうやっても含むことのできない無効エリアが存在する。その無効エリア以外を有効エリアと定義する。
 * 有効エリアは、必ず1つの連続したエリアになり、分断されることはない。
 *
 * 無効エリアの求め方
 * 　選択した3つの○で生成される最小エリアの左上をP0、時計回りに頂点をP1,P2,P3としたとき、
 *  P0より左かつ上に×があれば、その×から左上のエリアが無効エリア
 *  　　：
 *  P3より下かつP0.x～P1.yの範囲に×があれば、その×から下の全xが無効エリア
 *　以上で無効エリアが生成され、残りが有効エリアになる。
 *
 * 有効エリア内の面積最大の求め方 O(n^2: 全体エリアのx座標数に依存。26×26なので大した量ではない)
 *  最左から順番に最右まで以下xを移動させながら走査
 *  最大面積0初期値とする。
 *  (1) そのx座標における有効y座標範囲を求める。
 *  (2) x+1以降で、そのy座標範囲が有効な最大xを求める
 *  (3) (1)のy長さ×xから最大xまでの長さで面積を出す
 *  (4) (3)の面積が最大面積となれば最大面積として採用
 *
 */

/*
 * Map Class: 2次元配列で各点の○×何も無しを状態保持するもの
 *
 * area 座標の取り方
 *   右方向が +x軸、下方向が +y軸 とする。A-Z、a-z を 0-25 で対応付ける
 *
 */
class Map {
	// area各点の状態
	static final byte NONE = 0;
	static final byte MARU = 1;
	static final byte BATSU = 2;

	// 方向
	static final byte LEFT_UPPER = 0;
	static final byte UPPER = 1;
	static final byte RIGHT_UPPER = 2;
	static final byte RIGHT = 3;
	static final byte RIGHT_LOWER = 4;
	static final byte LOWER = 5;
	static final byte LEFT_LOWER = 6;
	static final byte LEFT = 7;

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
	 *
	 * strX: 開始座標（左上）のx
	 * strY: 開始座標（左上）のy
	 * endX: 終了座標（右下）のx
	 * endY: 終了座標（右下）のy
	 * stat: Map.NONE or Map.MARU or Map.BATSU
	 */
	public void setArea(int strX, int strY, int endX, int endY, byte stat) {
		int x, y;

		for (y=strY ; y<=endY ; y++)
			for (x=strX ; x<=endX ; x++)
				area[y][x] = stat;
	}

	/*
	 * getMaxAreaSize: 有効エリア内の最大面積を求める
	 *
	 * 前提: インスタンスのarea各点が無効エリアセットされていること。
	 *
	 * 引数: なし
	 */
	public int getMaxValidAreaSize() {
		int x, y, x2, xCount, yCount, strY, endY, maxS = 0;

		for (x=0 ; x<xSize ; x++) {
			// 現在のx座標の有効エリアにあるY座標の上下端を求める。同時に、有効エリアの最大Y幅も求める
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
	 *
	 * 各方向により、×で埋める範囲を決める。
	 *
	 * 左上方向なら、×から左上の座標すべてを埋めるため、(0,0)～(x, y) を埋める
	 * 上方向なら、×から上のすべてを埋めるため、(0,0)～(26, y) を埋める。。。等
	 */
	public void setInvalidArea(Point pt, byte dir) {
		int strX, strY, endX, endY;

		if (dir == LEFT_UPPER) {
			strX = 0;
			strY = 0;
			endX = pt.x;
			endY = pt.y;
		} else if (dir == UPPER) {
			strX = 0;
			strY = 0;
			endX = xSize-1;
			endY = pt.y;
		} else if (dir == RIGHT_UPPER) {
			strX = pt.x;
			strY = 0;
			endX = xSize-1;
			endY = pt.y;
		} else if (dir == RIGHT) {
			strX = pt.x;
			strY = 0;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == RIGHT_LOWER) {
			strX = pt.x;
			strY = pt.y;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == LOWER) {
			strX = 0;
			strY = pt.y;
			endX = xSize-1;
			endY = ySize-1;
		} else if (dir == LEFT_LOWER) {
			strX = 0;
			strY = pt.y;
			endX = pt.x;
			endY = ySize-1;
		} else {
			strX = 0;
			strY = 0;
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

public class MaruMaxArea {
	public static int MAX_X_SIZE = 26;
	public static int MAX_Y_SIZE = 26;

	/*
	 * init:  入力リストから、○リストと×リスト、○×に基づくMapを生成する。
	 *        そのとき、アルファベットを0-25のインデックス数値に変換する
	 *
	 * in: String mList, bList
	 * out: ArrayList maruList, batsuList, m
	 */
	public static void init(String[] mList, String[] bList,
			ArrayList<Point> maruList, ArrayList<Point> batsuList, Map m) {
		int i, xIndex, yIndex;
		byte[] wk;
		String pt;

		// ○リスト生成、マップに○の箇所をセット
		maruList.clear();
		for (i=0 ; i<mList.length ; i++) {
			pt = mList[i];
			wk = pt.getBytes();
			xIndex = wk[0] - "A".getBytes()[0];
			yIndex = wk[1] - "a".getBytes()[0];
			maruList.add(new Point(xIndex, yIndex));
			m.setMaru(xIndex, yIndex);
		}

		// ×リスト生成、マップに×の箇所をセット
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
	 *
	 * ○を含む最小領域からみて、各方向に存在する×を元に無効エリアを決定し、
	 * 残った有効エリアの最大面積を求める。
	 *
	 * 無効エリア：例えば、○を含む最小エリアから左上にある×は、
	 * 　　　　　その×から左上の領域は含み得ないため全て×にする
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
		Map tmpMap = mp.copy(); // src original は残しておくため作業用MAPとしてコピー
		ArrayList<Point> tmpBatsuList = new ArrayList<Point>(batsuList.subList(0, batsuList.size()));

		// (1) 選択された○以外の○を×にマッピングし、新たに×リストを生成する
		it = maruList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			// 選択されていない○なら×にする
			if (!selectedMaruList.contains(pt)) {
				tmpMap.setBatsu(pt.x, pt.y);
				tmpBatsuList.add(pt);
			}
		}

		// (2) 無効エリアを×にマッピング。それぞれの×が、○含みの最小領域からどの方向にあるか決定して無効化する
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

		// (3) 有効エリア内の最大面積を求める
		return	tmpMap.getMaxValidAreaSize();
	}

	/*
	 * 選択した○を含む最小の□エリアを求める。
	 * 左上をP0、右下をP2として、それぞれの点座標をセットする。
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
	 * 選択した○以外の○×が、選択○のエリアに含まれているかチェック。
	 * 含まれているとfalse、含まれていないとtrue
	 */
	public static boolean isValiableSelectedMaru(List<Point> orgMaruList,
											List<Point> orgBatsuList,
											List<Point> selectedMaruList,
											Point p0,
											Point p2) {
		Iterator<Point> it;
		Point pt;

		// 選択以外の○が含まれているかチェック
		it = orgMaruList.iterator();
		while (it.hasNext()) {
			pt = it.next();
			if (!selectedMaruList.contains(pt)) {
				if (pt.x >= p0.x && pt.x <= p2.x && pt.y >= p0.y && pt.y <= p2.y)
					return false;
			}
		}

		// 全ての×について含まれているかチェック
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
	 *
	 *　選択のアルゴリズムとしては、
	 *　選択候補にある先頭○を選択/未選択の2パターンで、
	 *　選択した○リストの次の候補から、再度再帰的に選択させる。
	 *　指定数選択したら、その○パターンでの最大面積を求め返す。
	 *　再帰的に全パターンの最大面積を求める。
	 */
	public static int getMaxMaruAreaSize(List<Point> orgMaruList,		// 全○リスト
										List<Point> orgBatsuList,		// 全×リスト
										List<Point> restMaruList,	// 残りの○候補リスト
										List<Point> selectedMaruList,	// 選択した○リスト
										Map mp,						// 全○×を反映させた大元のマップ
										int n,						// 入力された○の必要数
										int maxS) {					// 現時点での最大面積
		int retMaxS;
		Point p0, p2;

		if (selectedMaruList.size() == n) { // 必要数だけ○選択されたら、最大面積調査
			p0 = new Point(0, 0);
			p2 = new Point(0, 0);
			getMinMaruAreaBySelected(selectedMaruList, p0, p2);	// 選択○の最小エリアの左上P0,右下P2座標を求める
			// 求めたP0、P2の長方形領域に、他の○×を含まないかチェック
			if (isValiableSelectedMaru(orgMaruList, orgBatsuList, selectedMaruList, p0, p2)) {
				// 選択した○で生成できる最大領域を求める
				return getMaxMaruAreaSizeBySelected(orgMaruList, orgBatsuList, selectedMaruList, mp, n, p0, p2);
			} else { // 選択しきれてない
				return -1;
			}
		}
		if (restMaruList == null) return -1;		// 次に選択すべき○がない＝現在の選択パターンは無し
		if (restMaruList.size() <= 0) return -1;	// 同上

		/*
		 * restMaruListの先頭を採用する場合と、採用しない場合で再帰的に選択し、最大面積を調査する
		 *
		 * 採用する場合
		 */
		selectedMaruList.add(restMaruList.get(0)); // ○リストの先頭を選択
		retMaxS = getMaxMaruAreaSize(orgMaruList,
									orgBatsuList,
									restMaruList.subList(1, restMaruList.size()),
									selectedMaruList,
									mp,
									n,
									maxS); // 採用した場合で、次の○リスト要素以降から再帰選択開始
		if (maxS < retMaxS) maxS = retMaxS;	// 今までの最大面積を越えたらそれを採用
		selectedMaruList.remove(selectedMaruList.size()-1);	// ○リストから消す

		/*
		 * 採用しない場合
		 */
		retMaxS = getMaxMaruAreaSize(orgMaruList,
									orgBatsuList,
									restMaruList.subList(1, restMaruList.size()),
									selectedMaruList,
									mp,
									n,
									maxS); // 採用しない場合で、次の○リスト要素以降から再帰選択開始
		if (maxS < retMaxS) maxS = retMaxS;	// 今までの最大面積を越えたらそれを採用

		return maxS; // 今まで求めた最大サイズを返す
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n, maxS;
		String line;
		String[] mList, bList, ret;
		ArrayList<Point> maruList = new ArrayList<Point>();
		ArrayList<Point> batsuList = new ArrayList<Point>();
		Map mp = new Map(MAX_X_SIZE, MAX_Y_SIZE);
		ArrayList<Point> selectedMaruList = new ArrayList<Point>();

		while (cin.hasNext()) {
			mp.clear();
			maruList.clear();
			batsuList.clear();
			selectedMaruList.clear();
			line = cin.nextLine();
			ret = line.split(" ");
			n = Integer.parseInt(ret[0]);
			mList = ret[1].split(",");
			bList = ret[2].split(",");
			init(mList, bList, maruList, batsuList, mp);
			maxS = getMaxMaruAreaSize(maruList, batsuList, maruList, selectedMaruList, mp, n, 0);
			if (maxS <= 0) System.out.println("-");
			else System.out.println(maxS);
		}
		cin.close();
	}

}
