package main;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import ui.cfg.JFrameConfig;
import control.GameControl;

public class Main {
	public static void main(String[] args) {
		initGobalFont(JFrameConfig.FONT_ONE);
		
		try {
			// turn off bold fonts
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			// re-install the Metal Look and Feel
			//
			// Update the ComponentUIs for all Components. This
			// needs to be invoked for all windows.
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*  start game comtroler */
		new GameControl();
	}

	public static void initGobalFont(Font font) {
		FontUIResource fontResource = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontResource);
			}
		}
	}
}
