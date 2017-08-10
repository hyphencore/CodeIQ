package shorui;

import java.util.Scanner;
import java.util.Stack;

public class Shorui {

	// search: 今たまっているStackをもとに、Stackに溜めるか処理するかによって
	//         次に処理する内容を変化させる。最終m書類まで処理する。
	public static int search(int cur, Stack<Integer> s, int m, int n) {
		int count = 0, popN;
		// cur を Stack につむパターン。ただし、未処理書類がある場合だけ。
		if (cur <= m) {
			s.push(cur); // 詰んで、その次を探索
			count = search(cur+1, s, m, n);
			s.pop(); // 元に戻す
		}
		if (!s.isEmpty()) {
			popN = (Integer)s.pop(); // 取り出す
			if (popN == n) count++;
			count += search(cur, s, m, n);
			s.push(popN);		// 元に戻す
		}
		return count;
	}

	public static void main(String[] args) {
		Stack<Integer> syoruiBox = new Stack<Integer>();
		Scanner cin = new Scanner(System.in);
		String line;
		String[] rets;
		int m, n, count;

		while (cin.hasNext()) {
			line = cin.nextLine();
			rets = line.split(" ");
			m = Integer.parseInt(rets[0]);
			n = Integer.parseInt(rets[1]);
			count = search(1, syoruiBox, m, n);
			System.out.println(count);
		}
		cin.close();
	}

}
