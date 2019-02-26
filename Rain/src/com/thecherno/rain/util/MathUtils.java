package com.thecherno.rain.util;

public class MathUtils {
	
	private MathUtils() {
	}

	public static int min(int value, int min) {
		return value < min ? min : value;
	}
	
	public static int max(int value, int max) {
		return value > max ? max : value;
	}
	
	public static int clamp(int value, int min, int max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}
	
	public static int calculateTextWidth(int length_text, int font_size) {
		int width = 0;
		width = length_text * font_size / 2;
		return width;
	}

}
