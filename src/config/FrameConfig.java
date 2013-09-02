package config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class FrameConfig implements Serializable{
    private static final long serialVersionUID = 6692143439511931769L;
	private final String windowTitle;
	/**
	 * the frame's width and height
	 */
	private final int width;
	private final int height;
	/**
	 * these use to adjust the layer
	 * inside the frame
	 */
	private final int borderSize;
	private final int padding;
	private final int windowUp;
	/**
	 * the terrominoe's size
	 * normally is 32x32
	 */
	private final int cubeSize;
	/**
	 * when game over use this
	 * image index to get the
	 * ugly picture draw
	 */
	private final int loseIdx;

	/**
	 * layer is a subelement in the frame element.
	 * It's attributevalue is use to dynamic create
	 * the needed layer and build the
	 * whole game's content.
	 */
	private final ArrayList<LayerConfig> layersConfig;

	/**
	 * button is a subelement in the frame element.
	 * It save the button configure information which
	 * use to define the location of the button in the
	 * frame.
	 */
	private final ButtonConfig buttonConfig;
	public FrameConfig( Element frame) {
		windowTitle = frame.attributeValue("title");
		width = Integer.parseInt(frame.attributeValue("width"));
		height = Integer.parseInt(frame.attributeValue("height"));
		borderSize = Integer.parseInt(frame.attributeValue("border_size"));
		padding = Integer.parseInt(frame.attributeValue("padding"));
		windowUp = Integer.parseInt(frame.attributeValue("window_up"));
		cubeSize = Integer.parseInt(frame.attributeValue("cube_size"));
		loseIdx = Integer.parseInt(frame.attributeValue("loseIdx"));
		@SuppressWarnings( "unchecked")
		List<Element> layers = frame.elements("layer");
		layersConfig = new ArrayList<LayerConfig>();
		for (Element layer : layers) {
			//use the subelement 'layer' create 
			//a single layerconfig a time
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
	/**
	 * getters for the game controler to
	 * draw the game's JFrame
	 */
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
