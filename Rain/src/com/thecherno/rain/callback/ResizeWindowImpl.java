package com.thecherno.rain.callback;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ResizeWindowImpl implements IResizeWindow {

	@Override
	public void resizeWindow(JFrame frame, int width, int height) {
		Dimension size = new Dimension(width, height);
		frame.setPreferredSize(size);
	}

}
