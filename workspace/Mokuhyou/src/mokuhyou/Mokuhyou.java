package mokuhyou;

import java.util.Scanner;

public class Mokuhyou {
	public static int countGoalPattern(int m, int n, int t) {
		int ret = 0;

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int m, n, t;

		while (cin.hasNext()) {
			m = cin.nextInt();
			n = cin.nextInt();
			t = cin.nextInt();
			System.out.println(countGoalPattern(m, n, t));
		}
		cin.close();
	}

}
