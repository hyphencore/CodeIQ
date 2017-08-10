package wasosuu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * SosuuwaCache: 素数和の結果を保持するクラス
 */
class SosuuwaCache {
	boolean[] done;
	int[][] result;	// [残数][素因数最小値条件]
	Sosuu sosuu;

	SosuuwaCache(int max, Sosuu s) {
		result = new int[max][max];
		done = new boolean[max];
		for (int i=0 ; i<max ; i++) {
			for (int j=0 ; j<max ; j++)
				result[i][j] = 0;
			done[i] = false;
		}

		// n=2 .. n=4までは事前に生成
		if (max >= 2) {
			done[1] = true;
			result[1][1] = 1;
		}
		if (max >= 3) {
			done[2] = true;
			result[2][1] = 1;
			result[2][2] = 1;
		}
		if (max >= 4) {
			done[3] = true;
			result[3][1] = 1;
		}

		sosuu = s;
	}

	/*
	 * makeCacheSosuuwa: 素因数の和が n になる、全パターンをキャッシュする。
	 *                   result配列には、[素因数の和合計][使用できる素因数の最小値]=ﾊﾟﾀｰﾝ数　を格納
	 *
	 * 例) n=5 のとき、2+3 と 5 の2パターンとなるため、
	 *     result[5(-1)][2(素因数2以上OK)-1] = 2
	 *     result[5(-1)][3(素因数3以上OK)-1] = 1
	 *     result[5(-1)][5(素因数5以上OK)-1] = 1
	 *     が生成される。素因数を2以上使って良いなら、2+3, 5 があり得るため、2パターンになる。
	 *     素因数を3以上使って良い場合は、5のみのため1パターン、5も同様。6以上は存在しないので0
	 *     
	 *     この結果を作ることで、素因数m以上を使用条件にしたとき、残りnを作るためのﾊﾟﾀｰﾝが result[n-1][m-1]で
	 *     求まる。また、結果も流用できる。
	 */
	public void makeCacheSosuuwa(int n, int str, int end, List<Integer> sosuuArray) {
		int tmp, rest, p;

		/*
		 * 残りnで素因数str以上使ったパターンがすでに結果あり
		 */
		if (done[n-1]) {
			return;
		}

		// 以下、残りnで素因数 2～Int(n/2)とn(素数のとき)以上を条件とするときのそれぞれのパターンをカウント
		for (int i=0 ; i<sosuuArray.size() ; i++) {
			if (n/2 < sosuuArray.get(i)) break; // 必要な数を超えた
			p = sosuuArray.get(i);
			rest = n - p;
			if (!done[rest-1]) makeCacheSosuuwa(rest, 2, rest, sosuuArray); // 無ければ生成
			tmp = result[rest-1][p-1];
			result[n-1][p-1] = tmp;
		}

		// 自分自身
		if (sosuu.isSosuu(n)) result[n-1][n-1] = 1;

		// 各素因数の数値以上を使用して良いときのカウントを生成
		for (int i=sosuuArray.size()-1 ; i>0 ; i--) {
			result[n-1][sosuuArray.get(i-1)-1] += result[n-1][sosuuArray.get(i)-1];
		}
		done[n-1] = true;
	}

	public int countSosuuwa(int n, int str, int end, List<Integer> sosuuArray) {
		if (n == 0) {
			return 1; // 発見
		}
		if (n < str) return 0; // 残りのnに対して使用できる素因数がない
		if (sosuuArray.size() <= 0) return 0;

		if (!done[n-1]) {	// キャッシュないなら作る
			makeCacheSosuuwa(n, 2, n, sosuuArray);
		}

		return result[n-1][str-1];
	}

}

/*
 * 素数クラス
 */
class Sosuu {
	ArrayList<Integer> sosuu;
	int maxSosuu;

	/*
	 * Constructor: max までの素数を生成する
	 */
	Sosuu(int max) {
		makeSosuuArray(2, max);
		maxSosuu = max;
	}

	/*
	 * 素数列を返す
	 */
	public ArrayList<Integer> getArray() {
		return sosuu;
	}

	/*
	 * makeSosuuArray: str - end の範囲の素数を生成
	 */
	protected void makeSosuuArray(int str, int end) {
		sosuu = new ArrayList<Integer>();

		for (int i=0 ; i<end-str+1 ; i++) {
			if (isSosuu(str+i)) {
				sosuu.add(str+i);
			}
		}
	}

	/*
	 * isSosuu: 指定nが素数か判定
	 */
	public boolean isSosuu(int n) {
		int max = (int)Math.sqrt(n);
		int i;

		if (n < 2) return false;
		if (n == 2) return true;
		if (n % 2 == 0) return false;
		for (i=3 ; i<=max ; i+=2) {
			if (n % i == 0) return false;
		}
		return true;
	}
}

public class WaSosuu {
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n;
		int[] counted;
		Sosuu s;
		SosuuwaCache sc;

		while (cin.hasNext()) {
			n = cin.nextInt();
			counted = new int[n];
			for (int i=0 ; i<n ; i++) counted[i] = 0;
			s = new Sosuu(n);
			sc = new SosuuwaCache(n, s);
			System.out.println(sc.countSosuuwa(n, 2, n, s.getArray().subList(0, s.getArray().size())));
		}
		cin.close();
	}

}
