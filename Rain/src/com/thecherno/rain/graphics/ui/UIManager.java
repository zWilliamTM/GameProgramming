package com.thecherno.rain.graphics.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
	
	private List<UIPanel> panels = new ArrayList<UIPanel>();	
	private List<UIComponent> components = new ArrayList<UIComponent>();

	public UIManager() {
		
	}
	
	public void addComponent(UIComponent component) {
		components.add(component);
	}
	
	public void addPanel(UIPanel panel) {
		panels.add(panel);
	}
	
	public void clear() {
		panels.clear();
		components.clear();
	}
	
	public void update() {
		for (UIPanel panel : panels) {
			panel.update();
		}
		for (UIComponent component : components) {
			component.update();
		}
	}
	
	public void render(Graphics g) {
		for (UIPanel panel : panels) {
			panel.render(g);
		}
		for (UIComponent component : components) {
			component.render(g);
		}
	}

}
