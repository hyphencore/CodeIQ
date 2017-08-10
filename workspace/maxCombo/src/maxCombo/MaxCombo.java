package maxCombo;

import java.util.Scanner;

/*
 *  題の条件の中に、「skip後にコンボ継続した場合は、再度最大2回skip可能」を明記した方が
 *  題意が明確化すると思います。
 */
public class MaxCombo {

	/*
	 * 再帰によりコンボカウントする。
	 *
	 * curIndex：入力数列の現在処理位置
	 * curMaxNum：現在コンボ中の最大数
	 * skipCount：飛ばした数
	 * array：入力数列
	 * combo：現在のコンボ数
	 *
	 * アルゴリズム：
	 * 　(1)　現在位置が配列最後に到達した場合、現在のコンボ数を返す
	 * 　(2)　末尾でなく、今の数字でコンボが継続している場合、
	 * 　　　　コンボカウントして、次の要素に移動し再帰処理（このとき、スキップ数はクリアする）
	 * 　　　　その結果と、今の数字をスキップした場合（ただし、最大2回までとする）の結果を比較し、
	 * 　　　　そのうち最大コンボを返す
	 * 　(3)　末尾でなく、今の数字でコンボが継続しない場合、スキップして再帰処理。
	 * 　　　　ただし、2回スキップ済みであればコンボが途切れるため今のコンボ数を返す。
	 */
	public static int countMaxCombo(int curIndex, int curMaxNum, int skipCount, int[] array, int combo, int[][] counted) {
		int i, ret, sRet;

		if (curIndex >= array.length) return combo;

		if (curMaxNum < array[curIndex]) {
			combo++;
//			ret = countMaxCombo(curIndex+1, array[curIndex], skipCount, array, combo); skip後は再度最大2回飛ばせるためこちらは違う。
			ret = countMaxCombo(curIndex+1, array[curIndex], 0, array, combo, counted);
			counted[skipCount][curIndex] = ret;
			for (i=skipCount ; i<2 ; i++) {
				sRet = countMaxCombo(curIndex+1, curMaxNum, i+1, array, combo-1, counted);
				if (ret < sRet) ret = sRet;
				counted[i][curIndex] = sRet;
			}
			return ret;
		}	else {
			if (skipCount < 2) {
				skipCount++;
				return countMaxCombo(curIndex+1, curMaxNum, skipCount, array, combo, counted);
			}
		}
		return combo;
	}

	//　開始位置を全要素に。
	public static int countMaxComboMain(int N, int[] array) {
		int i, j, ret;
		int countMax = 0;
		int[][] counted;

		counted = new int[3][N];
		for (i=0 ; i<=2 ; i++)
			for (j=0 ; j<N ; j++)
				counted[i][j] = 0;

		for (i=N-1 ; i>=0 ; i--) {
			ret = countMaxCombo(i, 0, 0, array, 0, counted);
			if (countMax < ret) countMax = ret;
		}
		return countMax;
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		int i, N;
		int[] array;
		String[] ret;

		while (cin.hasNext()) {
			N = Integer.parseInt(cin.nextLine());
			array = new int[N];
			ret = cin.nextLine().split(" ");
			for (i=0 ; i<N ; i++)
				array[i] = Integer.parseInt(ret[i]);
			System.out.println(countMaxComboMain(N, array));
		}
		cin.close();
	}
}
