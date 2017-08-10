package sosuuretsu;

import java.util.Scanner;

public class Sosuuretsu {

	/*
	 * isSosuu: 指定nが素数か判定
	 */
	public static boolean isSosuu(int n) {
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

	/*
	 * makeSosuuString: m～nまでの素数を並べた文字列を返す
	 */
	public static String makeSosuuString(int m, int n) {
		String ret = "";

		if (m == 2) ret = "2";
		if (m % 2 == 0) m++;
		for (int i=m ; i<=n ; i+=2) {
			if (isSosuu(i)) ret = ret.concat(Integer.toString(i));
		}
		return ret;
	}

	/*
	 * getMaxSosuuwa: sosuuで示された素数文字列で、最大maxKeta文字(題は140文字)の
	 *                中での最大和(0～9で挟まるもの)を求める
	 */
	public static int getMaxSosuuwa(String srcStr, int maxKeta) {
		int fIndex, lIndex, max = -1, wa;
		String curStr, restStr;
		byte[] sosuuArray;

		for (int i=0 ; i<=9 ; i++) {
			restStr = srcStr;

			do {
				fIndex = restStr.indexOf(Integer.toString(i));		// 検索対象文字列の最初にiが現れる位置
				if (fIndex >= 0) {	// 対象の数字が見つかった
					curStr = restStr.substring(fIndex);

					// 見つかった場所から最大文字数分の文字列を獲得する
					if (curStr.length() >= maxKeta) curStr = curStr.substring(0, maxKeta);

					// 文字列の最後に現れるiの位置を獲得
					lIndex = curStr.lastIndexOf(Integer.toString(i));

					if (lIndex > 0) { // 最初以外の位置でiが見つかった
						sosuuArray = curStr.substring(0, lIndex+1).getBytes();
						wa = 0;
						for (int j=0 ; j<sosuuArray.length ; j++) wa += (sosuuArray[j] - "0".getBytes()[0]);
						if (max < wa) max = wa;
					}
					// 次の残り検索文字列を作る(fIndex+1から後ろのrestを作る)
					restStr = restStr.substring(fIndex+1);
				}
			} while (fIndex >= 0);
		}
		return max;
	}

	/*
	 * getMaxSosuuwa: sosuuで示された素数文字列で、最大maxKeta文字(題は140文字)の
	 *                中での最大和(0～9で挟まるもの)を求める. noncopy version
	 */
	public static int getMaxSosuuwa2(String srcStr, int maxKeta) {
		int fIndex, lIndex, max = -1, wa;
		String numStr;
		byte[] sosuuArray;

		for (int i=0 ; i<=9 ; i++) {
			numStr = Integer.toString(i);
			fIndex = -1;
			do {
				fIndex = srcStr.indexOf(numStr, fIndex+1);		// 検索対象文字列の最初にiが現れる位置
				if (fIndex >= 0) {	// 対象の数字が見つかった
					if (srcStr.length() - fIndex - 1 < maxKeta) lIndex = srcStr.length() - 1;
					else lIndex = fIndex + maxKeta -1;

					lIndex = srcStr.lastIndexOf(numStr, lIndex);
					if (fIndex != lIndex) { // fIndex でないiが見つかった
						sosuuArray = srcStr.substring(fIndex, lIndex+1).getBytes();
						wa = 0;
						for (int j=0 ; j<sosuuArray.length ; j++) wa += (sosuuArray[j] - "0".getBytes()[0]);
						if (max < wa) max = wa;
					}
				}
			} while (fIndex >= 0);
		}
		return max;
	}

	public static void main(String[] args) {
		String line, sosuu;
		String[] ret;
		int m, n;

		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			line = cin.nextLine();
			ret = line.split(" ");
			m = Integer.parseInt(ret[0]);
			n = Integer.parseInt(ret[1]);
			sosuu = makeSosuuString(m, n);
			System.out.println(getMaxSosuuwa2(sosuu, 140));
		}
		cin.close();
	}

}
