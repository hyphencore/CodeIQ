package sdm;

import java.util.Scanner;

/*
 * 進捗だめですマーク：SDMクラス
 */
public class SDM {

	/*
	 *
	 */
	public static void damePrint(int n) {
		int middle, pLeftX, pRightX;

		if (n % 2 == 0) {
			System.out.println("invalid");
		} else {
			middle = n / 2;	// 中心位置
			for (int y=0 ; y<n ; y++) {
				for (int x=0 ; x<n ; x++) {
					pLeftX = middle - Math.abs(middle - y); // 左側のX印字位置
					pRightX = n - pLeftX - 1;	// 右側の印字位置
					if (pLeftX == x || pRightX == x) System.out.print("x");
					else System.out.print(".");
				}
				System.out.println("");
			}
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			damePrint(cin.nextInt());
		}
		cin.close();
	}

}
