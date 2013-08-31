package config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class FrameConfig implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 6692143439511931769L;
	/*
	 * 游戏窗口宽高
	 * 边框大小 内边距
	 */
	private final String windowTitle;
	private final int width;
	private final int height;
	private final int borderSize;
	private final int padding;
	private final int windowUp;
	//方块尺寸
	private final int cubeSize;
	//游戏结束后的方块图片索引
	private final int loseIdx;
	/**
	 * 图层属性
	 */
	private final ArrayList<LayerConfig> layersConfig;

	/**
	 * 按钮属性
	 */
	private final ButtonConfig buttonConfig;
	public FrameConfig( Element frame) {
		//获取窗口宽度
		width = Integer.parseInt(frame.attributeValue("width"));
		// 获取窗口高度
		height = Integer.parseInt(frame.attributeValue("height"));
		// 获取窗口边框大小
		borderSize = Integer.parseInt(frame.attributeValue("border_size"));
		// 获取窗口内边距
		padding = Integer.parseInt(frame.attributeValue("padding"));
		// 获取标题
		windowTitle = frame.attributeValue("title");
		// 获取窗口拔高的值
		windowUp = Integer.parseInt(frame.attributeValue("window_up"));
		//方块大小
		cubeSize = Integer.parseInt(frame.attributeValue("cube_size"));
		//失败后显示的图片索引
		loseIdx = Integer.parseInt(frame.attributeValue("loseIdx"));
		// 从配置文件frame元素对应的layer嵌套中读取属性
		@SuppressWarnings( "unchecked")
		List<Element> layers = frame.elements("layer");
		layersConfig = new ArrayList<LayerConfig>();
		for (Element layer : layers) {
			// 利用读取到的数值创建单个层属性对象
			LayerConfig temp = new LayerConfig(
			                layer.attributeValue("class_name"),
			                Integer.parseInt(layer.attributeValue("x")),
			                Integer.parseInt(layer.attributeValue("y")),
			                Integer.parseInt(layer.attributeValue("w")),
			                Integer.parseInt(layer.attributeValue("h"))
			                );
			
			layersConfig.add(temp);
		}

		buttonConfig = new ButtonConfig(frame.element("button"));
    }
	public String getWindowTitle() {
		return windowTitle;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getBorderSize() {
		return borderSize;
	}
	public int getPadding() {
		return padding;
	}
	public int getWindowUp() {
		return windowUp;
	}
	public ArrayList<LayerConfig> getLayersConfig() {
		return layersConfig;
	}
	public int getCubeSize() {
		return cubeSize;
	}
	public int getLostIdx() {
		return loseIdx;
	}
	public ButtonConfig getButtonConfig() {
		return buttonConfig;
	}
}
