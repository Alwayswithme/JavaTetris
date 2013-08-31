package config;

import java.io.Serializable;

public class LayerConfig implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = -7089216936875004839L;
	private final String className;
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
