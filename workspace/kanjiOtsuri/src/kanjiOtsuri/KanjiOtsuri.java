package kanjiOtsuri;

import java.util.Scanner;

public class KanjiOtsuri {
	// Combination nCr
	public static int calcC(int n, int r) {
		int i;
		double lc=1,rc=1;
		if ((r == 0)||(n == 0)) return 1;

		if (n-r < r) r = n-r;
		for (i=1 ; i<=r ; i++) {
			lc = lc * n;
			n--;
		}
		for (i=2 ; i<=r ; i++) rc = rc * i;
		return (int)(lc/rc);
	}

	/*
	 * countOtsuriPattern: 再帰的に、1000円出す人、5000円出す人のﾊﾟﾀｰﾝを生成しながらカウントする。
	 *
	 * m: 1000円出す人の数
	 * n: 5000円出す人の数
	 * yen1000mai: 現在のﾊﾟﾀｰﾝでの1000円の枚数
	 * mCount: 現在までで1000円を出した人の数
	 * nCount: 現在までで5000円を出した人の数
	 *
	 * 1000円出した場合は、mCount+1、yen1000mai+3して再帰処理
	 * 5000円出した場合は、nCount+1、yen1000mai-2して再帰処理
	 */
	public static int countOtsuriPattern(int m, int n, int yen1000mai, int mCount, int nCount) {
		int count = 0;

		if (3*m < 2*n) return 0;	// 元々おつり準備必要
		if (yen1000mai < 0) return 0;	// おつり不足のﾊﾟﾀｰﾝだった
		if (mCount+nCount >= m+n) return 1;	// おつり充足したパターンだった
		if (yen1000mai >= 2*(n-nCount)) return (int)calcC(m+n-mCount-nCount, m-mCount);	// 今の1000円で任意パターンOK
		if (mCount < m) count += countOtsuriPattern(m, n, yen1000mai+3, mCount+1, nCount);	// 1000円出した場合
		if (nCount < n) count += countOtsuriPattern(m, n, yen1000mai-2, mCount, nCount+1);	// 5000円出した場合

		return count;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n, i, count;

		while (cin.hasNext()) {
			count = 0;
			n = cin.nextInt();
			for (i=0 ; i<=n ; i++) {
				count += countOtsuriPattern(i, n-i, 0, 0, 0);
			}
			System.out.println(count);
		}
		cin.close();
	}

}
