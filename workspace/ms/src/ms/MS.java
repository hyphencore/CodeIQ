package ms;

import java.util.Scanner;

public class MS {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String[] ret;
		int genten = 0, rank, review, count = 0;

		while (cin.hasNext()) {
			ret = cin.nextLine().split(",");
			rank = Integer.parseInt(ret[0]);
			review = Integer.parseInt(ret[1]);
			if (rank >= 4) genten += (rank - 3);
			if (review <= 4) genten += (5 - review);
			count++;
		}
		if (count >= 4) genten += (count - 3);
		System.out.println(genten);
		cin.close();
	}

}
