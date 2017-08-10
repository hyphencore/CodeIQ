package kyoukaisen;

import java.util.Scanner;

public class Kyoukaisen {
	public static int xSize = 8, ySize = 8;

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		Map m = new Map(xSize, ySize);
		int i, num;

		while (cin.hasNext()) {
			line = cin.nextLine();
			for (i=0 ; i<ySize ; i++) {
				// 先頭から2文字取得してInt変換し行の有無設定をする
				num = Integer.parseInt(line.substring(i*2, (i+1)*2), 16);
				m.setLineEnable(num, i);
			}
			// 各辺の合計を出力
			System.out.print(m.countContiguous(Map.Direction.Top)
							+ "," + m.countContiguous(Map.Direction.Bottom)
							+ "," + m.countContiguous(Map.Direction.Left)
							+ "," + m.countContiguous(Map.Direction.Right));
		}
		cin.close();
	}

}
