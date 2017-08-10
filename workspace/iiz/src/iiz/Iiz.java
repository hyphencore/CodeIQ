package iiz;

import java.util.Scanner;

public class Iiz {
	public static void print(int n) {
		for (int y=0 ; y<n ; y++) {
			for (int x=0 ; x<n ; x++) {
				if (y==0 || y==n-1) System.out.print("z");
				else {
					if (x == n-y-1) System.out.print("z");
					else System.out.print(".");
				}
			}
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int n;

		while (cin.hasNext()) {
			n = cin.nextInt();
			if (n % 2 == 0) System.out.println("invalid");
			else print(n);
		}
		cin.close();
	}

}
