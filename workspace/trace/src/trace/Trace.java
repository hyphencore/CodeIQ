package trace;

import java.util.Scanner;

public class Trace {
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		char[] command;
		char[][] tb = new char[9][9];
		int i, j, currentX, currentY;

		while (cin.hasNext()) {
			command = cin.nextLine().toCharArray();
			currentX = 0;
			currentY = 0;
			// init
			for (i=0 ; i<9 ; i++)
				for (j=0 ; j<9 ; j++) tb[i][j] = 'x';

			tb[0][0] = 'Y';
			for (i=0 ; i<command.length ; i++) {
				if (command[i] == '>') currentX++;
				else if (command[i] == 'v') currentY++;
				else if (command[i] == '<') currentX--;
				else if (command[i] == '^') currentY--;
				tb[currentY][currentX] = 'Y';
			}
			for (i=0 ; i<9 ; i++) {
				for (j=0 ; j<9 ; j++)
					System.out.print(tb[i][j]);
				System.out.println("");
			}
		}
		cin.close();
	}

}
