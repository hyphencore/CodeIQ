package card4;

import java.util.Scanner;

public class Card4 {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int min, max, i, a;
		boolean exist0;

		while (cin.hasNext()) {
			line = cin.nextLine();
			min = 99; max = 0; exist0 = false;
			for (i=0 ; i<=3 ; i++) {
				a = Integer.parseInt(line.substring(i,i+1));
				if (a == 0) exist0 = true;
				else {
					if (a < min) min = a;
					if (a > max) max = a;
				}
			}
			if (exist0) System.out.println(min);
			else System.out.println(max);
		}
		cin.close();

	}

}
