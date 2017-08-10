package sosuuwa;

import java.util.ArrayList;
import java.util.Scanner;

public class Sosuuwa {
	/*
	 * makeSosuuArray: str - end の範囲の素数を返す。昇順。
	 */
	public static ArrayList<Integer> makeSosuuArray(int str, int end) {
		int i;
		ArrayList<Integer> ret = new ArrayList<Integer>();

		for (i=0 ; i<end-str+1 ; i++) {
			if (isSosuu(str+i)) {
				ret.add(str+i);
			}
		}
		return ret;
	}

	/*
	 * isSosuu: 指定nが素数か判定
	 */
	static public boolean isSosuu(int n) {
		int max = (int)Math.sqrt(n);
		int i;

		if (n < 2) return false;
		if (n == 2) return true;
		if (n % 2 == 0) return false;
		for (i=3 ; i<=max ; i+=2) {
			if (n % i == 0) return false;
		}
		return true;
	}

	/*
	 * countSosuuwa: 合計tになるstr-endの素数を最大1回使用したパターンをカウントする。
	 *
	 * 考え方は、素数範囲の最初から走査し、その素数を採用した場合は、目標値-採用素数の数字を新たに目標値として
	 * 　次の素数から再帰カウントさせる。素数を採用しなかった場合は、目標値はそのままで、次の要素から末に対して
	 * 　再帰カウントする。
	 *
	 * アルゴリズム：
	 * 　(1) 素数配列を越えたら見つからなかったため0を返す
	 *   (2) 目標値より今の素数が越えた場合は、現在位置から後ろに目標数値はないため0を返す
	 *   (3) 目標値と今の素数が一致した場合は見つかったため1を返す
	 *   (4) それ以外(目標値に今の素数ではまだ到達していない)の場合は、
	 *   　　(a) 今の素数を採用したとして、目標値-今の素数の値を次の目標値として、次の素数から再帰探索する
	 *   　　(b) 今の素数を飛ばしたとして、目標値を変えず次の素数から再探索する
	 *   　(a)、(b)のカウント数を返す
	 */
	public static int countSosuuwa(int t, int str, int end, int curIndex, ArrayList<Integer> sosuuArray) {
		int ret;

		if (curIndex >= sosuuArray.size()) return 0;	// not found.
		if (t < sosuuArray.get(curIndex)) return 0;	// 目標値を超えてしまった
		if (t == sosuuArray.get(curIndex)) return 1;	// 目標値と一致

		ret = countSosuuwa(t-sosuuArray.get(curIndex), str, end, curIndex+1, sosuuArray); // 今の素数採用
		ret += countSosuuwa(t, str, end, curIndex+1, sosuuArray);	// 今の素数採用せず
		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String[] ret;
		int t, str, end;
		ArrayList<Integer> sosuuArray;

		while (cin.hasNext()) {
			ret = cin.nextLine().split(" ");
			t = Integer.parseInt(ret[0]);
			str = Integer.parseInt(ret[1]);
			end = Integer.parseInt(ret[2]);
			sosuuArray = makeSosuuArray(str, end);
			System.out.println(countSosuuwa(t, str, end, 0, sosuuArray));
		}
		cin.close();
	}

}
