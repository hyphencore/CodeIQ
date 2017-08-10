package rotateHexagon;

import java.util.Scanner;

public class RotateHexagon {
	// 回転時のマップ。-1は移動先存在せず
	static int[] map_a = {14, 17, -1, 9, 13, 16, -1, 4, 8, 12, -1, -1, 3, 7, -1, -1, -1, -1, -1}; // rotate base a
	static int[] map_b = {-1, 11, 15, -1, 6, 10, 14, -1, 2, 5, 9, 13, -1, 1, 4, 8, -1, 0, 3}; // rotate base b

	/*
	 * 入力に対する存在有無リスト（boolean配列）を生成し、配列を返す
	 */
	public static boolean[] initHexes(String[] hexLines) {
		int size = 0, count;
		boolean[] ret;

		for (int i=0 ; i<hexLines.length ; i++) {
			size += hexLines[i].length();
		}

		ret = new boolean[size];
		count = 0;

		for (int i=0 ; i<hexLines.length ; i++) {
			for (int j=0 ; j<hexLines[i].length() ; j++) {
				ret[count++] = (hexLines[i].charAt(j) == '0' ? false : true);
			}
		}

		return ret;
	}

	/*
	 * 入力存在有無配列をrotate(a or b)を基準に回転させた結果をboolean配列で返す
	 */
	public static boolean[] rotateHexagon(boolean[] hexes, String rotate) {
		boolean[] result;

		result = new boolean[hexes.length];

		for (int i=0 ; i<result.length ; i++) {
			result[i] = false;
		}

		for (int i=0 ; i<hexes.length ; i++) {
			if (hexes[i]) {
				if (rotate.compareTo("a") == 0) {
					if (map_a[i] < 0) return null;
					else result[map_a[i]] = true;
				}
				else {
					if (map_b[i] < 0) return null;
					else result[map_b[i]] = true;
				}
			}
		}

		return result;
	}

	public static void printHexagonFT(boolean[] hexes, int from, int to) {
		for (int i=from ; i<=to ; i++) {
			if (hexes[i]) System.out.print("1");
			else System.out.print("0");
		}
	}

	public static void printHexagon(boolean[] hexes) {
		if (hexes == null) System.out.println("-");
		else {
			printHexagonFT(hexes, 0, 2);
			System.out.print("/");
			printHexagonFT(hexes, 3, 6);
			System.out.print("/");
			printHexagonFT(hexes, 7, 11);
			System.out.print("/");
			printHexagonFT(hexes, 12, 15);
			System.out.print("/");
			printHexagonFT(hexes, 16, 18);
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String rLoc;
		String[] HexLines;
		boolean[] Hexes;

		cin.useDelimiter("\\s|\\n");
		while (cin.hasNext()) {
			rLoc = cin.next();
			HexLines = cin.next().split("/");
			Hexes = initHexes(HexLines);
			printHexagon(rotateHexagon(Hexes, rLoc));
		}
		cin.close();
	}

}
