package premiumDay;

import java.util.Calendar;
import java.util.Scanner;

public class PremiumDay {
	/*
	 * 指定年月の末日取得
	 */
	public static int getLastDay(int y, int m) {
		Calendar cal = Calendar.getInstance();

		cal.clear();
		cal.set(Calendar.YEAR, y);
	    cal.set(Calendar.MONTH, m-1);

	    return cal.getActualMaximum(Calendar.DATE);
	}

	/*
	 * 指定年月日の曜日取得（0:日曜～6：土曜）
	 */
	public static int getWeek(int y, int m, int d) {
		Calendar cal = Calendar.getInstance();

		cal.set(y, m-1, d);

	    return cal.get(Calendar.DAY_OF_WEEK)-1;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		String[] ret;
		int y, m, d, w, lastDayWeek, diffW;

		while (cin.hasNext()) {
			line = cin.nextLine();
			ret = line.split(",");
			y = Integer.parseInt(ret[0]);
			m = Integer.parseInt(ret[1]);
			w = Integer.parseInt(ret[2]);

		    //Calendarに年と月をセット
		    d = getLastDay(y, m);
		    lastDayWeek = getWeek(y, m, d);
		    diffW = lastDayWeek - w;
		    if (diffW < 0) diffW += 7;
			System.out.println(y + String.format("%02d", m) + String.format("%02d", d - diffW));
		}
		cin.close();
	}

}
