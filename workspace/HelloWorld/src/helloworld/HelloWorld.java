package helloworld;

import java.util.Scanner;

public class HelloWorld {
	// Combination nCr
	private static int calcC(int l, int r) {
		int i;
		int lc=1,rc=1;
		if ((r == 0)||(l == 0)) return 1;
		for (i=1 ; i<=r ; i++) {
			lc = lc * l;
			l--;
		}
		for (i=2 ; i<=r ; i++) rc = rc * i;
		return lc/rc;
	}

	/* l: 今残っている人数
	 * k: 現在の試行回数
	 * m: もともとの人数
	 * n: 決着つく回数
	 *
	 * 考え方：
	 *    (1) 試行回数が指定回数なら3パターン返す
	 *    (2) 試行回数を超えている（本来ありえない）なら0を返す
	 *    (3)　残り1人なら0を返す（本来ありえない）
	 *    (4)　2人残っている場合は、あいこ3パターン×次以降の試行回数（人数はそのまま）を求め、
	 *   　　　　　　　3人以上残っている場合は、（同手あいこ3パターン＋異種手あいこのﾊﾟﾀｰﾝ）×次以降の試行回数（人数はそのまま）を求める
	 *   　　　(5)　(4)に、試合終了しない人数の減り方全パターンにおいて、それぞれパターンでの、負け3パターン（人数に無関係）×次以降の試行回数を加算
	 *   　　　(6)　(5)を返す
	 */
	private static int jankenPattern(int l, int k, int m, int n) {
		int p, c;
		if (n == k) return 3;
		if (n < k) return 0;
		if (l <= 1) return 0;
		c = 0;
		if (l == 2) {
			c = 3 * jankenPattern(l, k+1, m, n);
		} else {
			//c = (int) (3 + Math.pow(3d, (double)(l-3))) * jankenPattern(l, k+1, m, n);
			c = (int) (3 + calcC(3+l-3-1, l-3)) * jankenPattern(l, k+1, m, n);
		}

		for (p=1 ; p<=l-2 ; p++) {
			c = c + 3 * jankenPattern(l-p, k+1, m, n);
		}
		return c;
	}

	public static void main(String[] args) {
		Scanner cin=new Scanner(System.in);
        String line;
        String[] elems;
        for(;cin.hasNext();){
            line=cin.nextLine();
            elems = line.split(" ", 0);
            System.out.println(jankenPattern(Integer.parseInt(elems[0]),
            									1,
            									Integer.parseInt(elems[0]),
            									Integer.parseInt(elems[1])));
        }
	}

}
