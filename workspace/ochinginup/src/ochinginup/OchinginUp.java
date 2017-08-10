package ochinginup;

// 出力順番が人名順でした。。。

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class OchinginUp {
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String name, file, line;
		String[] ret;
		ArrayList<String> inLines = new ArrayList<String>();
		ArrayList<String> lastUpdatePerson = new ArrayList<String>();
		ArrayList<String> inFile = new ArrayList<String>();
		ArrayList<String> person = new ArrayList<String>();
		ArrayList<Integer> personCount = new ArrayList<Integer>();
		int i, idx = 0;

		while (cin.hasNext()) {
			inLines.add(cin.nextLine());
		}
		cin.close();

		// 入力をもとに、最後に更新した人のリストをlastUpdatePerson Arrayに生成する
		for (i=inLines.size()-1 ; i>=0 ; i--) {
			ret = inLines.get(i).split(",");
			name = ret[0];
			file = ret[1];

			if (inFile.contains(file)) { // 最後に更新した人を更新する(逆からなので最初に現れた人)
			} else {
				inFile.add(file);
				lastUpdatePerson.add(name);
			}
		}

		Collections.sort(lastUpdatePerson);	// これが必要だった。
		// lastUpdatePerson をもとに、出現回数をカウントする。結果はperson(人名)、personCount(出現数)に。
		for (i=0 ; i<lastUpdatePerson.size(); i++) {
			if (person.contains(lastUpdatePerson.get(i))) {
				idx = person.indexOf(lastUpdatePerson.get(i));
				personCount.set(idx, personCount.get(idx)+1);
			} else {
				person.add(lastUpdatePerson.get(i));
				personCount.add(1);
			}
		}

		for (i=0 ; i<personCount.size() ; i++)
			System.out.println(person.get(i) + ":"
								+ (int)Math.floor(((double)personCount.get(i)/inFile.size())*100) + "%");
	}

}
