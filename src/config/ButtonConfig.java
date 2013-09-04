package config;

import java.io.Serializable;
import org.dom4j.Element;

public class ButtonConfig implements Serializable{
    private static final long serialVersionUID = 1024L;
	/*
	 * button's height and width
	 */
	private final int width;
	private final int height;

	/*
	 * start button coordinates
	 */
	private final int startX;
	private final int startY;

	/*
	 * setting button coordinates
	 */
	private final int settingX;
	private final int settingY;

	/** 
	 * readding value from the xml configure file
	 */
	public ButtonConfig(Element button) {
		width = Integer.parseInt(button.attributeValue("w"));
		height = Integer.parseInt(button.attributeValue("h"));

		Element eStart = button.element("start");
		startX = Integer.parseInt(eStart.attributeValue("x"));
		startY = Integer.parseInt(eStart.attributeValue("y"));

		Element eSetting = button.element("setting");
		settingX = Integer.parseInt(eSetting.attributeValue("x"));
		settingY = Integer.parseInt(eSetting.attributeValue("y"));
	}
	/**
	 * only getters
	 */
	public int getWidth () {
		return this.width;
	}
	public int getHeight () {
		return this.height;
	}
	public int getStartX () {
		return this.startX;
	}
	public int getStartY () {
		return this.startY;
	}
	public int getSettingX () {
		return this.settingX;
	}
	public int getSettingY () {
		return this.settingY;
	}
}
