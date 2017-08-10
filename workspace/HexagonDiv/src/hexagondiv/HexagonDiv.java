package hexagondiv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HexagonDiv {
	public static int LINE_EXIST_MASK = 0x00000001;
	public static int HEXAGON_LINE_NUM = 12;
	// 1つの線から右回りに最初に出現する線までの移動距離に応じた角形を保持。開始位置が頂点からの場合。
	public static int[] mapPolygonFromLines1 = {1, 3, 3, 4, 4, 5, 4, 6, 6, 7, 7, 8};
	// 1つの線から右回りに最初に出現する線までの移動距離に応じた角形を保持。開始位置が頂点でない場合。
	public static int[] mapPolygonFromLines2 = {1, 3, 4, 4, 5, 5, 5, 6, 7, 7, 8, 8};

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int linesNum;
		boolean[] lines = new boolean[HEXAGON_LINE_NUM];
		ArrayList<Integer> result = new ArrayList<Integer>();

		while (cin.hasNext()) {
			linesNum = cin.nextInt();
			result.clear();

			/*
			 *  入力された数値をもとに、各有線位置の格納する。
			 *  0時を0として右回りに移動し11時を11のインデックスとしたとき、入力した数の下位nビット目が
			 *  インデックスnに対応している。
			 */
			for (int i=0 ; i<HEXAGON_LINE_NUM ; i++) {
				lines[i] = (((linesNum >> i) & LINE_EXIST_MASK) == 1 ? true : false);
			}

			/*
			 * 0から始めて、最初の線があるインデックスを求める
			 */
			int firstIndex = -1, preIndex = -1, nextIndex = -1;
			for (int i=0 ; i<HEXAGON_LINE_NUM ; i++) {
				if (lines[i]) {
					firstIndex = i;
					break;
				}
			}

			if (firstIndex >= 0) { // 分割線があった
				preIndex = firstIndex;
				nextIndex = preIndex;
				do {
					/*
					 * preIndex+1 から始めて、最初に現れる線のインデックスを求める
					 */
					do {
						nextIndex++;
						if (nextIndex > HEXAGON_LINE_NUM-1) nextIndex = 0;
					} while (!lines[nextIndex]);

					/*
					 * preIndex ～ nextIndex の位置によって対応する角形を求める（mapより）
					 */
					int interval = nextIndex - preIndex;
					if (interval < 0) interval += HEXAGON_LINE_NUM;
					if (preIndex % 2 == 0) result.add(mapPolygonFromLines1[interval]);
					else result.add(mapPolygonFromLines2[interval]);

					/*
					 * next -> pre
					 */
					preIndex = nextIndex;
					if (nextIndex == firstIndex) nextIndex = -1; // search end.
				} while (nextIndex >= 0);
			}

			if (firstIndex >= 0) {
				Collections.sort(result);
				for (int i=0 ; i<result.size() ; i++) {
					System.out.print(result.get(i));
					if (i < result.size()-1) System.out.print(",");
				}
				System.out.println("");
			} else {
				System.out.println("6");
			}
		}

		cin.close();
	}

}
