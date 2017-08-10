package table;

import java.util.Scanner;

public class Table {
	public static void init(int[] r) {
		for (int i=0; i<r.length ; i++) r[i] = 0;
	}

	public static int getCountPattern(int m, int n, int preN, int[] result) {
		int count = 0;

		if (n <= 0) return 1;
		if (n == 1) return 0;
		if (result[n] != 0) return result[n];	// 既に結果がある場合はそれを返す

		for (int i=preN ; i<=Math.min(m, n) ; i++) {
			result[n-i] = getCountPattern(m, n-i, i, result);
			count += result[n-i];
		}
		return count;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int m, n;
		String[] ret;
		int[] result = new int[1001];

		while (cin.hasNext()) {
			init(result);
			ret = cin.nextLine().split(" ");
			m = Integer.parseInt(ret[0]);
			n = Integer.parseInt(ret[1]);
			System.out.println(getCountPattern(m, n, 2, result));
		}
		cin.close();
	}

}
