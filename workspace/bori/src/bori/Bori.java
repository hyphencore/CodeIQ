package bori;

import java.util.ArrayList;
import java.util.Scanner;

class OneScore {
	static final int SPARE = 1;
	static final int STRIKE = 2;

	int first;	 // 1投目スコア
	int second; // 2投目スコア

	// first = 10 <-- STRIKE
	// first != 10 && first + second = 10 <-- SPARE

	OneScore() {
		first = 0;
		second = 0;
	}

	OneScore(int first, int second) {
		this.first = first;
		this.second = second;
	}

	void setFirst(int first) {
		this.first = first;
	}

	void setSecond(int second) {
		this.second = second;
	}

	public int getFirstScore() {
		return first;
	}

	public int getSecondScore() {
		return first;
	}

	public boolean isStrike() {
		if (first == 10) return true;
		return false;
	}

	public boolean isSpare() {
		if (isStrike()) return false;
		if (first+second != 10) return false;
		return true;
	}
}

public class Bori {
	/*
	 * 最終フレームのあり得ないパターン判定
	 *
	 * 最終フレームの1投目がストライクか、1投目がストライクでなく2投目でスペアでなければ、3投目スコアはありえない
	 * どの投目でもSecondは使用してはならない
	 */
	public static boolean isOkPattern(ArrayList<OneScore> game1) {
		if (game1.get(11).first != 0) {
			if (!game1.get(9).isStrike() && (game1.get(9).first + game1.get(10).first != 10))
				return false;
		}

		for (int i=9 ; i<=11 ; i++) {
			if (game1.get(i).second != 0) return false;
		}

		return true;
	}

	/*
	 * スコア計算
	 */
	public static int getScore(ArrayList<OneScore> game1) {
		int score = 0;

		for (int i=0 ; i<game1.size() ; i++) {
			score += (game1.get(i).first + game1.get(i).second);
			if (i < 9) { // 最終フレーム以外はストライク、スペアによる後フレームスコア加算
				if (game1.get(i).isStrike()) { // 後2投分のスコア加算
					score += (game1.get(i+1).first+game1.get(i+1).second);
					if (game1.get(i+1).isStrike()) { // 次ゲームもストライクのときは、さらにその後1投目加算
						score += game1.get(i+2).first;
					}

				} else if (game1.get(i).isSpare()) {
					score += game1.get(i+1).first;
				}
			}
		}

		return score;
	}

	// debug print...
	public static void print(ArrayList<OneScore> game1) {
		for (int i=0 ; i<game1.size() ; i++) {
			System.out.print(game1.get(i).first + "," + game1.get(i).second + "|");
		}
		System.out.println(" is score:" + getScore(game1));
	}

	/*
	 * 最小値を求める再帰関数
	 */
	public static int getMin(int strike, int spare, int curStrike, int curSpare, int curFrame, int curMin,
										ArrayList<OneScore> game1) {
		int score = curMin;
		int restFrame = 12-curFrame+1; // 今のフレームを含む残り数
		int restStrike = strike - curStrike; // 残りのストライク数
		int restSpare = spare - curSpare; // 残りのスペア数

		if (restFrame < restStrike+restSpare) return curMin; // 条件を満たさない

		if (curFrame > 12) {
			if (strike != curStrike || spare != curSpare) return curMin; // 満たしていない
			if (isOkPattern(game1)) {
				score = getScore(game1);
				if (curMin > score) {
//					print(game1);
					return score;
				}
				else return curMin;
			} else return curMin;
		}

		if (strike > curStrike) { // 最初はストライク採用
			if (curFrame < 11 ||
					((curFrame == 11) && game1.get(9).first == 10) ||
					((curFrame == 12) && game1.get(10).first == 10) ||
					((curFrame == 12) && game1.get(9).first+game1.get(10).first == 10)) { // ただし、最終フレームの2投目以降は、前回がストライクまたはスペアであることが条件
				game1.get(curFrame-1).setFirst(10);
				game1.get(curFrame-1).setSecond(0);
				score = getMin(strike, spare, curStrike+1, curSpare, curFrame+1, score, game1);
			}
		}

		if (spare > curSpare) { // 次はスペア採用
			if (curFrame < 11 || ((curFrame >=11) && game1.get(curFrame-2).first < 10)) { // ただし、最終フレームの2投目以降は、前回投げたスコアが10未満であることが条件
				game1.get(curFrame-1).setFirst(0);
				game1.get(curFrame-1).setSecond(10);
				score = getMin(strike, spare, curStrike, curSpare+1, curFrame+1, score, game1);
			}
		}

		// 今を含む残りフレーム数が残りのストライク＋スペアの数より多いならオープンフレームも許す
		if (restFrame > restStrike+restSpare) { // オープンフレーム対応
			game1.get(curFrame-1).setFirst(0);
			game1.get(curFrame-1).setSecond(0);
			score = getMin(strike, spare, curStrike, curSpare, curFrame+1, score, game1);
		}

		// 念のため消しておく
		game1.get(curFrame-1).setFirst(0);
		game1.get(curFrame-1).setSecond(0);

		return score;
	}

	/*
	 * 最大値を求める再帰関数
	 */
	public static int getMax(int strike, int spare, int curStrike, int curSpare, int curFrame, int curMax,
										ArrayList<OneScore> game1) {
		int score = curMax;
		int restFrame = 12-curFrame+1; // 今のフレームを含む残り数
		int restStrike = strike - curStrike; // 残りのストライク数
		int restSpare = spare - curSpare; // 残りのスペア数

		if (restFrame < restStrike+restSpare) return curMax; // 条件を満たさない

		if (curFrame > 12) {
			if (strike != curStrike || spare != curSpare) return curMax; // 満たしていない
			if (isOkPattern(game1)) {
				score = getScore(game1);
				if (curMax < score) {
//					print(game1);
					return score;
				}
				else return curMax;
			} else return curMax;
		}

		if (strike > curStrike) { // 最初はストライク採用
			if (curFrame < 11 ||
					((curFrame == 11) && game1.get(9).first == 10) ||
					((curFrame == 12) && game1.get(10).first == 10) ||
					((curFrame == 12) && game1.get(9).first+game1.get(10).first == 10)) { // ただし、最終フレームの2投目以降は、前回がストライクまたはスペアであることが条件
				game1.get(curFrame-1).setFirst(10);
				game1.get(curFrame-1).setSecond(0);
				score = getMax(strike, spare, curStrike+1, curSpare, curFrame+1, score, game1);
			}
		}

		if (spare > curSpare) { // 次はスペア採用。ただし、最終フレーム1投目でスペアはありえない
			if ((curFrame < 10) || ((curFrame >=11) && game1.get(curFrame-2).first < 10)) { // ただし、最終フレームの2投目以降は、前回投げたスコアが10未満であることが条件
				if (curFrame < 10) {
					game1.get(curFrame-1).setFirst(9);
					game1.get(curFrame-1).setSecond(1);
				} else {
					game1.get(curFrame-1).setFirst(10-game1.get(curFrame-2).first); // 残りのピン数
					game1.get(curFrame-1).setSecond(0);
				}
				score = getMax(strike, spare, curStrike, curSpare+1, curFrame+1, score, game1);
			}
		}

		// 今を含む残りフレーム数が残りのストライク＋スペアの数より多いならオープンフレームも許す
		if (restFrame > restStrike+restSpare) { // オープンフレーム対応
			game1.get(curFrame-1).setFirst(9);
			game1.get(curFrame-1).setSecond(0);
			if (curFrame >= 11) {
				// ただし、最終フレームの2投目は前回がストライクでないなら0とする。
				// また、最終フレーム3投目は前回がストライクかスペアでないなら0とする。
				// なぜなら前回9ピン倒しているので、オープンにするには1ピンも倒せるものがない
				if ((curFrame == 11) && (!game1.get(9).isStrike()))
					game1.get(curFrame-1).setFirst(0);
				else if ((curFrame == 12)
						&& (!game1.get(10).isStrike() && (game1.get(9).first + game1.get(10).first != 10)))
					game1.get(curFrame-1).setFirst(0);
			}
			score = getMax(strike, spare, curStrike, curSpare, curFrame+1, score, game1);
		}

		// 念のため消しておく
		game1.get(curFrame-1).setFirst(0);
		game1.get(curFrame-1).setSecond(0);

		return score;
	}

	public static void initGame(ArrayList<OneScore> game1) {
		for (int i=0 ; i<game1.size() ; i++) {
			game1.get(i).first = 0;	// スコアボードの初期化
			game1.get(i).second = 0;	// スコアボードの初期化
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		ArrayList<OneScore> game1 = new ArrayList<OneScore>();
		int strike, spare, min, max;

		for (int i=0 ; i<12 ; i++) game1.add(new OneScore());	// スコアボードの初期化

		while (cin.hasNext()) {
			strike = cin.nextInt();
			spare = cin.nextInt();
			initGame(game1);
			min = getMin(strike, spare, 0, 0, 1, 999, game1);
			initGame(game1);
			max = getMax(strike, spare, 0, 0, 1, 0, game1);
			System.out.println(max - min);
		}
		cin.close();
	}

}
