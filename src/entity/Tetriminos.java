/**
 * 
 */
package entity;

import java.awt.Point;
import java.util.Random;
import util.GameMethod;
import config.GameConfig;

/**
 * 俄罗斯方块的抽象类，成员:能否旋转，需要几个坐标(方块)组成，初始形态(竖的或横的) 方法:下落、位移、旋转
 * 
 * @author phx
 */
public abstract class Tetriminos {

	public static int MIN_X = GameConfig.getSYSTEM().getMinX();
	
	public  static int MAX_X = GameConfig.getSYSTEM().getMaxX();
	
	protected static Random rand = new Random();
	
	protected boolean rotatable;
	
	protected byte shapeID;
	/**
	 * mark rotate how  many time then use to sync the
	 * hint picture
	 */
	protected int rotateFlag;
	/**
	 * use to initialize the Tetriminos
	 */
	protected Point[] fallCoords;
	/**
	 * use to make a hint shadow
	 */
	protected Point[] bottomCoords;
	
	public Tetriminos( byte ID, boolean rotatable, int rotateFlag, Point[] fall) {
		
		this.shapeID = ID;
		this.rotatable = rotatable;
		this.rotateFlag = rotateFlag;
		this.fallCoords = fall;
	}
	
	public Tetriminos( byte ID) {
		this.shapeID = ID;
	}
	/**
	 *  rotate method
	 * @return coordinates after rotate
	 */
	public Point[] rotate() {
		Point[] rotated = GameMethod.copyCoords(fallCoords);
		// if the coords in the boundary move to left or right first
		if(rotated[0].x == MIN_X) {
			move(rotated, 1, 0);
		}else if(rotated[0].x == MAX_X) {
			move(rotated, -1, 0);
		}
		
		for (int i = 0; i < 4; i++) {
			int newX = rotated[0].x + rotated[0].y - rotated[i].y;
			int newY = rotated[0].y - rotated[0].x + rotated[i].x;
			rotated[i] = new Point(newX, newY);
		}
		return rotated;
	}
	public boolean move(Point[] oldCoords, int newX, int newY) {
		int len = oldCoords.length;

		/* side effect */
		for (int i = 0; i < len; i++) {
			oldCoords[i].x += newX;
			oldCoords[i].y += newY;
		}
		return true;
	}
	public boolean isRotatable() {
		return rotatable;
	}
	
	public byte getShapeID() {
		return shapeID;
	}
	
	public int getRotateFlag() {
		return rotateFlag;
	}
	
	public Point[] getFallCoords() {
		return fallCoords;
	}
	
	public void setFallCoords(Point[] fallCoords) {
		this.fallCoords = fallCoords;
	}
	
	public Point[] getBottomCoords() {
		return bottomCoords;
	}

	public void setBottomCoords(Point[] bottomCoords) {
		this.bottomCoords = bottomCoords;
	}
}
