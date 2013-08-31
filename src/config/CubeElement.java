package config;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class CubeElement {
	/**
	 * 是cube元素的属性
	 * 装有方块能否旋转的信息
	 * 记录这种形状需要多少个坐标确定
	 */
	private  final boolean rotatable;
	
	private  final int needCoords;
	
	private  final ArrayList<Point> initPoints;
	
	private final String className;
	public CubeElement(Element cube) {
		//根据cube元素的属性(round)  判断是否可以旋转
		rotatable = Boolean.parseBoolean(cube.attributeValue("round"));
		// 获取coords属性这种形状确定需要几个坐标确定
		needCoords = Integer.parseInt(cube.attributeValue("coords"));
		
		className = cube.attributeValue("class_name");
		//取得一个cube元素中所有point元素
		@SuppressWarnings( "unchecked")
        List<Element> points = cube.elements("point");
		int size = points.size();
		initPoints = new ArrayList<Point>(size);
		for (int j = 0; j < size; j++) {
			int x = Integer.parseInt(points.get(j).attributeValue("x"));
			int y = Integer.parseInt(points.get(j).attributeValue("y"));
			//将point元素的坐标对应的Point对象加入Point数组
			initPoints.add(new Point(x, y));
		}
	}
	public boolean isRotatable() {
		return rotatable;
	}
	public int getNeedCoords() {
		return needCoords;
	}
	public ArrayList<Point> getInitPoints() {
		return initPoints;
	}
	public String getClassName() {
		return className;
	}

}
