package b2a;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class B2a {

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		int i;
		byte[] b = new byte[1];
		while (cin.hasNext()) {
			line = cin.nextLine();
			for (i=0 ; i<line.length()/8 ; i++) {
				b[0] = Byte.parseByte(line.substring(i*8, (i+1)*8), 2);
				try {
					System.out.print(new String(b, "US-ASCII"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		cin.close();;
	}

}
