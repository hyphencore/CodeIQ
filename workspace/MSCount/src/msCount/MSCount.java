package msCount;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * 地雷に着目し、全ての地雷各々に対して、その周辺8か所が地雷でなければカウントすれば、
 * その合計が求める数となる
 */

public class MSCount {
	/*
	 * m×n マップの地雷配置を次パターンにする処理
	 */
	public static boolean nextPattern(ArrayList<Boolean> map, int a) {
		int i, count=0, index0, index1;

		// マップ末尾が地雷数だけ地雷連続していたら次パターンなし
		for (i=0 ; i<a ; i++) {
			if (map.get(map.size()-i-1)) count++;
		}
		if (count == a) return false;	// 次候補なし

		// 最初の1を探し、その1の場所(index1)から最初の0の位置(index0)までに含む1の数(count)を記録する
		// それぞれの位置も記録する
		count = 0; index1 = -1;
		for (i=0 ; i<map.size() ; i++) {
			if (map.get(i)) {
				index1 = i;
				break;
			}
		}
		count++; index0 = -1;
		for (i=index1+1 ; i<map.size() ; i++) {
			if (!map.get(i)) {
				index0 = i;
				break;
			} else count++;
		}

		// index0 の位置に地雷を設置、index0から末尾を一旦クリアし、末尾からcount分を地雷で配置する
		map.set(index0, Boolean.TRUE);
		for (i=index0-1 ; i>=0 ; i--) map.set(i, Boolean.FALSE);
		for (i=0 ; i<count-1 ; i++) map.set(i, Boolean.TRUE);

		return true;
	}

	public static void printMap(ArrayList<Boolean>map) {
		for (int i=map.size()-1 ; i>=0 ; i--) {
			if (map.get(i)) System.out.print("1");
			else System.out.print("0");
		}
		System.out.println("");
	}

	/*
	 * 1つの地雷回りをカウントする
	 */
	public static int countAround(ArrayList<Boolean> map, int m, int n, int a, int current) {
		int ret = 0, startx=-1, endx=-1, starty=-1, endy=-1, currentx, currenty;

		currentx = current % n;
		currenty = current / n;

		if (current % n == 0) {
			startx = 0; endx = Math.min(1, n-1);
		} else if (current % n == n-1) {
			startx = Math.max(0,  n-2); endx = n-1;
		} else {
			startx = currentx-1; endx = currentx+1;
		}

		if (0 <= current && current <= n-1) {
			starty = 0; endy = Math.min(1, m-1);
		} else if ((m-1)*n <= current && current <= m*n-1) {
			starty = Math.max(0,  m-2); endy = m-1;
		} else {
			starty = currenty-1; endy = currenty+1;
		}

		for (int x=startx ; x<=endx ; x++) {
			for (int y=starty ; y<=endy ; y++) {
				if ((currentx != x || currenty != y) && !map.get(n*y+x))
					ret++;
			}
		}
		return ret;
	}

	/*
	 * 全ての地雷について、その周りをカウントする
	 */
	public static int count(ArrayList<Boolean> map, int m, int n, int a) {
		int ret = 0;

		for (int i=0 ; i<map.size(); i++) {
			if (map.get(i)) {
				ret += countAround(map, m, n, a, i);
				a--;
			}
		}

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int m, n, a, max, ret;
		ArrayList<Boolean> map;

		while (cin.hasNext()) {
			m = cin.nextInt();
			n = cin.nextInt();
			a = cin.nextInt();
			map = new ArrayList<Boolean>();
			for (int i=0 ; i<a ; i++) map.add(Boolean.TRUE);
			for (int i=a ; i<m*n ; i++) map.add(Boolean.FALSE);
			max = 0;
			do {
				ret = count(map, m, n, a);
				if (max < ret) {
					max = ret;
				}
			} while (nextPattern(map, a));
			System.out.println(max);
		}
		cin.close();
	}

}
