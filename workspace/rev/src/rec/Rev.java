package rec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Rev {
	// ArrayList src で指定された数が、n回でちょうど最左1になるか判定。途中で1になったら即終了
	public static int calcRev1(ArrayList<Integer> src, int m, int n) {
		int i;
		ArrayList<Integer> srcTmp = new ArrayList<Integer>(src); // copy

		for (i=1 ; i<=n ; i++) {
			if (srcTmp.get(0) == 1) return 0;
			Collections.reverse(srcTmp.subList(0, srcTmp.get(0)));
		}
		if (srcTmp.get(0) == 1) return 1;
		return 0;
	}

	// indexOfOverLow: 指定したリストの中からsearchLow以上の最小の数を探しインデックスを返す
	public static int indexOfOverLow(List<Integer> src, int searchLow) {
		int min = Integer.MAX_VALUE, i, ret = -1;

		for (i=0 ; i<src.size() ; i++) {
			if (min > src.get(i) && src.get(i) >= searchLow) {
				min = src.get(i);
				ret = i;
			}
		}
		return ret;
	}

	// src の次に大きい対象数を求める。
	//最下位からその上位の大小を比較し、
	// 小さいならその上位桁から下位桁のうち、上位数＋1以上を満たす最小数と入れ替えして下位桁以降をソートする。
	public static int getNextIntList(ArrayList<Integer> src) {
		int i, size, ret = -1, swapIndex;

		size = src.size();
		for (i=size-2 ; i>=0 ; i--) {
			if (src.get(i) < src.get(i+1)) {
				swapIndex = indexOfOverLow(src.subList(i+1, size), src.get(i)+1)+i+1;
				Collections.swap(src, i, swapIndex);
				Collections.sort(src.subList(i+1, size));	// sublist で取得したインスタンスは同じものを指す
				ret = 1;
				break;
			}
		}
		return ret;
	}

	public static int kaijyo(int n) {
		int ret = 1, i;

		for (i=2 ; i<=n ; i++) ret *= i;
		return ret;
	}

	// calcRev: m桁整数全体で、n回で左桁が1になるものをカウントし返す。
	public static int calcRev(int m, int n) {
		int count=0, cont = 1;
		ArrayList<Integer> nextIntList;

		if (n == 0) return kaijyo(m-1);
		nextIntList = makeBaseIntList(m);
		while (cont >= 0) {
			count += calcRev1(nextIntList, m, n);
			cont = getNextIntList(nextIntList);
		}
		return count;
	}

	// 指定桁数の初期数。左から小さい順。ただし最左は1以外
	public static ArrayList<Integer> makeBaseIntList(int m) {
		int i;
		ArrayList<Integer> ret = new ArrayList<Integer>();

		if (m == 1) ret.add(1);
		if (m >= 2) {
			ret.add(2);
			ret.add(1);
			for (i=3 ; i<=m ; i++) {
				ret.add(i);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		String[] ret;
		int m, n;

		while (cin.hasNext()) {
			line = cin.nextLine();
			ret = line.split(" ");
			m = Integer.parseInt(ret[0]);
			n = Integer.parseInt(ret[1]);
			System.out.println(calcRev(m, n));
		}
		cin.close();
	}
}
