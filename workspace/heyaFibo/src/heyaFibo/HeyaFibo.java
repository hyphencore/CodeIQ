package heyaFibo;

import java.util.Scanner;

public class HeyaFibo {
	/*
	 * 課題条件の場合、24要素ごとに要素1に戻る。
	 * よって、指定されたn番目の数は、(n-1) mod 24 + 1 番目の数と同じ。
	 *
	 * これにより、どんなnでも最大24個まで求めれば答えが算出できる。
	 */
	public static long getHeyaFibo(int n) {
		long pre1, pre2, ret = 0;

		n = (n - 1) % 24 + 1;

		if (n == 1) return 1;
		if (n == 2) return 1;

		pre1 = 1; pre2 = 1;
		for (int i=3 ; i<=n ; i++) {
			ret = pre1 + pre2;
			if (ret >= 16) ret %= 16;
			pre1 = pre2;
			pre2 = ret;
		}

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			System.out.println(getHeyaFibo(cin.nextInt()));
		}
		cin.close();
	}

}
