package ldextension;

import java.util.Scanner;

// Levenshtein distance Extension
public class LDextension {
	/*
	 * 指定した位置（i1,i2）から、stepで指定した文字数分だけ戻った変換前の距離を取得する。
	 *
	 * テーブルの左、左上、上のうち、最も最小となる方向に戻っていく。
	 * i2の減る方向に進む＝変換ステップが減る（前の変換に戻す）
	 * i2がステップ分だけ減ったらその位置が変換前の位置となる。
	 *
	 */
	public static int getDistance(int[][] dt, int i1, int i2, int step) {
		int distance = 0;

		while (step > 0) {
			// テーブルの端でない
			if ((i1 > 0) && (i2 > 0)) {
				// 左上が最小
				if ((dt[i1-1][i2-1] <= dt[i1-1][i2]) && (dt[i1-1][i2-1] <= dt[i1][i2-1])) {
					distance = dt[i1-1][i2-1];
					i1--; i2--; step--;
				} else if (dt[i1-1][i2] <= dt[i1][i2-1]) { // 上が最小
					distance = dt[i1-1][i2];
					i1--;
				} else {	// 左が最小
					distance = dt[i1][i2-1];
					i2--;
					step--;
				}
			} else if (i1 == 0) {
				distance = dt[i1][i2-1];
				i2--;
				step--;
			} else {
				distance = dt[i1-1][i2];
				i1--;
			}
		}
		return distance;
	}

	/* Transposition Distance
	 *
	 * 	今までの部分文字列を右回転または左回転したとき、その前後で完全一致するなら転置がコスト低い可能性があるため
	 *  その際の距離を計算する。ただし、大文字小文字の変換が入るとコストが逆転する場合もある。
	 *  （が、基本的には2回以上の転置で一致する場合は、末尾を先頭挿入＋末尾削除のコスト4で達成されるので、
	 *  　2文字を超える転置一致確認は実は不要。だが念のため）
	 *
	 *  dt: 距離計算テーブル
	 *  i1, i2: 現在の計算位置
	 */
	public static int tDistance(int[][] dt, String str1, String str2, int i1, int i2) {
		int maxLoop = (i1 < i2) ? i1 : i2;
		String l1, l2, r1, r2, l1Lower, l2Lower, r1Lower, r2Lower;
		int tCost, BSCost;
		int i, j;

		if (maxLoop > 2) maxLoop = 2; // force
		/*　例
		 * abcd : dabc の場合、abcdを右に回転させると一致する。
		 * dabc : abcd の場合、dabcを左に回転させると一致する。
		 * それらの転置コストと、転置後に大小文字変換のコストを加味した転置距離を返す。
		 */
		for (i=1 ; i<=maxLoop-1 ; i++) {
			l1 = str1.substring(i1-maxLoop+i, i1).concat(String.valueOf(str1.charAt(i1-maxLoop+i-1)));
			l2 = str2.substring(i2-maxLoop+i-1, i2);
			l1Lower = l1.toLowerCase();
			l2Lower = l2.toLowerCase();
			if (l1Lower.compareTo(l2Lower) == 0) {
				// 範囲内では転置で一致する
				tCost = (maxLoop - i) * 2;	// 転置コスト
				BSCost = 0;
				for (j=0 ; j<l1.length() ; j++) {	// 大文字小文字変換コスト
					if (l1.charAt(j) != l2.charAt(j)) BSCost++;
				}
				return tCost + BSCost + getDistance(dt, i1, i2, maxLoop-i+1);
			} else {
				r1 = String.valueOf(str1.charAt(i1-1)).concat(str1.substring(i1-maxLoop+i-1, i1-1));
				r2 = str2.substring(i2-maxLoop+i-1, i2);
				r1Lower = r1.toLowerCase();
				r2Lower = r2.toLowerCase();
				if (r1Lower.compareTo(r2Lower) == 0) {
					// 範囲内では転置で一致する
					tCost = (maxLoop - i) * 2;	// 転置コスト
					BSCost = 0;
					for (j=0 ; j<l1.length() ; j++) {	// 大文字小文字変換コスト
						if (r1.charAt(j) != r2.charAt(j)) BSCost++;
					}
					return tCost + BSCost + getDistance(dt, i1, i2, maxLoop-i+1);
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	public static int distance(String str1, String str2) {
		int i1, i2, cost;
		int[][] dt = new int[str1.length()+1][str2.length()+1];

		for (i1=0 ; i1<=str1.length() ; i1++) dt[i1][0] = i1*2;
		for (i2=0 ; i2<=str2.length() ; i2++) dt[0][i2] = i2*2;
		for (i1=1 ; i1<=str1.length() ; i1++)
			for (i2=1 ; i2<=str2.length() ; i2++) {
				if (str1.charAt(i1-1) == str2.charAt(i2-1)) cost = 0;
				else {
					// 小文字大文字変換の拡張
					if (str1.toUpperCase().charAt(i1-1) == str2.toUpperCase().charAt(i2-1)) cost = 1;
					else cost = 2;
				}
				/*
				 * 前の状態から最小のものを選択する
				 * (1)削除コスト
				 * (2)挿入コスト
				 * (3)置換コスト
				 * (4)転置コスト
				 * のうち最小のものを選択する
				 */
				dt[i1][i2] = Math.min(dt[i1-1][i2]+2,
							Math.min(dt[i1][i2-1]+2,
							Math.min(dt[i1-1][i2-1]+cost, tDistance(dt, str1, str2, i1, i2))));

			}
		return dt[str1.length()][str2.length()];
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String str1, str2;

		while (cin.hasNext()) {
			str1 = cin.nextLine();
			str2 = cin.nextLine();
			System.out.println(distance(str1, str2));
		}
		cin.close();
	}

}
