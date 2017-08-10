package Projection3D;
import java.util.Scanner;

public class Projection3D {

	public static final int DIMENSION = 3;

	// Projection の各有無を保持。Trueなら存在、Falseなら存在せず。
	// 各方向から見たプロジェクションの添え字は、上から見た:0,、右から見た：1、前から見た：2とする。
	// 各プロジェクションの2次元平面は、上から下方向を0,1,2、左から右を0,1,2とする。
	//   まとめると、各プロジェクションの状態は、[どのプロジェクションか][下方向の位置][右方向の位置]で表現する
	public static boolean Prj[][][] = new boolean[DIMENSION][DIMENSION][DIMENSION];

	// 考え方：3Dキューブを考えたとき、前面から見て、
	//       横方向をx軸（右がプラス）、縦方向をy軸（前面方向がプラス）、高さ方向をz軸（底面方向がプラス）とする
	//      Cubeの各1要素の状態（有り無し）を保持する
	//      Cube[z][y][x]の次元対応とする
	public static boolean Cube[][][] = new boolean[DIMENSION][DIMENSION][DIMENSION];

	//  Projectionの状態から、Cubeの各要素が確定される。その状態を保持するもの。
	//　Trueならその1要素は有り無しが確定している。Falseならどちらの可能性もあることを示す。
	//　各要素の有り無しはCube配列のTrue/Falseによる。次元のインデックス対応関係はCube配列と同一。
	public static boolean EstCube[][][] = new boolean[DIMENSION][DIMENSION][DIMENSION];

	// 0 or 1 or true or false の文字列をbooleanで返す
	public static boolean getStrToBool(String str) {
		String uStr = str.toUpperCase();
		if (uStr.equals("0") || uStr.equals("FALSE")) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	public static void printCube(boolean[][][] cb) {
		int x, y, z;

		for (z=0 ; z<DIMENSION ; z++) {
			for (y=0 ; y<DIMENSION ; y++) {
				System.out.print("(" + Integer.toString(z) + "," + Integer.toString(y) + ",x) = (");
				for (x=0 ; x<DIMENSION ; x++) {
					System.out.print(Boolean.toString(cb[z][y][x]));
					System.out.print(",");
				}
				System.out.println(")");
			}
		}
	}

	/*
	 * firstEst()
	 *   各Projection の条件をもとに、各ブロックの有り無しを決定づける。Projectionだけでは不確定なブロックも存在する。
	 *
	 *   考え方：　以下条件にあてはまる場合、各ブロックの有無が確定する
	 * 		(1)　上からのプロジェクションで、存在する箇所は、最下段は必ず存在する
	 * 		(2)　上からのプロジェクションで、存在しない箇所は、その個所のすべてのz座標において存在しない
	 * 		(3)　右からのプロジェクションで、存在しない箇所は、その個所のすべてのx座標において存在しない
	 * 		(4)　前面からのプロジェクションで、存在しない箇所は、その個所のすべてのy座標において存在しない
	 * 		(5)　右からのプロジェクションで、存在する場合に、その個所のx軸のブロックのうち2つのブロックが存在しないことが確定している場合、
	 * 			残りのx軸1ブロックは必ず存在する
	 * 		(6)　前面からのプロジェクションで、存在する場合に、その個所のy軸のブロックのうち2つのブロックが存在しないことが確定している場合、
	 * 			残りのy軸1ブロックは必ず存在する
	 * 		(7)　最上段または中段にブロックが存在する場合、そのブロックの下段には必ずブロックが存在する
	 */
	public static void firstEstablished() {
		int j, k;
		int x, y, z;

		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				// (1)　上からのPrjがあるということは、1段目は必ず存在する
				if (Prj[0][j][k]) {
					Cube[2][j][k] = true;
					EstCube[2][j][k] = true;
				} else {
					// (2)　上からのPrjがないということは、そのx,yでの全zはないことになる
					for (z=0 ; z<DIMENSION ; z++) {
						Cube[z][j][k] = false;
						EstCube[z][j][k] = true;
					}
				}
			}
		}

		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				// (3)　右からのPrjがないということは、そのy,zでの全xはないことになる
				if (!Prj[1][j][k]) {
					for (x=0 ; x<DIMENSION; x++) {
						Cube[j][DIMENSION-k-1][x] = false;
						EstCube[j][DIMENSION-k-1][x] = true;
					}
				}
			}
		}

		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				// (4)　前からのPrjがないということは、そのx,zでの全yはないことになる
				if (!Prj[2][j][k]) {
					for (y=0 ; y<DIMENSION; y++) {
						Cube[j][y][k] = false;
						EstCube[j][y][k] = true;
					}
				}
			}
		}

		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				// (5)　右からのPrjがある場合に、そのy,zにある3つのxのうち、2つが0で確定なら、残りは1で確定する
				if (Prj[1][j][k]) {
					if (!EstCube[j][DIMENSION-k-1][0]
						&& EstCube[j][DIMENSION-k-1][1]
						&& EstCube[j][DIMENSION-k-1][2]
						&& !Cube[j][DIMENSION-k-1][1] && !Cube[j][DIMENSION-k-1][2]) {
						Cube[j][DIMENSION-k-1][0] = true;
						EstCube[j][DIMENSION-k-1][0] = true;
					}
					if (EstCube[j][DIMENSION-k-1][0]
							&& !EstCube[j][DIMENSION-k-1][1]
							&& EstCube[j][DIMENSION-k-1][2]
							&& !Cube[j][DIMENSION-k-1][0] && !Cube[j][DIMENSION-k-1][2]) {
							Cube[j][DIMENSION-k-1][1] = true;
							EstCube[j][DIMENSION-k-1][1] = true;
					}
					if (EstCube[j][DIMENSION-k-1][0]
							&& EstCube[j][DIMENSION-k-1][1]
							&& !EstCube[j][DIMENSION-k-1][2]
							&& !Cube[j][DIMENSION-k-1][0] && !Cube[j][DIMENSION-k-1][1]) {
							Cube[j][DIMENSION-k-1][2] = true;
							EstCube[j][DIMENSION-k-1][2] = true;
					}
				}

				// (6)　前からのPrjがある場合に、そのx,zにある3つのyのうち、2つが0で確定なら、残りは1で確定する
				if (Prj[2][j][k]) {
					if (!EstCube[j][0][k]
						&& EstCube[j][1][k]
						&& EstCube[j][2][k]
						&& !Cube[j][1][k] && !Cube[j][2][k]) {
						Cube[j][0][k] = true;
						EstCube[j][0][k] = true;
					}
					if (EstCube[j][0][k]
							&& !EstCube[j][1][k]
							&& EstCube[j][2][k]
							&& !Cube[j][0][k] && !Cube[j][2][k]) {
							Cube[j][1][k] = true;
							EstCube[j][1][k] = true;
					}
					if (EstCube[j][0][k]
							&& EstCube[j][1][k]
							&& !EstCube[j][2][k]
							&& !Cube[j][0][k] && !Cube[j][1][k]) {
							Cube[j][2][k] = true;
							EstCube[j][2][k] = true;
					}
				}
			}
		}

		// (7)　ブロックが存在するその下段はすべて存在する
		for (z=0 ; z<DIMENSION-1 ; z++) {
			for (y=0 ; y<DIMENSION ; y++) {
				for (x=0 ; x<DIMENSION ; x++) {
					if (EstCube[z][y][x] && Cube[z][y][x]) {
						Cube[z+1][y][x] = true;
						EstCube[z+1][y][x] = true;
					}
				}
			}
		}
	}

	public static void copyCube(boolean[][][] src, boolean[][][] dst) {
		int z, y, x;

		for (z=0 ; z<DIMENSION ; z++) {
			for (y=0 ; y<DIMENSION ; y++) {
				for (x=0 ; x<DIMENSION ; x++) {
					dst[z][y][x] = src[z][y][x];
				}
			}
		}
	}

	public static void getNextXYZ(int[] src, int[] dst) {
		int x, y, z;
		int srcCal = src[2]*100 + src[1]*10 + src[0];
		int dstCal;

		for (z=src[2] ; z<DIMENSION ; z++) {
			for (y=0 ; y<DIMENSION ; y++) {
				for (x=0 ; x<DIMENSION ; x++) {
					dstCal = z*100 + y*10 + x;
					if ((dstCal > srcCal) && !EstCube[z][y][x]) {
						dst[0] = x;
						dst[1] = y;
						dst[2] = z;
						return;
					}
				}
			}
		}
		dst[0] = -1;
		dst[1] = -1;
		dst[2] = -1;
	}

	public static boolean match(boolean[][][] cb) {
		int j, k, z;

		// 上からのProjectionチェック。存在しているブロックの下段が全て存在するか
		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				if (cb[0][j][k] && (!cb[1][j][k] || !cb[2][j][k])) { return false; }
				if (cb[1][j][k] && !cb[2][j][k]) { return false; }
			}
		}

		// 右からのProjectionチェック
		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				if (Prj[1][j][k] && !cb[j][DIMENSION-k-1][0]
						&& !cb[j][DIMENSION-k-1][1] && !cb[j][DIMENSION-k-1][2]) { return false; }
				if (!Prj[1][j][k] && (cb[j][DIMENSION-k-1][0]
						|| cb[j][DIMENSION-k-1][1] || cb[j][DIMENSION-k-1][2])) { return false; }
			}
		}

		// 前面からのProjectionチェック
		for (j=0 ; j<DIMENSION ; j++) {
			for (k=0 ; k<DIMENSION ; k++) {
				if (Prj[2][j][k] && !cb[j][0][k] && !cb[j][1][k] && !cb[j][2][k]) { return false; }
				if (!Prj[2][j][k] && (cb[j][0][k] || cb[j][1][k] || cb[j][2][k])) { return false; }
			}
		}

		return true;
	}

	/*
	 * countCubePattern()
	 *   考え方：
	 *   	(1)　今の箇所が確定の場合、次の未確定x,y,zを求め、そこから再探索開始（再帰）し（引数Cubeをそのまま渡す）、その結果をそのまま返す。
	 *   		もし次の未確定x､y､zがない場合、現在のCubeでマッチングした結果をカウントして返す
	 *   	(2)　今の箇所が未確定の場合、次の未確定x,y,zを求め、
	 *   		次の未確定がある場合、現ステップでの有無それぞれのﾊﾟﾀｰﾝでそこから再探索開始（再帰）し、その結果を加算して返す。
	 *   		※ただしCubeはコピーし、有無それぞれのﾊﾟﾀｰﾝをセットして再探索のCube引数とすることで各ステップに影響がないようにする。
	 *   		もし次の未確定x,y,zがない場合は、現段階の有無それぞれでマッチングカウントした結果をそのまま返す。
	 */
	public static int countCubePattern(int z, int y, int x, boolean[][][] cb) {
		int c = 0;
		int[] currXYZ = new int[3];
		int[] nextXYZ = new int[3];
		currXYZ[0] = x; currXYZ[1] = y; currXYZ[2] = z;
		nextXYZ[0] = -1; nextXYZ[1] = -1; nextXYZ[2] = -1;
		getNextXYZ(currXYZ, nextXYZ);
		// (1)
		if (EstCube[z][y][x]) {
			if (nextXYZ[0] == -1) { // 次の未確定がない
				if (match(cb)) { c++; }
				return c;
			} else { // 次の未確定で再探査
				return countCubePattern(nextXYZ[2], nextXYZ[1], nextXYZ[0], cb);
			}
		} else { // (2)
			boolean[][][] newCube = new boolean[DIMENSION][DIMENSION][DIMENSION];
			copyCube(cb, newCube);
			if (nextXYZ[0] == -1) {
				newCube[z][y][x] = false;
				if (match(newCube)) { c++; }
				newCube[z][y][x] = true;
				if (match(newCube)) { c++; }
			} else {
				newCube[z][y][x] = false;
				c += countCubePattern(nextXYZ[2], nextXYZ[1], nextXYZ[0], newCube);
				newCube[z][y][x] = true;
				c += countCubePattern(nextXYZ[2], nextXYZ[1], nextXYZ[0], newCube);
			}
		}

		return c;
	}

	public static void setProjection(int direction, boolean[][][] proj, String line) {
		int i;

		for (i=0 ; i<DIMENSION; i++) {
			proj[direction][0][0] = getStrToBool(line.substring(2, 3));
			proj[direction][0][1] = getStrToBool(line.substring(4, 5));
			proj[direction][0][2] = getStrToBool(line.substring(6, 7));
			proj[direction][1][0] = getStrToBool(line.substring(10, 11));
			proj[direction][1][1] = getStrToBool(line.substring(12, 13));
			proj[direction][1][2] = getStrToBool(line.substring(14, 15));
			proj[direction][2][0] = getStrToBool(line.substring(18, 19));
			proj[direction][2][1] = getStrToBool(line.substring(20, 21));
			proj[direction][2][2] = getStrToBool(line.substring(22, 23));
		}
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner cin = new Scanner(System.in);
		String line;
		for (;cin.hasNext();) {
			// 上からのProjection
			line = cin.nextLine();
			setProjection(0, Prj, line);

			// 右からのProjection
			line = cin.nextLine();
			setProjection(1, Prj, line);

			// 前面からのProjection
			line = cin.nextLine();
			setProjection(2, Prj, line);

			// 各Projectionを元に、有無が確定する箇所を決定づける
			firstEstablished();

			// Projectionだけでは未確定だった箇所を有無パターンでProjectionの状態と一致するものだけカウント
			System.out.println(countCubePattern(0, 0, 0, Cube));
		}
		cin.close();
	}
}