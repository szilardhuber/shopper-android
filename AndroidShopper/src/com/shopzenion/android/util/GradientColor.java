package com.shopzenion.android.util;

import android.graphics.Color;

public class GradientColor {

	private int startColor;
	private int endColor;
	private int colorCount;
	private int redStep;
	private int greenStep;
	private int blueStep;

	public GradientColor(int startColor, int endColor, int colorCount) {
		super();
		this.startColor = startColor;
		this.endColor = endColor;
		this.colorCount = colorCount;
		this.redStep = (Color.red(this.endColor) - Color.red(this.startColor))
				/ colorCount;
		this.greenStep = (Color.green(this.endColor) - Color
				.green(this.startColor)) / colorCount;
		this.blueStep = (Color.blue(this.endColor) - Color
				.blue(this.startColor)) / colorCount;
	}

	public int getColor(int i) {
		if (i < 0 || i >= colorCount) {
			throw new ArrayIndexOutOfBoundsException(
					"Invalid parameter for colorCount: " + colorCount);
		}

		int red = Color.red(startColor) + i * redStep;
		int green = Color.green(startColor) + i * greenStep;
		int blue = Color.blue(startColor) + i * blueStep;
		return 0xFF000000 | red << 16 | green << 8 | blue;
	}
}
