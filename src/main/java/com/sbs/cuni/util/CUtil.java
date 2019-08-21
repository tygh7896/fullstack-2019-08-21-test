package com.sbs.cuni.util;

import java.math.BigInteger;
import java.util.Random;

// C(common) Util
// 일반적으로 많이 쓰이는 유틸
public class CUtil {

	public static long getAsLong(Object num) {
		if (num instanceof Long) {
			return (long) num;
		} else if (num instanceof String) {
			return Long.parseLong((String) num);
		} else if (num instanceof Integer) {
			return (long) num;
		} else if (num instanceof BigInteger) {
			return ((BigInteger) num).longValue();
		}

		return 0;
	}

	public static int getAsInt(Object num) {
		if (num instanceof Long) {
			return (int) num;
		} else if (num instanceof Integer) {
			return (int) num;
		} else if (num instanceof String) {
			return Integer.parseInt((String) num);
		} else if (num instanceof BigInteger) {
			return ((BigInteger) num).intValue();
		}

		return 0;
	}

	public static String getTempKey() {
		return new TempKey().getKey();
	}
	
	public static String getTemporaryPassword() {
		return new TemporaryPassword().getTemporaryPassword();
	}

}

class TempKey {
	private int length;

	public TempKey() {
		length = 30;
	}

	public String getKey() {
		return makeKey();
	}

	private String makeKey() {
		StringBuffer buffer = new StringBuffer();
		while (buffer.length() < length) {
			Random random = new Random();
			int key = random.nextInt(75) + 48;
			if ((key >= 48 && 57 >= key) || (key >= 65 && 90 >= key) || (key >= 97 && 122 >= key)) {
				buffer.append((char) key);
			}
		}

		return buffer.toString();
	}
}
class TemporaryPassword {
	private int length;

	public TemporaryPassword() {
		length = 10;
	}

	public String getTemporaryPassword() {
		return makeTemporaryPassword();
	}

	private String makeTemporaryPassword() {
		StringBuffer buffer = new StringBuffer();
		while (buffer.length() < length) {
			Random random = new Random();
			int key = random.nextInt(75) + 48;
			if ((key >= 48 && 57 >= key) || (key >= 65 && 90 >= key) || (key >= 97 && 122 >= key)) {
				buffer.append((char) key);
			}
		}

		return buffer.toString();
	}
}