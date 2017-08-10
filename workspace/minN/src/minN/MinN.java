package minN;

import java.util.ArrayList;
import java.util.Scanner;

public class MinN {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		ArrayList<String> nnStr = new ArrayList<String>();
		String line;
		int n;
		int[][] nn;

		while (cin.hasNext()) {
			nnStr.add(cin.nextLine());
		}
		cin.close();

		// N×N の初期化
		n = nnStr.get(0).length();
		nn = new int[n][n];
		for (int i=0 ; i<n ; i++) {
			line = nnStr.get(i);
			for (int j=0 ; j<n ; j++) {
				nn[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
			}
		}

		// 左上から順番に、それぞれのマスに到達する最小値を算出する。
		for (int i=0 ; i<n ; i++) {
			for (int j=0 ; j<n ; j++) {
				if (i == 0) { // 最上段
					if (j != 0) { // 最左でない場合は左と自分をを足していく
						nn[i][j] += nn[i][j-1];
					}
				} else { // 最上段でない
					if (j != 0) { // 最左でない場合は左と上の小さい方に自分を足す
						nn[i][j] += Math.min(nn[i][j-1], nn[i-1][j]);
					} else { // 最左の場合は上と自分を足す
						nn[i][j] += nn[i-1][j];
					}
				}
			}
		}

		// 最右下に出来た数が求める数
		System.out.println(nn[n-1][n-1]);
	}

}
