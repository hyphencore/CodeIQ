package crontab;

import java.util.Scanner;

public class Crontab {
	public static int getHours(String hourString, boolean hours[]) {
		String[] literals;			// 時間指定のリテラル（全体）
		String[] oneHourLiterals;;	// 1つの時間指定リテラル
		String[] hourRange;			// 時間範囲指定"-"のリテラル
		int from, to, span;
		int count = 0;

		literals = hourString.split(",");
		for (int i=0 ; i<literals.length ; i++) {
			oneHourLiterals = literals[i].split("/");
			if (oneHourLiterals.length > 1) { // x時間ごとの指定あり
				span = Integer.parseInt(oneHourLiterals[1]);
				if (oneHourLiterals[0].compareTo("*") == 0) {
					from = 0; to = 23;
				} else {
					hourRange = oneHourLiterals[0].split("-");
					from = Integer.parseInt(hourRange[0]);
					if (hourRange.length > 1) to = Integer.parseInt(hourRange[1]);
					else to = from;
				}
			} else { // 時間ごとの指定なし
				span = 1;
				hourRange = oneHourLiterals[0].split("-");
				from = Integer.parseInt(hourRange[0]);
				if (hourRange.length > 1) to = Integer.parseInt(hourRange[1]);
				else to = from;
			}
			for (int j=from ; j<=to ; j+=span)
				hours[j] = true;
		}

		for (int i=0 ; i<=23 ; i++)
			if (hours[i]) count++;
		return count;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line, hourString;
		boolean hours[] = new boolean[24];
		int hourCount, count, i;

		while (cin.hasNext()) {
			line = cin.nextLine();
			hourString = line.split(" ")[1];
			for (i=0 ; i<=23 ; i++) hours[i] = false;
			hourCount = getHours(hourString, hours);
			count = 0;
			for (i=0 ; i<=23 ; i++) {
				if (hours[i]) {
					count++;
					System.out.print(Integer.toString(i));
					if (count < hourCount) System.out.print(" ");
				}
			}
			System.out.println("");
		}
		cin.close();
	}

}
