package ketawa;
import java.util.Scanner;

public class Ketawa {
	// 1の位の総和配列
	public static int[] ketawa1 = {0, 1, 3, 6, 10, 15, 21, 28, 36, 45};

	// 1-n までの桁総和を求める
	// 数値nの各桁をak（kは添え字）
	public static int calcKetawaFrom1(int n, int ketaNum) {
		int i, ai;
		int ret = 0;
		int work = 0;
		if (n < 10) {
			ret = ketawa1[n];
		} else {
			ret = (int)(n/10)*45 + ketawa1[(int)(n%10)];
			for (i=2 ; i<=ketaNum ; i++) {
				ai = ((int)(n/(int)Math.pow(10, i-1)))%10;	// i桁目の数字取得
				if (ai > 0) {
					work = ((int)(n/(int)Math.pow(10, i)))*(int)Math.pow(10, i-1)*45
							+ ketawa1[ai-1]*(int)Math.pow(10, i-1)
							+ (n%(int)Math.pow(10, i-1)+1)*ai;
				} else {
					work = ((int)(n/(int)Math.pow(10, i)))*(int)Math.pow(10, i-1)*45
							+ ketawa1[ai]*(int)Math.pow(10, i-1)
							+ (n%(int)Math.pow(10, i-1)+1)*ai;
				}
				ret += work;
			}
		}
		return ret;
	}

	// a-b の各桁の総和を求める. a<b
	public static int calcKetawaRange(int a, int b) {
		int am1, am1keta;
		int bketa;

		am1 = a - 1;
		am1keta = String.valueOf(am1).length();
		bketa = String.valueOf(b).length();
		return calcKetawaFrom1(b, bketa) - calcKetawaFrom1(am1, am1keta);
	}

	// str - end の範囲の桁総和がsearchと一致するものを2分木探索で探す
	//  direction: 0 なら、endは固定しstrを動かす
	//  direction: 1なら、strは固定しendを動かす
	//  発見したら1、なければ0を返す
	public static int findEqualKetawa(int str, int end, int search, int direction) {
		int ketawa;
		if (str > end) return 0;
		if (str == end) {
			if (str == search) return 1;
			else return 0;
		}
		ketawa = calcKetawaRange(str, end);
		if (ketawa == search) return 1;
		if (ketawa < search) return 0; // ない（最大範囲でsearchに届いていない）
		else {
			if (direction == 0) {

			}
			if (direction == 1) return findEqualKetawa(str, (int)(end/2), search, direction);
		}
	}

	// str - end の範囲の、桁和がwaになるものを前提として、
	// その条件に合致する範囲を探索する。
	public static int countEqualKetawa(int str, int end, int wa) {
		int i;
		int count = 0;

		// To範囲のそれぞれについて、waと一致するFromを探す
		for (i=str+1 ; i<end ; i++) {
			count += findEqualKetawa(1, i, wa, 0); // 1-i を初期値として、2分木探索で発見する
		}

		// from範囲のそれぞれについて、waと一致するToを探す
		for (i=str+1 ; i<end ; i++) {
			count += findEqualKetawa(i, i+2500, wa, 1); // 1-i を初期値として、2分木探索で発見する
		}
		return count;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		String[] rets;
		int a, b, am1, am1keta;

		while (cin.hasNext()) {
			line = cin.nextLine();
			rets = line.split(" ");
			a = Integer.parseInt(rets[0]);
			b = Integer.parseInt(rets[1]);
			System.out.println(calcKetawaRange(a, b));
		}
		cin.close();
	}
}
