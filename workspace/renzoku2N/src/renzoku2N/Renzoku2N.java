package renzoku2N;

import java.util.Scanner;

public class Renzoku2N {

	/*
	 *  連続する2数をn、n+1とすると、Nの倍数になるとき、2n+1 = Nk （ｋ≧1）であり、n = (Nk - 1)/2
	 *   1 <= n <= 999999 より、 1 <= (Nk - 1)/2 <= 999999
	 *   よって、nが自然数になるにはNK-1は偶数であることからNKは奇数。NKが奇数になるのは、Nもkも奇数のとき。
	 *   したがって、3/N <= k <= 1999999/N の範囲となる奇数kを数えればよい。
	 *   以上をまとめると、
	 *   (1) Nが偶数のとき、満たすｎはなく0
	 *   (2) Nが奇数のとき、3/N <= k <= 1999999/N　を満たす奇数ｋを数える。ただし、N>=3より、3/Nは1以下だから、
	 *        1 <= k <= 1999999/N となる奇数kを数える。
	 *        つまりは、INT（1999999/N）が偶数ならその値/2、奇数ならINT(その値/2）+1
	 */
	public static int countN(int N) {
		int count;
		// (1) N = 2
		if (N % 2 == 0) return 0;

		// (2) N > 2
		count = (int)Math.floor(1999999/N);
		if (count % 2 == 0) return count/2;
		else return (int)Math.floor(count/2)+1;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;

		while (cin.hasNext()) {
			line = cin.nextLine();
			System.out.println(countN(Integer.parseInt(line)));
		}
		cin.close();
	}

}
