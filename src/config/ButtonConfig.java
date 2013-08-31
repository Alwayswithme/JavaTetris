package config;

import java.io.Serializable;
import org.dom4j.Element;

public class ButtonConfig implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 1024L;

	/*
	 * 按钮宽度、高度
	 */
	private final int width;

	private final int height;

	/*
	 * 开始按钮坐标
	 */
	private final int startX;

	private final int startY;

	/*
	 * 设置按钮坐标
	 */
	private final int settingX;

	private final int settingY;

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
