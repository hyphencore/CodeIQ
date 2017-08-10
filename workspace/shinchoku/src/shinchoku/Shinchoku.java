package shinchoku;

import java.util.Scanner;

public class Shinchoku {
	public static void print(int n) {
		int middle;

		middle = n / 2;

		if (n == 1) System.out.println("o");
		else {
			/*
			 * 縦方向の真ん中からの相対距離を、真ん中位置番号から引いた数値を、
			 * 横方向の真ん中の位置に加算または減算したものが横座標と一致したらo、一致しなければ.
			 */
			for (int y=0 ; y<n ; y++) {
				for (int x=0 ; x<n ; x++) {
					if ((middle + (middle - Math.abs(middle - y)) == x)
						|| (middle - (middle - Math.abs(middle - y)) == x)) System.out.print("o");
					else System.out.print(".");
				}
				System.out.println("");
			}
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n;

		while (cin.hasNext()) {
			n = cin.nextInt();
			if (n % 2 == 0) System.out.println("invalid");
			else print(n);
		}
		cin.close();
	}

}
