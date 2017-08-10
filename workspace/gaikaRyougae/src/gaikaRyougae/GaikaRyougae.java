package gaikaRyougae;

import java.util.Scanner;

class Money {
	// 各貨幣の使用枚数をカウントする
	int[] ammount = {10000, 5000, 2000, 1000, 500, 100, 50, 10, 5, 1};	// 額の配列
	int[] m = new int[ammount.length]; // 各額の使用枚数
	int ammountLength;	// 額の種類数
	int totalCount;		//　合計使用枚数

	Money() {
		int i;
		for (i=0 ; i<m.length ; i++) m[i] = 0;
		totalCount = 0;
		ammountLength = ammount.length;
	}

	public void useCount(int addc, int index) {
		m[index] += addc;
		totalCount += addc;
	}

	public void decCount(int subc, int index) {
		m[index] -= subc;
		totalCount -= subc;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getAmmount(int index) {
		return ammount[index];
	}

	public int getAmmountLength() {
		return ammountLength;
	}
}

class GaikaRyougae {
	public static void getRyougaeMaisu1(Money m, int restMoney) {
		boolean fin = false;
		int i, count = 0, t;

		for (i=0 ; i<m.ammountLength ; i++) {
			t = (int)Math.floor(restMoney/m.getAmmount(i));
			m.useCount(t, i);
			restMoney -= (t * m.getAmmount(i));
		}
	}

	public static int getRyougaeMaisu(int restMoney) {
		Money m = new Money();
		int i;

		// 最初に最大種類になるように低金額から1つずつ使用する
		for (i=m.ammountLength-1 ; i>=0 ; i--) {
			if (restMoney >= m.getAmmount(i)) {
				m.useCount(1, i);
				restMoney -= m.getAmmount(i);
			}
		}

		//　あとは、最小枚数になるように選択する
		getRyougaeMaisu1(m, restMoney);
		return m.getTotalCount();
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int rest;

		while (cin.hasNext()) {
			rest = Integer.parseInt(cin.nextLine());
			System.out.println(getRyougaeMaisu(rest));
		}
		cin.close();
	}

}
