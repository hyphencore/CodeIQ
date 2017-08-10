package delCN;

import java.util.Scanner;

public class DelCN {

	public static String getDelCN(String line) {
		boolean fin = false;
		int curIndex = 0;
		int i, len;
		byte[] src = line.getBytes();
		byte[] dst;

		curIndex = 0;
		len = line.length();
		while (!fin) {
			if (curIndex >= len-1) fin = true;
			else {
				if ((src[curIndex] == src[curIndex+1]+1) || (src[curIndex] == src[curIndex+1]-1)) {
					for (i=curIndex+2 ; i<len ; i++) src[i-2] = src[i];
					len -= 2;
					if (curIndex > 0) curIndex--;
				} else curIndex++;
			}
		}

		if (len == 0) return "";
		else {
			dst = new byte[len];
			for (i=0 ; i<dst.length ; i++) dst[i] = src[i];
		}
		return new String(dst);
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		String line;
		while (cin.hasNext()) {
			line = cin.nextLine();
			System.out.println(getDelCN(line));
		}
		cin.close();
	}

}
