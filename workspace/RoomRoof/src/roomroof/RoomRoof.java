package roomroof;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

class Ret {
	BigDecimal a;
	BigDecimal b;

	Ret() {
		a = null;
		b = null;
	}

	Ret(BigDecimal asrc, BigDecimal bsrc) {
		set(asrc, bsrc);
	}

	void set(BigDecimal asrc, BigDecimal bsrc) {
		a = asrc;
		b = bsrc;
	}
}

public class RoomRoof {
	/*
	 * 【処理概要】
	 * 題より、P(n) = (2 + √3)^n * 1 で面積が求められる。
	 *  したがって、言い換えれば、P(n)の整数部をA(n)、実数部√3の係数をB(n)とすると
	 *    P(n) = A(n) + B(n)*√3　 で表現でき、2+√3をかけて展開すると、
	 *     A(n) = 2*A(n-1) + 3*B(n-1)
	 *     B(n) = A(n-1) + 2*B(n-1)
	 *    で各係数を求められる。
	 *    よって、　A(0) = 1, B(0) = 0　を初期値とする漸化式でA(n)とB(n)を求める。
	 */
/*	public static Ret calc(int n) {
		BigDecimal a = new BigDecimal(1);
		BigDecimal b = new BigDecimal(0);
		BigDecimal const2 = new BigDecimal(2);
		BigDecimal const3 = new BigDecimal(3);
		BigDecimal tmp;
		Ret ret = new Ret();
		int i;

		for (i=1 ; i<=n ;i++) {
			tmp = a.multiply(const2).add(b.multiply(const3)); // A(n) = 2*A(n-1) + 3*B(n-1)
			b = a.add(b.multiply(const2));	// B(n) = A(n-1) + 2*B(n-1)
			a = tmp;
		}
		ret.a = a;
		ret.b = b;
		return ret;
	}
*/
	/*
	 * calcArea(n): P(n)を求め、その結果のmod100万をもとめる
	 */
/*	public static int calcArea(int n) {
		Ret ret1; // A(0)=1, B(0)=0
		BigDecimal const3 = new BigDecimal(3);
		BigDecimal const1000000 = new BigDecimal(1000000);
		BigDecimal sqrt3, ret;
		MathContext mc = new MathContext(0, RoundingMode.DOWN);
		if (n == 0) return 1;
		ret1 = calc(n); // a + b√3　の形式でP(n)の計算結果を求める
		sqrt3 = sqrtBD(const3,ret1.b.toPlainString().length()+2); // bの整数桁分だけ√3の精度が必要
		ret = ret1.a.add(ret1.b.multiply(sqrt3)).round(mc).remainder(const1000000);	// INT(a + b√3) mod 100万

		return ret.intValue();
	}
*/
	/*
	 * calcArea(n): P(n)を求め、その結果のmod100万をもとめる
	 */
	public static int calcArea(int n) {
		Ret ret1; // A(0)=1, B(0)=0
		BigDecimal const3 = new BigDecimal(3);
		BigDecimal const1000000 = new BigDecimal(1000000);
		BigDecimal sqrt3, ret;
		MathContext mc = new MathContext(0, RoundingMode.DOWN);
		if (n == 0) return 1;
		ret1 = calcP(n); // a + b√3　の形式でP(n)の計算結果を求める
		
		sqrt3 = sqrtBD(const3,ret1.b.toPlainString().length()*2); // bの整数桁分だけ√3の精度が必要
		ret = ret1.a.add(ret1.b.multiply(sqrt3)).round(mc).remainder(const1000000);	// INT(a + b√3) mod 100万
//		ret = ret1.a.add(ret1.b.multiply(sqrt3)).(const1000000);	// INT(a + b√3) mod 100万

		return ret.intValue();
	}

	// BigDecimal sqrt
	// newton法で平方根を求める。x{n+1} = x{n} - f{x}/F'{x}
	public static BigDecimal sqrtBD(BigDecimal base, int scale){
		BigDecimal b = new BigDecimal(Math.sqrt(base.doubleValue()), MathContext.DECIMAL64);
		BigDecimal b2 = new BigDecimal(2);
        int i;

        if(scale < 17) return b;	// doubleゆえにまんま返す
        for(i = 16; i < scale; i *= 2){
        	// f{x} = x^2 - base, f'{x] = 2x ===> x{n+1} = x{n} - (x^2-base)/2x
            b = b.subtract(b.multiply(b).subtract(base).divide(
                    b.multiply(b2), scale, BigDecimal.ROUND_HALF_EVEN));
        }
        return b;
    }

	// (a+b√3)*(c+d√3) を計算する
	public static Ret calcPow2(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d) {
		Ret r = new Ret();
		BigDecimal const3 = new BigDecimal(3);
		//
		r.a = a.multiply(c).add(b.multiply(d).multiply(const3));
		r.b = a.multiply(d).add(b.multiply(c));
		return r;
	}

	// (a+b√3)のN乗を計算する
	public static Ret calcPowN(BigDecimal a, BigDecimal b, int n) {
		Ret r = new Ret(a, b);
		int i;

		if (n == 0) return new Ret(new BigDecimal(1), new BigDecimal(0));
		for (i=1 ; i<=n-1 ; i++) {
			r = calcPow2(r.a, r.b, a, b);
		}
		return r;
	}

	/*
	 *  P(n) = (2+√3)^n であるが、nが大きくなると計算量が多くなるため、
	 *  P(100) = {(2+√3)^10}^10
	 *  P(1000) = [{(2+√3)^10}^10]^10
	 *    :
	 *  で計算することでほとんど計算量が必要なくなることを利用する。
	 *
	 *  ex) n=987654 のときは、
	 *   P(987654) = (2+√3)^987654 = (2+√3)^900000*(2+√3)^80000*(2+√3)^7000*(2+√3)^600*(2+√3)^50*(2+√3)^4
	 *        = (2+√3)^10^10^10^10^10^9
	 *           * (2+√3)^10^10^10^10^8
	 *           * (2+√3)^10~10~10^7
	 *           * (2+√3)^10^10^6
	 *           * (2+√3)^10^5
	 *           * (2+√3)^4
	 */
	public static Ret calcP(int n) {
		int i, amari;
		int keta = Integer.toString(n).length();
		Ret[] pows10 = new Ret[keta]; // (2+√3)^10, ^100, ^1000 ... を格納
		Ret r = new Ret(new BigDecimal(0), new BigDecimal(0)), t;
		BigDecimal const2 = new BigDecimal(2);
		BigDecimal const1 = new BigDecimal(1);

		pows10[0] = calcPowN(const2, const1, 10); // create (2+√3)^10  ... this is a base.
		// (2+√3)^100, ^1000 ... を先に生成
		for (i=1 ; i<keta-1 ; i++) pows10[i] = calcPowN(pows10[i-1].a, pows10[i-1].b, 10);

		// (pows10[計算する桁])^計算する桁の係数) の全桁加算が求める数
		for (i=1 ; i<=keta ; i++) {
			amari = (int)Math.floor((n % (int)Math.pow(10, i))/(int)Math.pow(10, i-1));
			if (i == 1) r = calcPowN(const2, const1, amari);
			else {
				t = calcPowN(pows10[i-2].a, pows10[i-2].b, amari);
				r = calcPow2(r.a, r.b, t.a, t.b);
			}
		}
		return r;
	}

	public static BigDecimal Combination(int n, int k) {
		if (k>n-k) k=n-k;
		BigDecimal B[][]=new BigDecimal[n+1] [k+1];

		for (int i = 0; i <= n; i++) {
			int min;
			if (i>=k) min=k;
			else min=i;

			for (int j = 0; j <= min; j++) {
				if (j == 0 || j == i) B[i][j] =new BigDecimal(1);
				else {
					if(j>i-j) B[i][j]=B[i][i-j];
					else B[i][j] = B[i - 1][j - 1].add(B[i - 1] [j]);
				}
			}
		}
		BigDecimal div=new BigDecimal(142857);
		return B[n][k].remainder(div);
	}

	public static BigDecimal calcBN(int n) {
		int k = (int)Math.floor(n/2);
		int l;
		BigDecimal ret = new BigDecimal(0);
		BigDecimal const2 = new BigDecimal(2);
		BigDecimal const3 = new BigDecimal(3);

		if (n<=1) return new BigDecimal(n);
		for (l=1 ; l<=k ; l++) {
			ret = ret.add(Combination(n, 2*l-1).multiply(const2.pow(n-2*l+1)).multiply(const3.pow(l-1)));
		}
		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int n;

		while (cin.hasNext()) {
			line = cin.nextLine();
			n = Integer.parseInt(line);
			System.out.println(calcArea(n));
		}
		cin.close();
	}

}
