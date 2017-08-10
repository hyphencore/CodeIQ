package mathPath;

import java.util.Scanner;

/*
 *  考え方：m×n を縦横だけで移動した場合、手数は m+n かかり、これが最大手数になる。
 *          よって、a回で到達するとき、m+n-a 回だけ斜めを必要とする。
 *
 *  したがって、
 *   縦の回数：n-(m+n-a) = a-m 回
 *   横の回数：m-(m+n-a) = a-n 回
 *   斜の回数：m+n-a 回
 *  であるから、求めるパターンは、「a回のうち縦を選ぶ組み合わせ×(a-縦の回数)のうち横を選ぶ回数」となる。
 *  aCa-m × mCa-n
 *
 *   ただし、最短手数はMax(m, n)になるため、Max(m, n) <= a <= m+n が条件。それ以外の範囲のaなら解無し(=0)
 *
 */


public class MathPath {
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

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int m, n, a;

		while (cin.hasNext()) {
			m = cin.nextInt();
			n = cin.nextInt();
			a = cin.nextInt();
			if (Math.max(m, n) <= a && a <= m+n)
				System.out.println(Combination(a, a-m)*Combination(m, a-n));
			else
				System.out.println("0");
		}
		cin.close();
	}

}
