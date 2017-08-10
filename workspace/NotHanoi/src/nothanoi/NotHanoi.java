package nothanoi;

import java.util.Scanner;
import java.util.Stack;

public class NotHanoi {
	public static final int MOVE_NONE = -1;
	public static final int MOVE_LEFT_TO_MIDDLE = 0;
	public static final int MOVE_LEFT_TO_RIGHT = 1;
	public static final int MOVE_MIDDLE_TO_LEFT = 2;
	public static final int MOVE_MIDDLE_TO_RIGHT = 3;
	public static final int MOVE_RIGHT_TO_LEFT = 4;
	public static final int MOVE_RIGHT_TO_MIDDLE = 5;

	public static final Stack<Integer>passed = new Stack<Integer>();
	/*
	 * trace情報を元に1手戻す
	 */
	public static void backTrace(Stack<Integer>l, Stack<Integer>m, Stack<Integer>r, Stack<Integer> trace) {
		Stack<Integer> src, dst;

		if (trace.isEmpty()) return;

		switch (trace.pop()) {
			case MOVE_LEFT_TO_MIDDLE:
				src = m; dst = l;
				break;

			case MOVE_LEFT_TO_RIGHT:
				src = r; dst = l;
				break;

			case MOVE_MIDDLE_TO_LEFT:
				src = l; dst = m;
				break;

			case MOVE_MIDDLE_TO_RIGHT:
				src = r; dst = m;
				break;

			case MOVE_RIGHT_TO_LEFT:
				src = l; dst = r;
				break;

			case MOVE_RIGHT_TO_MIDDLE:
				src = m; dst = r;
				break;

			default:
				System.out.println("error: unknown trace!");
				return;
		}
		dst.push(src.pop());
	}

	public static boolean canMove(Stack<Integer>s, int num) {
		int overcount = 0;

		for (int i=0 ; i<s.size() ; i++) {
			if (num > s.get(i)) overcount++;
		}
		if (overcount <= 1) return true;
		else return false;
	}

	public static boolean move(Stack<Integer>src, Stack<Integer>dst, Stack<Integer>trace, int cont) {
		dst.push(src.pop());
		trace.push(cont);
		return true;
	}

	public static void printStack(Stack<Integer>l, Stack<Integer>m, Stack<Integer>r) {
		if (!l.isEmpty()) {
			System.out.print("L: ");
			for (int i=0 ; i<l.size() ; i++) System.out.print(l.get(i));
			System.out.print(", ");
		} else { System.out.print("L: ,"); }
		if (!m.isEmpty()) {
			System.out.print("M: ");
			for (int i=0 ; i<m.size() ; i++) System.out.print(m.get(i));
			System.out.print(", ");
		} else { System.out.print("M: ,"); }

		if (!r.isEmpty()) {
			System.out.print("R: ");
			for (int i=0 ; i<r.size() ; i++) System.out.print(r.get(i));
			System.out.println("");
		} else { System.out.println("M:"); }
	}

	public static boolean searchPassed(Stack<Integer>l, Stack<Integer>m, Stack<Integer>r, Stack<Integer>passed) {

		return true;
	}

	public static int countMinPath(Stack<Integer>l,
										Stack<Integer>m,
										Stack<Integer>r,
										Stack<Integer> trace,
										int cur,
										int min) {
		int retmin = 0, lastMoved = MOVE_NONE;
//		Stack<Integer> src, dst;

		printStack(l, m, r);

		if (l.isEmpty() && r.isEmpty()) { // 終了。小さい方を返す
			if (cur < min) return cur;
			else return min;
		}

		if (cur >= min) return min; // これ以上動かしても最小になりえない

		if (searchPassed(l, m, r, passed)) {
			System.out.println("passed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}

		// まず移動元を決める。ただし、直前の移動先は選べない。
		if (!trace.isEmpty()) lastMoved = trace.peek();

		// 左が空でなく、前回の移動先が左でないなら左を移動元とする
		if (!l.isEmpty() && (lastMoved != MOVE_MIDDLE_TO_LEFT && lastMoved != MOVE_RIGHT_TO_LEFT)) {
			if (canMove(m, l.peek())) { // 中に移動できる
				move(l, m, trace, MOVE_LEFT_TO_MIDDLE);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
			if (canMove(r, l.peek())) { // 右に移動できる
				move(l, r, trace, MOVE_LEFT_TO_RIGHT);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
		}
		// 中が空でなく、前回の移動先が中でないなら中を移動元とする
		if (!m.isEmpty() && (lastMoved != MOVE_LEFT_TO_MIDDLE && lastMoved != MOVE_RIGHT_TO_MIDDLE)) {
			if (canMove(l, m.peek())) { // 左に移動できる
				move(m, l, trace, MOVE_MIDDLE_TO_LEFT);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
			if (canMove(r, m.peek())) { // 右に移動できる
				move(m, r, trace, MOVE_MIDDLE_TO_RIGHT);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
		}
		// 右が空でなく、前回の移動先が右でないなら右を移動元とする
		if (!r.isEmpty() && (lastMoved != MOVE_LEFT_TO_RIGHT && lastMoved != MOVE_MIDDLE_TO_RIGHT)) {
			if (canMove(l, r.peek())) { // 左に移動できる
				move(r, l, trace, MOVE_RIGHT_TO_LEFT);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
			if (canMove(m, r.peek())) { // 中に移動できる
				move(r, m, trace, MOVE_RIGHT_TO_MIDDLE);
				retmin = countMinPath(l, m, r, trace, cur+1, min);
				if (min > retmin) min = retmin; // 最小値更新
				backTrace(l, m, r, trace);
				System.out.print("back: ");  printStack(l, m, r);
			}
		}
/*
		src = null;
		do {
			if (!l.isEmpty() && (src == null) && (lastMoved != MOVE_MIDDLE_TO_LEFT) && (lastMoved != MOVE_RIGHT_TO_LEFT))
				src = l;
			else if (!m.isEmpty() && (src != m) && (src != r) && (lastMoved != MOVE_LEFT_TO_MIDDLE) && (lastMoved != MOVE_RIGHT_TO_MIDDLE))
				src = m;
			else if (!r.isEmpty() && (src != r) && (lastMoved != MOVE_LEFT_TO_RIGHT) && (lastMoved != MOVE_MIDDLE_TO_RIGHT))
				src = r;
			else // 手詰まり
				break;

			// 次に移動可能な場所それぞれに対して試行する。試行して結果を得たら元に戻し、次を試す
			srcInt = src.peek();
			dst = null;
			if (src != l) {
				dst = l;
				if (canMove(l, srcInt)) {
					if (src == m) move(src, dst, trace, MOVE_MIDDLE_TO_LEFT);
					else move(src, dst, trace, MOVE_RIGHT_TO_LEFT);
					retmin = countMinPath(l, m, r, trace, ++cur, min);
					if (retmin < min) min = retmin;
					backTrace(l, m, r, trace);
					System.out.print("back: ");  printStack(l, m, r);
					cur--;
				}
			}
			if (src != m) {
				dst = m;
				if (canMove(m, srcInt)) {
					if (src == l) move(src, dst, trace, MOVE_LEFT_TO_MIDDLE);
					else move(src, dst, trace, MOVE_RIGHT_TO_MIDDLE);
					retmin = countMinPath(l, m, r, trace, ++cur, min);
					if (retmin < min) min = retmin;
					backTrace(l, m, r, trace);
					System.out.print("back: ");  printStack(l, m, r);
					cur--;
				}
			}
			if (src != r) {
				dst = r;
				if (canMove(r, srcInt)) {
					if (src == l) move(src, dst, trace, MOVE_LEFT_TO_RIGHT);
					else move(src, dst, trace, MOVE_MIDDLE_TO_RIGHT);
					retmin = countMinPath(l, m, r, trace, ++cur, min);
					if (retmin < min) min = retmin;
					backTrace(l, m, r, trace);
					System.out.print("back: ");  printStack(l, m, r);
					cur--;
				}
			}
		} while (true);
*/

		return min;
	}

	/*
	 * 初期状態のスタックを生成する
	 */
	public static Stack<Integer> makeInit(char[] in) {
		Stack<Integer> s = new Stack<Integer>();

		if (in.length < 1) return null;

		for (int i=in.length-1 ; i>=0 ; i--) {
			s.push(Character.getNumericValue(in[i]));
		}
		return s;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		char[] in;
		Stack<Integer> l;	// left
		Stack<Integer> m = new Stack<Integer>();	// middle
		Stack<Integer> r = new Stack<Integer>();	// right
		Stack<Integer> trace = new Stack<Integer>();	// trace, save
//		ArrayList<Stack> save;
//		int n;

		while (cin.hasNext()) {
			in = cin.nextLine().toCharArray();
			l = makeInit(in);
//			System.out.println(countMinPath(l, m, r, trace, 0, Integer.MAX_VALUE));
//			n = l.get(l.size()-1);
//			save = new ArrayList<Stack>(n);
			System.out.println(countMinPath(l, m, r, trace, 0, Integer.MAX_VALUE));
		}
		cin.close();
	}
}
