package degiseg;

import java.util.Scanner;

/*
 * 考え方
 *
 *  n=1 のときは、6のときだけ9になるため1
 *  n=2 のときは、題にもある通り21パターン
 *  n≧3 のときは、題意を満たす総数をP(n)とすると、
 *  　　　P(n) = 21×7^(n-2) + 7×P(n-2) で求められる
 *
 *  P(n)の式の考え方
 *  　あるn桁の数を逆さ設置した後の方が数字が大きくなる条件は、
 *  (1) 最下位の桁＞最上位の桁のとき（ただし、6,9が逆設置により大小変化することに注意する）
 *  (2) 最下位の桁＝最上位の桁のとき、最下位桁と最上位桁を除去した残りの数で、さらに(1)を満たすとき
 *  　　（もしそれでも満たない場合は、(2)をさらに適用・・・を残り2桁以下になるまで繰り返す）
 *
 *  (1)のﾊﾟﾀｰﾝは、最下位が、
 *  0： 逆設置により大きくなる最上位桁は存在しない
 *  1： 0のみ
 *  2： 0,1
 *  5： 0,1,2
 *  6： 0,1,2,5,6,8   （反転すると9になるため）
 *  8： 0,1,2,5,6
 *  9： 0,1,2,5　　　　（反転すると6になるため）
 *  となり、全部で21パターンになる。残りの桁はどの数でも良いため、21×7^(n-2) となる
 *
 *  (2)のパターンは、同じ数値になるパターンが7パターン。残りの桁は、最上位桁と最下位桁を除く
 *  　n-2桁を再帰的に求めればよいので、7×P(n-2)
 *
 *  これらの和が求める数
 */
public class DegiSeg {
	public static long countRevDegiSeg(long n) {
		if (n == 1) return 1;
		if (n == 2) return 21;
		else return 21 * (long)Math.pow(7, n-2) + 7*countRevDegiSeg(n-2);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		long n;

		while (cin.hasNext()) {
			n = (long)cin.nextInt();
			System.out.println(countRevDegiSeg(n));
		}
		cin.close();
	}

}
