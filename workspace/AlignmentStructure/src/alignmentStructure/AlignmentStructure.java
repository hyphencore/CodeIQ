package alignmentStructure;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * StructureInformationクラス
 *
 * 1つの構造体情報を保持するクラス。メンバに文字、アラインサイズ、構造体サイズを持つ。
 */
class StructureInformation {
	private char c;
	private int alignment;
	private int size;

	/*
	 * Constructor
	 */
	StructureInformation(char cin, int a, int s) {
		c = cin;
		alignment = a;
		size = s;
	}

	/*
	 * 文字を返す
	 */
	public char getChar() {
		return c;
	}

	/*
	 * アラインサイズを返す
	 */
	public int getAlignment() {
		return alignment;
	}

	/*
	 * 構造体サイズを返す
	 */
	public int getSize() {
		return size;
	}
}

/*
 * 構造体情報のリスト集合クラス
 *
 * 構造体情報を集約するクラス。題意の構造体を内部的に保持する。
 */
class StructureInformationList {
	private ArrayList<StructureInformation> stList;

	/*
	 * Constructor
	 */
	StructureInformationList() {
		stList = new ArrayList<StructureInformation>();

		stList.clear();
		stList.add(new StructureInformation('C', 1, 1));
		stList.add(new StructureInformation('S', 2, 2));
		stList.add(new StructureInformation('I', 4, 4));
		stList.add(new StructureInformation('L', 4, 8));
		stList.add(new StructureInformation('D', 8, 8));
		stList.add(new StructureInformation('M', 16, 16));
	}

	/*
	 * 指定文字のリスト中の位置を返す
	 */
	private int getIndex(char c) {
		for (int i=0 ; i<stList.size() ; i++) {
			if (c == stList.get(i).getChar()) return i;
		}
		return -1;
	}

	/*
	 * 指定文字の長さを返す
	 */
	public int getSize(char c) {
		int index;

		index = getIndex(c);
		if (index >= 0) return stList.get(index).getSize();
		return 0;
	}

	/*
	 * 現在の長さと、次に来る文字を与えることで、パディング後の長さを返す
	 *
	 * {Int(現在の長さ÷アラインサイズ)+1}*アラインサイズ+構造体サイズ　が次の構造体含むパディング後の長さ
	 *
	 */
	public int getLengthWithPadding(int currentLength, char c) {
		StructureInformation si;
		int index;

		index = getIndex(c);
		if (index >= 0) {
			si = stList.get(index);
			if (currentLength == 0) return si.getSize();
			else return (((currentLength-1)/si.getAlignment())+1)*si.getAlignment()+si.getSize();
		} else {
			return 0;
		}
	}
}

public class AlignmentStructure {
	public static int getLength(String str) {
		StructureInformationList sil = new StructureInformationList();

		char[] chars = new char[str.length()];
		int ret = 0;

		str.getChars(0, str.length(), chars, 0);
		for (int i=0 ; i<chars.length ; i++) {
			ret = sil.getLengthWithPadding(ret, chars[i]);
		}
		return ret-sil.getSize(chars[chars.length-1]);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);

		while (cin.hasNext()) {
			System.out.println(getLength(cin.nextLine()));
		}
		cin.close();
	}

}
