package config;

import java.io.Serializable;

/**
 * layer is like a JPanel in the
 * JFrame, but here we use a Graphics
 * to draw it
 */
public class LayerConfig implements Serializable{
    private static final long serialVersionUID = -7089216936875004839L;
	/**
	 * this className is used by reflect to create
	 * the newInstance
	 */
	private final String className;
	/*
	 * these is used by the
	 * constructor of the layerClass
	 * x stand for x coordinate
	 * y stand for y coordinate
	 * w stand for width
	 * h stand for height
	 */
	private final int x;
	private final int y;
	private final int w;
	private final int h;

	public LayerConfig(String str, int x,int y, int w, int h) {
		this.className = str;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	/**
	 * getters used by the controler
	 */
	public String getClassName() {
		return this.className;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getW() {
		return this.w;
	}
	public int getH() {
		return this.h;
	}
}
