package lrjump;

import java.util.Scanner;

public class LRJump {
	/*
	 * countLRJumpPattern: 再帰処理によって、nマスの箇所を交互左右に移動しながら全マス通過、最終右端到達する
	 * 						パターンをカウントする
	 *
	 * 第1引数: 入力されたマス数(不変)
	 * 第2引数: 今まで移動した箇所の履歴
	 * 第3引数: 移動回数(初期値1、最大n）
	 * 第4引数: 現在位置(左端0、右端n-1)
	 *
	 * 返値: 条件を満たして右端到達したら1、見つからなければ0、それ以外は再帰的にカウントを加算してそのまま返す
	 */
	public static int countLRJumpPattern(int n, boolean[] moved, int movedCount, int currentLocation) {
		int i, ret = 0;

		if (n % 2 == 1) return 0;	// 奇数のnは条件を満たせない

		// 現在地が最右で、移動回数がn-1（すべて移動した）場合カウント
		if ((currentLocation == n-1) && (movedCount == n)) ret = 1;
		else {
			if (movedCount % 2 == 1) { // 次は右
				for (i=currentLocation+1 ; i<n ; i++) { // 現在地より右で未踏のところへ行く
					if (!moved[i]) {
						moved[i] = true;
						ret += countLRJumpPattern(n, moved, movedCount+1, i);
						moved[i] = false;
					}
				}
			} else { // 次は左
				for (i=1 ; i<currentLocation ; i++) { // 現在地より左で未踏のところへ行く
					if (!moved[i]) {
						moved[i] = true;
						ret += countLRJumpPattern(n, moved, movedCount+1, i);
						moved[i] = false;
					}
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n, i;
		boolean[] moved = new boolean[12];

		while (cin.hasNext()) {
			moved[0] = true;
			for (i=1 ; i<12 ; i++) moved[i] = false;
			n = Integer.parseInt(cin.nextLine());
			System.out.println(countLRJumpPattern(n, moved, 1, 0));
		}
		cin.close();
	}
}
