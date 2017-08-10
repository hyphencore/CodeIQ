package superSosuu;

import java.util.Scanner;

public class SuperSosuu {
	static int[] sosuu = new int[20000];
	static int[] superSosuu = new int[2017];

	// 引数の数は素数かチェック
	static public boolean isSosuu(int n) {
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

	static public void makeSosuu() {
		int i, count = 0;

		sosuu[count] = 2; count++; i = 3;
		while (count < 17539) { // 2017番目の素数
			if (isSosuu(i)) {
				sosuu[count] = i;
				count++;
			}
			i += 2;
		};
	}

	static public void makeSuperSosuu() {
		int i;

		for (i=0 ; i<2017 ; i++) {
			superSosuu[i] = sosuu[sosuu[i]-1];
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int i, n;

		while (cin.hasNext()) {
			line = cin.nextLine();
			n = Integer.parseInt(line);
			for (i=0 ; i<20000 ; i++) sosuu[i] = 0;
			for (i=0 ; i<2017 ; i++) superSosuu[i] = 0;
			makeSosuu();
			makeSuperSosuu();
			System.out.println(superSosuu[n-1]);
		}
		cin.close();
	}
}
