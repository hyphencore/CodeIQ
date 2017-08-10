package colorize;

import java.util.Scanner;

/*
 * まさかの親切心がダメだったとか・・・
 */
public class Colorize {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String src, dst, after;
		String[] dsts;
		int index;

		while (cin.hasNext()) {
			src = cin.nextLine();
			dst = cin.nextLine();
			dsts = dst.split(" ");
			after = "";
			for (int i=0 ; i<dsts.length ; i++) {
				if (dsts[i].compareTo(src) == 0) after += "[" + dsts[i] + "]";
				else {
					dst = dsts[i];
					do { // 検索対象の文字列が複数回あらわれた場合も加味
						index = dst.indexOf(src);
						if (index >= 0) {
							after += dst.substring(0, index) + "=" + dst.substring(index, index+src.length()) + "=";
							dst = dst.substring(index+src.length());
						} else {
							after += dst;
						}
					} while (index >= 0);
				}
				if (i < dsts.length-1) after += " ";

				// 複数回連続して検索文字列が現れた場合、"=="があるので、不要のため削除する
				// ex) ge検索で higege なら hi=ge==ge= となっているので、"=="を削除して hi=gege= とする
				/*
				 * ↓の処理はしない方が答え？
				 */
				// after = after.replaceAll("==", "");
			}
			System.out.println(after);
		}
		cin.close();
	}

}
