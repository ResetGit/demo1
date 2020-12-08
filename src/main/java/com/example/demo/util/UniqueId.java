package com.example.demo.util;

import java.util.Calendar;
import java.util.UUID;

public abstract class UniqueId {
	private static long thisId = 0L;

	public static synchronized long getUniqueId() {
		long id = 0L;
		do {
			Calendar c = Calendar.getInstance();
			id = c.getTimeInMillis();
		} while (id == thisId);
		thisId = id;
		return id;
	}

	public static String getUniqueId(int digit) {
		char[] digits = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
				'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
				'Y', 'Z' };

		String id = "";
		for (int i = 0; i < digit; i++) {
			int temp = new Double(Math.random() * 997.0D).intValue() % 36;
			id = id + String.valueOf(digits[temp]);
		}
		return id;
	}

	public static String getGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase();
	}
	
	public static String getGUID2() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase().replaceAll("-", "");
	}
}