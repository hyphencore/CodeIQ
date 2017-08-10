package turnOnRightMostLight;

import java.util.ArrayList;
import java.util.Scanner;

public class TurnOnRightMostLight {

	/*
	 * 指定位置から開始し、最初に点灯されている電球の位置を返す
	 *
	 */
	public static int findLeftmostLightBulbs(int start, ArrayList<Boolean> lightBulbs) {
		for (int i=start ; i<lightBulbs.size() ; i++) {
			if (lightBulbs.get(i)) return i;
		}

		return -1;
	}

	/*
	 * 指定した位置のスイッチを押す。
	 * 　押したスイッチの場所が最左なら最左と次のみ点灯状態反転
	 * 　最右なら最右とその前のみ点灯状態反転
	 * 　それ以外は押した場所とその前後の状態を反転
	 */
	public static void pushSwitch(int index, ArrayList<Boolean> lightBulbs) {
		int str, end;

		if (lightBulbs.size() <= 0) return;
		if (lightBulbs.size() == 1) {
			str = 0; end = 0;
		}
		if (index == 0) {
			str = 0;
			end = 1;
		} else if (index == lightBulbs.size()-1) {
			str = lightBulbs.size()-2;
			end = str+1;
		} else {
			str = index-1;
			end = index+1;
		}

		for (int i=str ; i<=end ; i++) lightBulbs.set(i, !lightBulbs.get(i));
	}

	/*
	 * 再帰的に最右だけ点灯させるルーチン。
	 * 考え方:
	 *   点灯している最左にある電球を再帰的に消す作業をしながら、点灯が最右だけになるまで繰り返す
	 *   このルーチンでは、最左の点灯電球を leftmostIndex が指し示している。
	 *
	 *   (1) 残りが1なら完了
	 *   (2) 残りが2なら、最右が点灯していると解無し、最右が点灯していなければleftmostIndexのスイッチを押して完了
	 *   (3) 残りが3なら、次が点灯している場合は次のボタンを押せば良いので+1を返す
	 *       次が点灯していなければ左を押して次に右を押せば良いので+2を返す
	 *   (4) 残りが4以上なら、最左点灯電球の次の位置にあるスイッチを押して、次の最左点灯電球の位置から再帰処理
	 *       このとき、再帰処理+1を返す
	 */
	public static int countTurnOnRightMostLight(int leftmostIndex, ArrayList<Boolean> bulbs) {
		int rest, nextIndex;

		rest = bulbs.size() - leftmostIndex; // 点灯している最左から右に何個電球が残っているか

		if (rest <= 0) return -1; // 解無し
		if (rest == 1) return 0; // 最右だけ点灯で終了
		if (rest == 2) {
			if (!bulbs.get(leftmostIndex+1)) { // 左だけ点灯
				pushSwitch(leftmostIndex+1, bulbs);
				return 1;
			} else return -1; // 今と右が点灯していると解無し
		} else if (rest == 3) {
			if (bulbs.get(leftmostIndex+1)) { // 次が点灯している場合
				pushSwitch(leftmostIndex+1, bulbs);
				return 1;
			} else {
				pushSwitch(leftmostIndex, bulbs);
				return 2;
			}
		} else {
			pushSwitch(leftmostIndex+1, bulbs);
		}

		nextIndex = findLeftmostLightBulbs(leftmostIndex, bulbs);
		if (nextIndex < 0) return -1; // 次なし
		else return countTurnOnRightMostLight(nextIndex, bulbs)+1;		// 1手増やして再帰処理
	}

	/*
	 * 電球の初期化。最左だけ点灯させる。
	 *
	 * 第1引数: 電球の数
	 *
	 * 返値: 指定した電球の数分の点灯状態リストを返す。ただし、最左だけ点灯(true)
	 */
	public static ArrayList<Boolean> initLightBulbs(int num) {
		ArrayList<Boolean> ret = new ArrayList<Boolean>();

		ret.add(Boolean.TRUE);
		for (int i=1 ; i<num ; i++) {
			ret.add(Boolean.FALSE);
		}

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		ArrayList<Boolean> lightbulbs;
		int bulbNum;

		while (cin.hasNext()) {
			bulbNum = cin.nextInt();
			lightbulbs = initLightBulbs(bulbNum);
			System.out.println(countTurnOnRightMostLight(0, lightbulbs));
		}
		cin.close();
	}

}
