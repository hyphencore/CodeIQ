package takasa;

public class Takasa {

	public static void main(String[] args) {
		int loop = 1;

		for (int i=1 ; i<=100 ; i++) {
			loop = 1;
			if (i % 2 == 0) loop += 2;
			if (i % 3 == 0) loop += 3;
			if (i % 5 == 0) loop += 5;
			for (int j=1 ; j<=loop ; j++) {
				System.out.print("]");
			}
			System.out.println("");
		}

	}

}
