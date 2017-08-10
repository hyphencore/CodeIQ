package totient;

import java.math.BigInteger;
import java.util.Scanner;

// 指数の1つの項を表現する。項の係数は1固定とする。
class Shisuu {
	BigInteger base;		// 基底
	int shisuu;		// 指数

	Shisuu(BigInteger b, int s) {
		base = b;
		shisuu = s;
	}

	public void setBase(BigInteger b) { base = b; }
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
	 *       = Π(base(i)^shisuu(i) - base(i)^(shisuu(i)-1)) {i: 1～k}
	 *       の形に変形して、計算することで分数計算がなくなる。
	 */
	public static BigInteger calcTotient(Shisuu[] s, int n) {
		int i;
		BigInteger ret = new BigInteger("1");
		BigInteger const1 = new BigInteger("1");
		Shisuu elem;

		for (i=0 ; i<=SosuuMap.SOSUU_MAX-1 ; i++) {
			if (sm.s[i] > n) return ret; // 素数がnを越えたら終了
			else if (s[sm.s[i]].shisuu > 0) {
				elem = s[sm.s[i]];
				// base(i)^shisuu(i) - base(i)^(shisuu(i)-1) の積を累積していく
/*				ret = ret.multiply(new BigInteger(Integer.toString(elem.base)).pow(elem.shisuu).subtract(
							new BigInteger(Integer.toString(elem.base)).pow(elem.shisuu-1)));  */

				if (i < 50) {
					ret = ret.multiply(myPow(elem.base, elem.shisuu-1).multiply(elem.base.subtract(const1)));
				} else {
					ret = ret.multiply(elem.base.pow(elem.shisuu-1).multiply(elem.base.subtract(const1)));
				}
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
		BigInteger tResult;
		int i;
		long start, end;

		if (n == 1) return 1;
		start = System.currentTimeMillis();
		for (i=2 ; i<=n ; i++) {
			makeSoinsuBunkai(ss, i);
		}
		end = System.currentTimeMillis();
		System.out.println("makeSoinsu: " + (end-start));
		start = System.currentTimeMillis();
		tResult = calcTotient(ss, n);
		end = System.currentTimeMillis();
		System.out.println("calcTotient: " + (end-start));
		return tResult.mod(new BigInteger("1000003")).intValue();
	}

	// 引数の数は素数かチェック
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

	// ShisuuをSOSUU_MAX分だけ保持しておく。
	public static void init() {
		for (int i=0 ; i<SosuuMap.SOSUU_MAX ; i++)
			s[i] = new Shisuu(new BigInteger(Integer.toString(i)), 0);
	}

	public static BigInteger myPow(BigInteger base, int shisuu) {
		int amari, keta = 0;
		BigInteger pow = new BigInteger(base.toByteArray());
		BigInteger ret = new BigInteger("1");

		while (shisuu > 0) {
			amari = shisuu % 10;
			if (keta > 0) pow = pow.pow(10);	// 前の10乗を求める
			ret = ret.multiply(pow.pow(amari));
			shisuu = (int)Math.floor(shisuu/10);
			keta++;
		}
		return ret;
	}

	public static void printSoinsuBunkai(Shisuu[] s, int n) {
		int i;

		for (i=0 ; i<sm.size ; i++) {
			if (s[i].shisuu > 0)
				if (i<10)
					System.out.println(s[i].base + "^{" + s[i].shisuu + "}, " + s[i].base.pow(s[i].shisuu).toString());
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n;

		while (cin.hasNext()) {
			init();
			n = Integer.parseInt(cin.nextLine());
			System.out.println(calcTagainiso(s, n));
			printSoinsuBunkai(s, n);
		}
		cin.close();
	}
}
