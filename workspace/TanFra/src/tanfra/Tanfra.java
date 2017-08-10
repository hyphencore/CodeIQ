package tanfra;

import java.util.Scanner;

/*
 * ベースの考え方
 *
 *  tan(α)=1/a, tan(β)=1/b とするとき、tan(α+β)=(a+b)/(ab-1) となる。
 *  分子が1になることから、(ab-1)は(a+b)を約数を持つため、(ab-1) = k*(a+b) (kは自然数)と表わせ、
 *  aをbで表すと、a=(bk+1)/(b-k) ・・・(1)
 *
 *  α＋β≧m　かつ　α＜βであるから、β＞m/2である。つまり、b＜1/tan(m/2)である。
 *  また、α＜βからa＞bであり、a,bは自然数から、aの最小値は2、bの最小値は1である。
 *  よってbは、1≦b＜1/tan(m/2) の範囲の自然数となる。・・・(2)
 *
 *  一方、a<bより、(bk+1)/(b-k) < b となり、k>(b^2-1)/(2b)
 *  また、aは正より、b-k>0すなわち k < b
 *  よって、(b^2-1)/(2b) < k < b ・・・(3)の範囲の自然数kにおいて、aが整数値となる（(bk+1)が(b-k)で割り切れる）
 *  aを求める。
 *  ただし、上記範囲には、α＋β≧mを満たさないkの範囲も含まれている可能性がある。それを考慮すると、
 *  α≧m-arctan(1/b) すなわち 1/a≧tan(m-arctan(1/b)) すなわち a≦1/(tan(m-arctan(1/b)))・・・(4)も満たす必要がある。
 *  ※ただし、β≧mのとき、つまり、1/b≧tan(m)・・・(5)のときaは任意で良いため、(4)は考慮せず(3)の範囲のみでよい
 *
 *  以上をまとめると、
 *  (2)の範囲の自然数bそれぞれについて、(3)の範囲の自然数kそれぞれに(bk+1)mod(b-k)=0となるaを求め、
 *  もし(5)を満たす場合は無条件にカウントし、そうでなければそのaが(4)の範囲内であるなら
 *  対象としてカウントすればよい。
 */

public class Tanfra {

	public static int F(double m) {
		int ret = 0;
		double a;

		for (double b=1 ; b<1/Math.tan(m/2) ; b++) {
			for (double k=Math.floor((b*b-1)/(2*b))+1 ; k<b ; k++) {
				if ((b*k+1) % (b-k) == 0) {
					a = (b*k+1)/(b-k);
					if (b <= ((double)1/Math.tan(m))) ret++;
					else {
						if (a <= (double)1/Math.tan(m-Math.atan(1/b))) {
							ret++;
						}
					}
				}
			}
		}

		return ret;
	}
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		double m;

		while (cin.hasNext()) {
			m = cin.nextFloat();
			System.out.println(F(m));
		}
		cin.close();
	}

}
