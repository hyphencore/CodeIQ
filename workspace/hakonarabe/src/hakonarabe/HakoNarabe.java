package hakonarabe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/*
 * MapStateクラス：1つの箱配置パターンを管理するクラス
 */
class MapState implements Comparator<MapState> {
	final static int UPPER = 0, RIGHT = 1, LOWER = 2, LEFT = 3;
	int n = 9;
	int size = ((n*2+1)*(n*2+1)/64)+1; // 1マス1ビット×(n*2+1)だけ必要。壁に当たらないサイズを最初から確保する前提
	long state[];	// map保存用

	MapState() {
		state = new long[size];
	}

	MapState(int in_n) {
		n = in_n;
		size = ((in_n*2+1)*(in_n*2+1)/64)+1;
		state = new long[size];
	}

	void clear() {
		for (int i=0 ; i<size ; i++) state[i] = 0;
	}

	// 0 <= index <= 80 (1～81のindex)
	void setBit(int index) {
		int idx = index / 64;	// state1要素が64bit
		int loc = index % 64;  // state[idx]の対象ビットをloc
		state[idx] |= ((long)1 << loc);	// indexビット目だけ1にする
	}

	// 0 <= index <= 80 (1～81のindex)
	void unsetBit(int index) {
		int idx = index / 64;	// state1要素が64bit
		int loc = index % 64;  // state[idx]の対象ビットをloc
		state[idx] &= (~((long)1 << loc));	// indexビット目だけ0にする
	}

	int getBit(int index) {
		int idx = index / 64;	// state1要素が64bit
		int loc = index % 64;  // state[idx]の対象ビットをloc
		return (state[idx] & ((long)1 << loc)) != 0 ? 1 : 0;	// indexビット目が0なら0を、そうでなければ1を返す
	}

	// n*n の形でパターン表示 for debug
	void print(int n) {
		for (int i=0 ; i<=n*2 ; i++) {
			for (int j=0 ; j<=n*2 ; j++) {
				System.out.print(getBit(i*(n*2+1)+j));
			}
			System.out.println("");
		}
		System.out.println("------------");
	}

	// 引数の状態をコピーする
	void save(MapState ms) {
		for (int i=0 ; i<size ; i++) state[i] = ms.state[i];
	}

	// 引数の状態と一致するか。一致するならTRUE
	boolean compare(MapState ms) {
		for (int i=0 ; i<size ; i++)
			if (ms.state[i] != state[i]) return false;

		return true;
	}

	// toIndex に移動できればtrue
	boolean canMove(int cur, int direction) {
		int next;

		// 次のインデックス
		next = getNextIndex(cur, direction);

		// 既に通った場所
		if (getBit(next) != 0) return false;

		return true;
	}

	// 現在位置から次の方向に進んだ時のindexを算出する
	int getNextIndex(int cur, int direction) {
		int ret = cur;

		if (direction == UPPER) ret -= (n*2+1);
		if (direction == LOWER) ret += (n*2+1);
		if (direction == LEFT)  ret--;
		if (direction == RIGHT) ret++;

		return ret;
	}

	// for interface
	public int compare(MapState ms1, MapState ms2) {
		int endi = Math.min(ms1.size, ms2.size);

		// サイズの大きい方があれば、そちらしか存在しない上位桁が0でなければ大と判定する
		if (ms1.size < ms2.size) {
			for (int i=ms2.size-1 ; i>=ms1.size ; i--) {
				if (ms2.state[i] != 0) return -1;
			}
		} else if (ms1.size > ms2.size) {
			for (int i=ms1.size-1 ; i>=ms2.size ; i--) {
				if (ms1.state[i] != 0) return 1;
			}
		}

		for (int i=endi-1 ; i>=0 ; i--) {
			if (ms1.state[i] > ms2.state[i]) return 1;
			else if (ms1.state[i] < ms2.state[i]) return -1;
		}

		return 0;
	}
}

public class HakoNarabe {
	/*
	 * 既知盤面かチェックする。既知ならtrue
	 */
	public static boolean isExistedMap(MapState ms, ArrayList<MapState> mss) {
//		Iterator<MapState> it = mss.iterator();

		if (Collections.binarySearch(mss, ms, ms) >= 0) return true;
		else return false;

		/* 自前
		while (it.hasNext()) {
			if (ms.compare(it.next())) return true;
		}
		*/
	}

	/*
	 * 再帰的に手数を進めて、既知でないパターンをカウントする。
	 *
	 * 手順:
	 *  (1) 現在位置を通過済みにセットする
	 *  (2) 今の手数が求められたステップ数到達なら、
	 *    (2.a) 通過マップのパターンが既出ならカウントせず戻す
	 *    (2.b) 既出でないなら、既知パターンとして登録し、1パターンカウントして戻す
	 *  (3) まだ手数が求められたステップ数に到達していないなら、
	 *    UPPER～LEFTまでの4方向それぞれに、進行できるならステップ数を増やして次の位置をセットして再帰処理し、
	 *    返ってきたカウント数を加算する
	 *  (4) 現在位置の通過済みをリセットする
	 *  (5) 求めたカウント数を返す
	 *
	 * 第1引数: n マップの1辺サイズ
	 * 第2引数: step 現在進んだ数
	 * 第3引数: cur 現在の位置(index)
	 * 第4引数: 現在のマップ状況
	 * 第5引数: 既知マップ状況のリスト(探索済みマップパターンのリスト)
	 */
	public static int countBoxPattern0(int n, int step, int cur, MapState curMap, ArrayList<MapState> mss) {
		int ret = 0, nextIndex;

		curMap.setBit(cur);	// (1) 現在地をセット
		if (n == step) { // (2)
			if (!isExistedMap(curMap, mss)) { // (2.b) 既知のパターンかっチェック
				mss.add(new MapState(n));	// 1つインスタンス追加
//				curMap.print(n); // debug
				mss.get(mss.size()-1).save(curMap); // 今のマップを追加する
				Collections.sort(mss, curMap);
				ret = 1;
			} else // (2.a)
				ret = 0;
		} else { // (3)
			for (int i=MapState.UPPER ; i<= MapState.LEFT ; i++) {
				if (curMap.canMove(cur, i)) {
					nextIndex = curMap.getNextIndex(cur, i);
					ret += countBoxPattern0(n, step+1, nextIndex, curMap, mss);
				}
			}
		}
		curMap.unsetBit(cur);	// (4) 元に戻す

		return ret; // (5)
	}

	/*
	 * 再帰カウントのためのMAINルーチン
	 */
	public static int countBoxPattern(int n) {
		ArrayList<MapState> mss = new ArrayList<MapState>(); // 既知のマップリストは空で初期化
		MapState curMap = new MapState(n);	// nステップどう動いてもOKなマップサイズにて初期化しておく

		// cur=マップ真ん中から開始する
		return countBoxPattern0(n, 0, (n*2+1)*n+n, curMap, mss);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			System.out.println(countBoxPattern(cin.nextInt()));
		}
		cin.close();
	}

}
