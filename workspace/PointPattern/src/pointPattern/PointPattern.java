package pointPattern;

import java.util.Scanner;

public class PointPattern {
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
	 * countPointPattern: n点先取の点a,bになるパターンをカウントする
	 *
	 * a <= b でcallすることが前提。a,b入れ替えしてもパターン数は変わらない。
	 *
	 * (1) n > b (>= a) のときは、a+b回の全得点パターンのうち、aをとる組み合わせ。(a+b)C(a)
	 * (2) n = b かつ n > a のときは、(a+b-1)Ca
	 * (3) n <= b のときは、(2*n-2)C(n-1) * 2^{a-n+1}
	 *
	 */
	public static long countPointPattern(int n, int a, int b) {
		if (((a > n) || (b > n)) && (b - a > 2)) return 0;
		if (n > b) return Combination(a+b, a);
		if (n == b && n > a) return Combination(a+b-1, a);
		if (n <= b) return Combination(2*n-2, n-1)*(long)Math.pow(2, a-n+1);
		return 0;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String[] ret;
		int n, a, b, tmp;

		while (cin.hasNext()) {
			ret = cin.nextLine().split(" ");
			n = Integer.parseInt(ret[0]);
			a = Integer.parseInt(ret[1]);
			b = Integer.parseInt(ret[2]);
			if (a > b) { // b <= a となるよう入れ替え
				tmp = a;
				a = b;
				b = tmp;
			}
			System.out.println(countPointPattern(n, a, b));
		}
		cin.close();
	}

}
