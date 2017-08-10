package kuriagari;

import java.util.Scanner;

public class Kuriagari {
	public static int mapSize = 1000;
	public static int countMap[][] = new int[mapSize][mapSize];
	public static int kuriageMap[][] = new int[mapSize][mapSize];

	public static int kuriageCount(int a, int b, int n) {
		int i, j;
		int aTmp = a, bTmp = b;
		int count = 0;
		int aAmari, bAmari, kuriage = 0;

		for (i=1 ; i<=n ; i++) {
			aAmari = aTmp % 10;
			bAmari = bTmp % 10;
			aTmp = aTmp / 10;
			bTmp = bTmp / 10;
			if (aAmari + bAmari + kuriage >= 10) {
				count++;
				kuriage = 1;
			} else { kuriage = 0; }
		}
		return count;
	}

	public static int countKuriageMatch(int n, int c) {
		int count = 0;
		int aStart = 0, bStart = 0;
		int aEnd = (int)Math.pow(10, n), bEnd = aEnd;
		int i, j;

		for (i=aStart ; i<aEnd ; i++) {
			for (j=bStart; j<bEnd ; j++) {
				//if (kuriageCount(i, j, n) == c) count++;
				if (i*j%2 == 0) count++;
			}
		}
		return count;
	}

	public static void init() {
		int i, j;

		for (i=0 ; i<mapSize ; i++) {
			for (j=0 ; j<mapSize ; j++) {
				//countMap[i][j] = kuriageCount(i, j);
			}
		}
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner cin = new Scanner(System.in);
		String line;
		String[] ret;
		int n, c;

		//init();
		for (;cin.hasNext();) {
			// 上からのProjection
			line = cin.nextLine();
			ret = line.split(" ");
			n = Integer.parseInt(ret[0]);
			c = Integer.parseInt(ret[1]);
			System.out.println(countKuriageMatch(n, c));
		}
	}
}
