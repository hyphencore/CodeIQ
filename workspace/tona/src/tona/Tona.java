package tona;

import java.util.Scanner;

/*
 * 【考え方】
 * 
 * 最大となるトーナメントは、最初の勝者に対して1人ずつ対戦させ、勝者を1人ずつ決定し、最終1人の勝者にする方法
 * 最小となるトーナメントは、2人グループに分け、それぞれのグループの勝者を決め、
 * 　　　　　　　　　　　　　勝者同士を再度2人グループに分け、最終1人を決定する方法
 *
 * 前者は、対戦するごとに、勝者の対戦相手の対戦数は減少していくため階差数列の和。n*(n+1)/2-1 で求まる。
 *
 * 後者は、
 *  (1) 1人増えると奇数人になる場合、末端のトーナメント階層が1増える。
 *     全体試合数で言うと、最終グループ2人が+1と、その階層試合分1人分が増えることになる。
 *  (2) 1人増えると偶数人になる場合、単独だった人の試合数が+1と、その階層試合分+1分の1人分が増える。
 *  
 * (1)、(2) どちらの場合も、nが1増えると、Int(log2(n-1))+2 だけ増える。
 * したがって、2～nまでの増分を加算したものが求める試合数。
 */
public class Tona {
	// 最大試合数
	public static int getMaxTotal(int n) {
		return n*(n+1)/2-1;
	}

	// 最小試合数
	public static int getMinTotal(int n) {
		int ret = 0;

		for (int i=2 ; i<=n ; i++) {
			ret += (Math.log(i-1)/Math.log(2.0)+2);	// log2(n) = loge(n)/loge(2) で変換計算する
		}

		return ret;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n, max, min;

		while (cin.hasNext()) {
			n = cin.nextInt();
			max = getMaxTotal(n);
			min = getMinTotal(n);
			System.out.println(min + " " + max);
		}
		cin.close();
	}

}
