package squareArea;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * OneSquare: 1つのマスを管理するクラス。これを利用したMapは処理が重くなるため未使用とした。
 */
class OneSquare {
	int height;

	OneSquare() { height = 0; }
	OneSquare(int height) { this.height = height;}
	void init() { height = 0; }
	void init(int height) { this.height = height;}
	void set(int height) { this.height = height;}
	int getHeight() { return height; };
}

/*
 * OneSquareのArrayListで確保しながら処理すると相当重いため配列に変更した。map2
 */
class Map {
	ArrayList<OneSquare> map;
	int[] map2;

	int n;
	int h;

	Map() {
		n = 0;
		h = 0;
	}

	Map(int n) {
		h = 0;
		make(n);
	}

	Map(int n, int h) {
		make(n, h);
	}

	void make(int n) {
		this.n = n;
		this.h = 0;
		map2 = new int[n*n];
	}

	void make(int n, int h) {
		this.n = n;
		this.h = h;
		map2 = new int[n*n];
	}

	void init() {
		for (int i=0 ; i<n*n ; i++)
			map2[i] = 0;
	}

	void init(int h) {
		for (int i=0 ; i<n*n ; i++)
			map2[i] = h;
	}

	void resize(int n) {
		make(n);
	}

	void resize(int n, int h) {
		make(n, h);
	}

	// map間のコピー
	void copy(Map src) {
		for (int i=0 ; i<src.n*n ; i++) map2[i] = src.map2[i];
	}

	/*
	 * 題提示の生成アルゴリズム
	 */
	void genBoard(int n, int h) {
		int r=1, r0, r1, r2, r3, r4;
		int max = (n < 100) ? n : 100;	// max 100
		int sqrX, sqrY, sqrW, sqrH, brdH;

		make(n, h);
		init(1);

		for (int i=0 ; i<max ; i++) {
			r = (r % 10009) * 99991;
			r0 = r;
			r = (r % 10009) * 99991;
			r1 = r;
			r = (r % 10009) * 99991;
			r2 = r;
			r = (r % 10009) * 99991;
			r3 = r;
			r = (r % 10009) * 99991;
			r4 = r;

			sqrX = r0 % n;
			sqrY = r1 % n;
			sqrW = r2 % (n - sqrX) % 100;
			sqrH = r3 % (n - sqrY) % 100;
			brdH = (r4 % h) + 1;

			for (int x = sqrX ; x < sqrX + sqrW ; x++) {
				for (int y = sqrY ; y<sqrY + sqrH ; y++) {
					map2[x+y*n] = brdH;
				}
			}
		}
	}

	void print() {
		for (int y=0 ; y<n ; y++) {
			for (int x=0 ; x<n ; x++) {
				System.out.print(map2[x+y*n]);
				if (x != n-1) System.out.print(",");
			}
			System.out.println("");
		}
	}

	/*
	 * k以外の数値は0に、kの数値は1に
	 */
	void mask(int k) {
		for (int i=0 ; i<n*n ; i++)
			if (map2[i] != k)
				map2[i] = 0;
			else
				map2[i] = 1;
	}

	/*
	 * アルゴリズムの肝: 動的計画法にて求める。O(n^2)程度。
	 *   したがって、n<=3000だから最大9,000,000の計算量。これが数値1～hでh<=9なので最大81,000,000の計算量
	 *
	 * (1) 1～hまで(2)以下繰り返す(=k)
	 * (2) 大元のMap(以下Map)をコピーする(Work Map、以下MapWk)
	 * (3) MapWkに対して、k以外の数値は0に、kの数値は1にセットする。これは初期化の操作。
	 * (4) MapWkに対して、左上から右下に向かって、右方向をx、下方向をyとしたとき、
	 *      全ての点(x,y)について、(x,y)がMap上でkでない場合は0、
	 *      kのときは、(x-1, y), (x, y-1), (x-1, y-1) の地点の最小値に1加算した値を(x, y)の数値としてセットする
	 * (5) (4)でセットされた最大値を記憶・更新しておく
	 *
	 * 以上により求まった最大値が求めるべき最大面積の1辺の長さとなる。
	 * したがって、その2乗が求めるべき面積。
	 */
	int getMaxSquareArea() {
		int maxArea = 0;
		Map MapWk = new Map(this.n, this.h);

		for (int k=1 ; k<=h ; k++) {			// (1)
			MapWk.copy(this);	// (2)

			MapWk.mask(k);		// (3)

			// (4)
			for (int y=1 ; y<n ; y++) {
				for (int x=1 ; x<n ; x++) {
					if (map2[x+y*n] != k)
						MapWk.map2[x+y*n] = 0;
					else {
						MapWk.map2[x+y*n] =
								Math.min(MapWk.map2[x-1+y*n],
										Math.min(MapWk.map2[x+(y-1)*n], MapWk.map2[x-1+(y-1)*n]))+1;
						maxArea = Math.max(maxArea, MapWk.map2[x+y*n]);
					}
				}
			}
		}

		return maxArea*maxArea;
	}
}

class SquareArea {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		ArrayList<String> lines = new ArrayList<String>();
		String[] ret;
		int n, h;

		Map map;

		while (cin.hasNext()) {
			lines.add(cin.nextLine());
		}
		cin.close();

		for (int i=0 ; i<lines.size() ; i++) {
			ret = lines.get(i).split(",");
			n = Integer.parseInt(ret[0]);
			h = Integer.parseInt(ret[1]);

			map = new Map();

			map.genBoard(n, h);

			System.out.println(map.getMaxSquareArea());
		}
	}

}
