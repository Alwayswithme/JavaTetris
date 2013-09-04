package entity;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import config.CubeElement;
import config.GameConfig;

public class TetriminosGenerator {
	
	// 方块ID 与 构造器的映射
	public static HashMap<Byte, Constructor<?>> factory = new HashMap<Byte, Constructor<?>>();
	// 随机数生成器
	public static Random rand = new Random();
	// 方块所有属性
	public static ArrayList<CubeElement>  cubeElements = GameConfig.getSYSTEM().getECube();
	
	public static int numOfTerrominoe = cubeElements.size();
	static {
		for (Byte i = 0, len = (byte) numOfTerrominoe; i < len; i++) {
			String cName = cubeElements.get(i).getClassName();
		try {
			Class<?> c = Class.forName(cName);
			Constructor<?>[] cons = c.getConstructors();
			factory.put(i, cons[0]);
        } catch (SecurityException | ClassNotFoundException e) {
	        e.printStackTrace();
        }
		}
	}
	public static Tetriminos get(byte shapeID) {
		CubeElement e = cubeElements.get(shapeID);
		
		int coords = e.getNeedCoords();
		
		boolean rotatable = e.isRotatable();
		
		ArrayList<Point> allPoints = e.getInitPoints();
		
		int rotateFlag = rand.nextInt(allPoints.size() / coords);
		
		int start = rotateFlag * coords;
		
		Point[] fallPoint = new Point[coords];
		
		for(int j = 0; j< coords; j++) {
			// 从某一变体起始坐标初始化下落方块
			Point temp = allPoints.get(j + start);
			fallPoint[j] = new Point(temp.x, temp.y);
		}
		Tetriminos result = null;
        try {
	        result = (Tetriminos) factory.get(shapeID).newInstance(shapeID, rotatable, rotateFlag, fallPoint);
        } catch (InstantiationException e1) {
	        e1.printStackTrace();
        } catch (IllegalAccessException e1) {
	        e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
	        e1.printStackTrace();
        } catch (InvocationTargetException e1) {
	        e1.printStackTrace();
        }
		return result;
	}
	
	public static Tetriminos randomTerrominoe() {
		byte randomId = (byte) rand.nextInt(numOfTerrominoe);
		return get(randomId);
	}

}
