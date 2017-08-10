package ame;

import java.util.Scanner;

public class Ame {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int m, n;

		while (cin.hasNext()) {
			line = cin.nextLine();
			m = Integer.parseInt(line.split(" ")[0]);
			n = Integer.parseInt(line.split(" ")[1]);
		}
		cin.close();
	}

}
