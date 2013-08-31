package entity;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import config.CubeElement;
import config.GameConfig;

public class FallCube implements Serializable {

    private static final long serialVersionUID = 1L;
    
	private static int MIN_X = GameConfig.getSYSTEM().getMinX();
	private static int MIN_Y = GameConfig.getSYSTEM().getMinY();

	private static int MAX_X = GameConfig.getSYSTEM().getMaxX();
	private static int MAX_Y = GameConfig.getSYSTEM().getMaxY();
	/**
	 * 方块编号
	 */
	private int shapeID;
	/**
	 * 方块数组
	 */
	private Point[] fallBlock;
	/**
	 * 旋转标记
	 */
	private int rotateFlag;
	
	private static Random rand = new Random();
	
	private static List<CubeElement> TERROMINOES = GameConfig.getSYSTEM().getECube();
	
/*   构造器   */
	public FallCube(int code) {
		shapeID = code;
		//根据编号获取一种形状的方块的属性用于创建下落方块
		CubeElement eCube = TERROMINOES.get(code);
		// 从XML元素获得该形状需要的坐标数目
		int coordNum = eCube.getNeedCoords();
		fallBlock = new Point[coordNum];
		// 配置文件中预先定义好的方块出现坐标
		ArrayList<Point> defPoints = eCube.getInitPoints();
		/* 计算形状方块可能存在的变体,用来决定下落方块最初的形状,如长条可能是横的或竖的
		 * 总坐标数除以所需坐标数计算得共有几种最初形态
		 * 用随机数获得一个种形态起始坐标点
		 * 该点同时作为旋转次数的标记
		 * 乘以所需坐标数得到某一变体起始坐标
		 */
		rotateFlag = rand.nextInt(defPoints.size() / coordNum);
		int start = rotateFlag * coordNum;
		
		for(int i = 0; i< coordNum; i++) {
			// 从某一变体起始坐标初始化下落方块
			Point temp = defPoints.get(i + start);
			fallBlock[i] = new Point(temp.x, temp.y);
		}
	}


	/**
	 * 方块移动
	 * @param moveX x轴偏移量
	 * @param moveY y轴偏移量
	 */
	public boolean tryMove(int moveX, int moveY, boolean[][] gameMap) {
		//移动判断
		int len = fallBlock.length;
		for (int i = 0 ; i < len; i++) {
			int newX = fallBlock[i].x + moveX;
			int newY = fallBlock[i].y + moveY;
			if (isOverZone(newX, newY, gameMap))
				return false;
		}

		//可以移动，坐标进行处理
		for (int i = 0; i < len; i++) {
			fallBlock[i].x += moveX;
			fallBlock[i].y += moveY;
		}
		return true;
		
	}
	/**
	 * 方块旋转
	 * 顺时针旋转公式
	 * A.x = O.x + O.y - B.y
	 * A.y = O.y - O.x + B.x
	 */
	public void rotate(boolean[][] gameMap) {
			//不能旋转直接返回
		if ( !TERROMINOES.get(shapeID).isRotatable() ) return;
		for (int i = 1; i < fallBlock.length; i++) {
			int newX =fallBlock[0].x + fallBlock[0].y - fallBlock[i].y;
			int newY =fallBlock[0].y - fallBlock[0].x + fallBlock[i].x;

			if (isOverZone(newX, newY, gameMap))
				return;
		}
		
		for (int i = 1; i < fallBlock.length; i++) {
			int newX =fallBlock[0].x + fallBlock[0].y - fallBlock[i].y;
			int newY =fallBlock[0].y - fallBlock[0].x + fallBlock[i].x;
			fallBlock[i].x = newX;
			fallBlock[i].y = newY;
		}
	}
	/**
	 * 判断移动后的方块是否达到边界
	 */
	private boolean isOverZone(int newX, int newY, boolean[][] gameMap) {
			return newX < MIN_X || newX > MAX_X || newY < MIN_Y || newY > MAX_Y || gameMap[newX][newY];
	}
	public int getShapeID() {
		return this.shapeID;
	}
	public void setFallBlock(Point[] actPoint) {
		this.fallBlock = actPoint;
	}
	public Point[] getFallBlock() {
		return fallBlock;
	}
	public int getRotateFlag() {
		return rotateFlag;
	}
}
