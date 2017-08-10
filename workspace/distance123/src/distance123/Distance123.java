package distance123;

import java.util.Scanner;

public class Distance123 {
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int N, i, j, minDistance;
		int[] array;
		String[] ret;
		boolean found1, found2, found3;

		while (cin.hasNext()) {
			N = Integer.parseInt(cin.nextLine());
			ret = cin.nextLine().split(" ");

			// init
			array = new int[N];
			for (i=0 ; i<N ; i++) {
				array[i] = Integer.parseInt(ret[i]);
			}

			minDistance = Integer.MAX_VALUE;
			for (i=0 ; i<N ; i++) {
				j = i;
				found1 = false; found2 = false; found3 = false;
				while ((!found1 || !found2 || !found3) && (j < N)) {
					if (array[j] == 1) found1 = true;
					if (array[j] == 2) found2 = true;
					if (array[j] == 3) found3 = true;
					j++;
				}
				if ((found1 && found2 && found3) && (minDistance > (j - i))) minDistance = j - i;
			}
			System.out.println(minDistance);
		}
		cin.close();
	}

}
