package whiteDay;

import java.util.Scanner;

/*
 * ベースの考え方：本命の個数を先に決めた方が探索パターンが少なくなるため、
 * 　本命個数→義理個数→義務個数　の順で数を決定し、カウントする。
 * 　※義務個数は、本命個数と義理個数が決定されれば決定するため、本命と義理のパターンをカウントすればよい。。
 *
 * 義務、義理、本命それぞれp, q, r個とし、貰いトータルm個、返しn個とすると、
 * p+q+r=m, p+2q+3r=n　は必ず満たす必要がある。2式からpを消すと、
 * q=n-m-2rとなり、rが決まると必ずqも一意に決まることになる。
 * また、p+q+r=mなので、r、qが決まればpも決まる。したがって、rの取りうる値を判定すれば、
 * それが全体のﾊﾟﾀｰﾝ数となる。あとは、rの閾値を考える。
 *
 * q=n-m-2r>=0 すなわち、r<=(n-m)/2
 * また、p = m-q-r = m-(n-m-2r)-r = 2m-n+r>=0 すなわち、r>=n-2m, かつ r>=0
 *
 * これをまとめると、
 * (n-2mと0の大きい方)≦r≦(n-m)/2 の範囲で整数となる個数を数えればよい。
 * よって、Int((n-m)/2) - (n-2m or 0) + 1 が求める数
 */
public class WhiteDay {
	public static int countWhiteDayOkaeshi(int m, int n) {
		return (int)Math.floor((n-m)/2) - (n-2*m < 0 ? 0 : n-2*m) + 1;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int m, n;

		while (cin.hasNext()) {
			line = cin.nextLine();
			m = Integer.parseInt(line.split(" ")[0]);
			n = Integer.parseInt(line.split(" ")[1]);
			System.out.println(countWhiteDayOkaeshi(m, n));
		}
		cin.close();
	}

}
