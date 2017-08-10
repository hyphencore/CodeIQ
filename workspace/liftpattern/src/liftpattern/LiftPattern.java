package liftpattern;

import java.util.Scanner;

public class LiftPattern {
	public static int countLiftPattern(int m, int n, int[] counted) {
		int ret = 0;
		int loopMax, i;

		if (m <= 0) return 1;
		if (counted[m-1] > 0) return counted[m-1];		// 過去計算済み分はその値を返す
		loopMax = (m < n) ? m : n;
		for (i=1 ; i<=loopMax ; i++) {
			ret += countLiftPattern(m-i, n, counted);
		}
		counted[m-1] = ret;
		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		String[] ret;
		int[] countedPattern;
		int m, n, count;

		while (cin.hasNext()) {
			line = cin.nextLine();
			ret = line.split(" ");
			m = Integer.parseInt(ret[0]);
			n = Integer.parseInt(ret[1]);
			countedPattern = new int[m];
			count = countLiftPattern(m, n, countedPattern);
			System.out.println(count);
		}
		cin.close();
	}


}
