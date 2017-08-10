package hidentare;

import java.util.Scanner;

public class Hidentare {
	public static String calcComplexity(int complx, int refac, int month, int refacbase, int ngbase) {
		int ret = 0;

		for (int i=0 ; i<month ; i++) {
			ret += complx;
			if (ret > refacbase) ret -= refac;
			if (ret > ngbase) {
				return "good bye";
			}
		}

		if (ret < 0) return "0";
		else return Integer.toString(ret);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			String[] elems = cin.nextLine().split(",");
			System.out.println(calcComplexity(Integer.parseInt(elems[0]),
											Integer.parseInt(elems[1]),
											Integer.parseInt(elems[2]),
											Integer.parseInt(elems[3]),
											75));
		}
		cin.close();
	}
}
