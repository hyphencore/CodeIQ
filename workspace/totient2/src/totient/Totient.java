package totient;

import java.util.Scanner;

// 指数の1つの項を表現する。項の係数は1固定とする。
class Shisuu {
	int base;		// 基底
	int shisuu;		// 指数

	Shisuu(int b, int s) {
		base = b;
		shisuu = s;
	}

	public void setBase(int b) { base = b; }
	public void setShisuu(int s) { shisuu = s; }
}

class SosuuMap {
	public static int SOSUU_MAX = 100000;
	int[] s = new int[SOSUU_MAX];
	int size;

	// 引数の数は素数かチェック
	public boolean isSosuu(int n) {
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

	// 2～SOSUU_MAXまでの素数を事前に配列でもっておく。
	// その際の素数の総数をsizeに保持
	SosuuMap() {
		int i;

		s[0] = 2;
		size = 1;
		for (i=3 ; i<SOSUU_MAX ; i+=2) {
			if (isSosuu(i)) {
				s[size] = i;
				size++;
			}
		}
	}
}

public class Totient {
	public static SosuuMap sm = new SosuuMap();
	public static Shisuu[] s = new Shisuu[SosuuMap.SOSUU_MAX];
	public static final int JYOSUU = 1000003;

	/*
	 * 素因数分解をする関数
	 *
	 * Shisuu配列の素因数をインデックス指定し（ある意味ハッシュ）、カウントする。
	 */
	public static void makeSoinsuBunkai(Shisuu[] ss, int n) {
		int maxLoop = (int)Math.floor(Math.sqrt(n));
		int i;

		for (i=2 ; i<=maxLoop ; i++) {
			if (n % i == 0) {
				do {
					ss[i].shisuu++;
					n = n / i;
				} while (n % i == 0);
			}
		}
		if (maxLoop < n) ss[n].shisuu++; // 最後の素因数
	}

	/*
	 * calc Totient
	 *
	 * n = Π(base(i)^shisuu(i))　{i: 素因数分解した際のi番目の素因数、iは1～kまでの範囲。kは素因数の個数}　とすると、
	 * φ(n) = n×Π(1-1/base(i))
	 *       = Π(base(i)^(shisuu(i) - 1)*(base - 1)) {i: 1～k}
	 *       の形に変形して、計算することで分数計算がなくなる。
	 */
	public static long calcTotient(Shisuu[] s, int n) {
		int i;
		long ret = 1;
		Shisuu elem;

		for (i=0 ; i<=SosuuMap.SOSUU_MAX-1 ; i++) {
			if (sm.s[i] > n) return ret; // 素数がnを越えたら終了
			else if (s[sm.s[i]].shisuu > 0) {
				elem = s[sm.s[i]];
				ret *= (myPow(elem.base, elem.shisuu-1)*(elem.base-1));
				ret %= JYOSUU;
			}
		}
		return ret;
	}

	/*
	 * オイラ―のトーシェント関数を利用した計算ルーチン
	 *
	 * 1: 1
	 * nの階乗は必ず合成数
	 */
	public static int calcTagainiso(Shisuu[] ss, int n) {
		long tResult;
		int i;

		if (n == 1) return 1;
		for (i=2 ; i<=n ; i++) {
			makeSoinsuBunkai(ss, i);
		}
		tResult = calcTotient(ss, n);
		return (int)(tResult % JYOSUU);
	}

	// ShisuuをSOSUU_MAX分だけ保持しておく。
	public static void init() {
		for (int i=0 ; i<SosuuMap.SOSUU_MAX ; i++)
			s[i] = new Shisuu(i, 0);
	}

	/*
	 * 1000003の余りを求めながらpow計算
	 */
	public static long myPow(int base, int shisuu) {
		int i;
		long ret = 1;

		for (i=0 ; i<shisuu ; i++) {
			ret *= base;
			ret %= JYOSUU;
		}
		return ret;
	}

	public static void printSoinsuBunkai(Shisuu[] s, int n) {
		int i;

		for (i=0 ; i<sm.size ; i++) {
			if (s[i].shisuu > 0)
				System.out.println(s[i].base + "^{" + s[i].shisuu + "}, ");
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n;

		while (cin.hasNext()) {
			init();
			n = Integer.parseInt(cin.nextLine());
			System.out.println(calcTagainiso(s, n));
			//printSoinsuBunkai(s, n);
		}
		cin.close();
	}
}
