// 力技
package marubatsu;

import java.util.Scanner;

public class Marubatsu {
	public static final int BLANK = 0;
	public static final int MARU = 1;
	public static final int BATSU = 10;
	public static final int N = 3;

	/*
	 * 現在のmapの状態が勝ちパターンかチェック
	 */
	public static boolean isKachiPattern(int[][] map) {
		int total;

		// 横方向の和計算
		for (int y=0 ; y<N ; y++) {
			total = 0;
			for (int x=0 ; x<N ; x++) {
				total += map[y][x];
			}
			if (total == N*MARU || total == N*BATSU) return true;
		}

		// 縦方向の和計算
		for (int x=0 ; x<N ; x++) {
			total = 0;
			for (int y=0 ; y<N ; y++) {
				total += map[y][x];
			}
			if (total == N*MARU || total == N*BATSU) return true;
		}

		// 左上から右下方向の和計算
		total = 0;
		for (int i=0 ; i<N ; i++) total += map[i][i];
		if (total == N*MARU || total == N*BATSU) return true;

		// 右上から左下方向の和計算
		total = 0;
		for (int i=0 ; i<N ; i++) total += map[i][N-i-1];
		if (total == N*MARU || total == N*BATSU) return true;

		return false;
	}

	/*
	 * ○×のﾊﾟﾀｰﾝを生成しながら勝ち条件になるパターンをカウントする
	 *
	 * n: 指定手数
	 * step: 現在のステップ数
	 * map: 状態マップ
	 */
	public static int getMaruBatsuPattern(int n, int step, int[][] map) {
		int count = 0;
		int x, y, teban;

		// 指定数になったとき、勝ち条件なら1、それ以外は0を返す
		if (n < step) {
			if (isKachiPattern(map)) return 1;
			else return 0;
		}

		/*
		 *  指定数でないとき
		 */
		// 指定数でないが勝ち到達してしまった場合は条件を満たさないので0を返す
		if (isKachiPattern(map)) return 0;

		teban = (step % 2 == 1) ? MARU : BATSU; // 手版の設定。奇数手番は○、偶数手番は×
		// マップ各々について、空いている箇所それぞれで手番をセットしてみて試す。再帰処理
		for (int i=0 ; i<N*N ; i++) {
			y = i/N;
			x = i%N;
			if (map[y][x] == BLANK) { // 空いているならセット
				map[y][x] = teban;	// 手番側をセット
				count += getMaruBatsuPattern(n, step+1, map);
				map[y][x] = BLANK;	// 戻す
			}
		}

		return count;
	}

	public static int getCount(int n) {
		int[][] map = new int[N][N]; // map[y][x]

		// 配列初期化
		for (int i=0 ; i<N ; i++) {
			for (int j=0 ; j<N ; j++) {
				map[i][j] = BLANK;
			}
		}

		return getMaruBatsuPattern(n, 1, map);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			System.out.println(getCount(cin.nextInt()));
		}
		cin.close();
	}

}
