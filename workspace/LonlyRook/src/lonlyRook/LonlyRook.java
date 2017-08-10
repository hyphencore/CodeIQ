package lonlyRook;

import java.util.Scanner;

public class LonlyRook {
	// Combination nCr
	public static long Combination(int n, int r) {
		int nl, nu;
		long ret;

		if ((r == 0)||(n == 0)) return 1;

		r = (n - r < r ? n - r : r);

		ret = 1;
		for (nu = n-r+1, nl = 1; nl <= r ; nu++, nl++) {
			ret *= nu;	// 分子側は数の大きい方から掛ける
			ret /= nl;	// 分母側は数の小さい方から割る
		}
		return ret;
	}

	/*
	 * 指定マップでのLonlyなRookをカウントする
	 */
	public static int countLonlyRook(int n, int k, boolean[][] m) {
		int x, y, findIndex, rcount, ccount;
		int count = 0;

		/*
		 * 1行ずつ、ルークが何個存在するかカウントし、1個ならその列にルークが1個のみであれば、カウントする。
		 */
		for (y=0 ; y<n ; y++) {
			rcount = 0; findIndex = -1;
			for (x=0 ; x<n ; x++) if (m[y][x]) {
				rcount++;
				findIndex = x;
			}
			if (rcount == 1) { // 現在のy行にはLonlyRookがいる
				ccount = 0;
				for (int y2=0 ; y2<n ; y2++) {
					if (m[y2][findIndex]) ccount++;
				}
				if (ccount == 1) count++; // Lonly Rook を発見した
			}
		}

		return count;
	}

	/*
	 * 次の配置を生成する。マップの末尾から検索して、最初に1マスずらせるルークを探す
	 * 1つも次のマスに移動できるルークがなくなれば終了
	 */
	public static boolean setNextPlace(int n, int k, boolean[][] m) {
		int cannotMoveRook = 0;

		if (m[n-1][n-1]) cannotMoveRook++;

		for (int i=n*n-2 ; i>=0 ; i--) {
			int x, y, preX, preY;
			x = i % n;
			y = i / n;
			preX = (i+1) % n;	// i に対して次のマス座標
			preY = (i+1) / n;
			if (m[y][x] && !m[preY][preX]) {	// 今の位置にルークが居て、次の位置にルークが居ない場合は移動
				m[preY][preX] = true;
				m[y][x] = false;
				/*
				 * 移動できなかったルーク分を連続で再配置し、末尾までクリアする
				 */
				for (int j=i+2 ; j<i+2+cannotMoveRook ; j++) {
					x = j % n;
					y = j / n;
					m[y][x] = true;
				}
				/*
				 * 再配置された末尾からマップ末尾までをクリアする
				 */
				for (int j=i+2+cannotMoveRook ; j<n*n ; j++) {
					x = j % n;
					y = j / n;
					m[y][x] = false;
				}
				return true;
			} else if (m[y][x] && m[preY][preX]) {
				cannotMoveRook++; // 移動できなかったルークの数をカウントする
			}
		}
		return false;
	}

	/*
	 * n x n のマスにk個を上段から並べる初期配置
	 */
	public static void initPlace(int n, int k, boolean[][] m) {
		for (int i=0 ; i<k ; i++) {
			int x, y;
			x = i % n;
			y = i / n;
			m[y][x] = true;
		}
	}

	/*
	 * 課題のF関数
	 */
	public static double F(int n, int k) {
		boolean[][] map;
		double count = 0;

		map = new boolean[n][n];
		initPlace(n, k, map);
		do {
			count += countLonlyRook(n, k, map);
		} while(setNextPlace(n, k, map));

		return count/(double)Combination(n*n, k);
	}

	/*
	 * 課題のG関数
	 */
	public static double G(int n, int m) {
		double result = 0;

		for (int i=1 ; i<=m ; i++) {
			result += F(n, i);
		}

		return result;
	}

	/*
	 * 課題のH関数
	 */
	public static int H(int n, int m) {
		return (int)Math.floor(G(n, m) * 1000);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String[] ret;
		int n, m;

		while (cin.hasNext()) {
			cin.useDelimiter("[;\r\n]+");
			ret = cin.nextLine().split(" ");
			n = Integer.parseInt(ret[0]);
			m = Integer.parseInt(ret[1]);
			System.out.println(H(n, m));
		}
		cin.close();
	}

}
