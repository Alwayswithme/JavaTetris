package util;

import java.awt.Color;
import java.awt.Point;

public class GameMethod {
	/**
	 * 复制坐标点集合
	 * @param coords 要复制的Point[]数组
	 * @return 和coords一样的数组
	 */
	public static Point[] copyCoords(Point[] coords) {
	    int len = coords.length;
		Point[] copyCoords = new Point[len];
		for (int i = 0; i < len; i++) {
			copyCoords[i] = new Point(coords[i].x, coords[i].y);
		}
	    return copyCoords;
    }
	
    public static Color getNowColor(double percent) {
		//渐变rgb
		int[] from = {56, 215, 56};
		int[] to = {215, 34, 34};
		int[] rgb = new int[3];
		for ( int i = 0; i < 3; i++) {
			rgb[i] = calColor(from[i], to[i], percent);
		}
		Color temp = new Color(rgb[0], rgb[1], rgb[2], 224);
		return temp;
	}
	private static int calColor(int color1, int color2, double percent) {
		return (int)((color2 - color1) *percent + color1);
	}
	//分段函数处理颜色渐变
    public  static Color getNewColor(double hp, double maxHp) {
		int colorR = 0;
		int colorG = 255;
		double hpHalf = maxHp/2;
		if ( hp > hpHalf ) { 
			colorR = 255 - (int) ((hp - hpHalf) / (maxHp / 2) * 255);
			colorG = 255;
		}else {
			colorR = 255;
			colorG = (int) (hp / (maxHp / 2) * 255);
		}
		return new Color( colorR, colorG, 0);
	}
 
}
