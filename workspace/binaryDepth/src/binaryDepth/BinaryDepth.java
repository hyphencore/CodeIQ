package binaryDepth;

import java.util.Scanner;

/*
 * binary tree を構成するためのクラス。左と右に子を持つ
 */
class Node {
	int value;
	Node left, right;

	Node() {
		value = 0;
		left = null;
		right = null;
	}

	Node(int v) {
		value = v;
	}

	void setValue(int v) {
		value = v;
	}

	void setLeft(Node l) {
		left = l;
	}

	void setLeft(int l) {
		left = new Node();
		left.setValue(l);
	}

	void setRight(Node r) {
		right = r;
	}

	void setRight(int r) {
		right = new Node();
		right.setValue(r);
	}
}

public class BinaryDepth {

	public static Node findNodeByDepth(Node tree, int n, int[] step) {
		Node ret = null;

		if (step[0] == n) return tree;
		if (tree.left == null) return null;
		step[0]++;
		ret = findNodeByDepth(tree.left, n, step);
		if (ret == null) {	// not left
			if (tree.right == null) return null;
			step[0]++;
			ret = findNodeByDepth(tree.right, n, step);
		}
		return ret;
	}

	/*
	 * makeTree: parentを親とする子を作成する。再帰的に。
	 *
	 *  parentの値のノードを作成し、それをTOPとする左子と右子を作成（再帰）する。
	 *  左ノードは、parent*2、右ノードは、parent*2+1になる。
	 */
	public static Node makeTree(int m, int parent) {
		Node ret;

		if (m < parent) return null;

		ret = new Node(parent);
		ret.left = makeTree(m, parent*2);
		ret.right = makeTree(m, parent*2+1);

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		String[] ret;
		int m, n;
		Node top;
		int[] step = new int[1];

		while (cin.hasNext()) {
			line = cin.nextLine();
			ret = line.split(" ");
			m = Integer.parseInt(ret[0]);
			n = Integer.parseInt(ret[1]);
			top = makeTree(m, 1);
			step[0] = 1;
			System.out.println(findNodeByDepth(top, n, step).value);
		}
		cin.close();
	}

}
