package config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

	/**
	 * store the attributes in the system element
	 */
public class SystemConfig implements Serializable{
    private static final long serialVersionUID = -8797975232131531734L;

	/**
	 * the game table which is a coordinates map,
	 * use these X Y to define the table size.
	 */
	private final int minX;
	private final int maxX;
	private final int minY;
	private final int maxY;
	/**
	 * decied when how many line get remove
	 * to upgrade a level
	 */
	private final int levelUp;
	/**
	 * decied which level to create
	 * obstacle randomly
	 */
	private final int obstacle;

	/**
	 * List of cube element which is
	 * to define the terrominoe's rotatablity
	 * and init location
	 */
	private final ArrayList<CubeElement> eCube;

	/**
	 * A map of the bonus(value) and remove how
	 * many lines(key)
	 */
	private final Map<Integer, Integer> bonus;
	public SystemConfig( Element system) {
		minX = Integer.parseInt(system.attributeValue("min_x"));
		minY = Integer.parseInt(system.attributeValue("min_y"));
		maxX = Integer.parseInt(system.attributeValue("max_x"));
		maxY = Integer.parseInt(system.attributeValue("max_y"));
		levelUp = Integer.parseInt(system.attributeValue("level_up"));
		obstacle = Integer.parseInt(system.attributeValue("obstacle"));
		@SuppressWarnings( "unchecked")
        List<Element> cubes = system.elements("cube");
		//实例化cube元素 List
		eCube = new ArrayList<CubeElement>(cubes.size());
		//实例化boolean数组
		//遍历cube元素
		for (Element cube : cubes) {
			eCube.add(new CubeElement(cube));
		}
		this.bonus = new HashMap<Integer, Integer>();
		//获得奖励元素配置
		@SuppressWarnings( "unchecked")
        List<Element> eScore = system.elements("bonus");
		for (Element e : eScore) {
			int rm = Integer.parseInt(e.attributeValue("rm"));
			int score = Integer.parseInt(e.attributeValue("score"));
			
			bonus.put(rm, score);
		}
    }
	public int getMinX() {
		return minX;
	}
	public int getMaxX() {
		return maxX;
	}
	public int getMinY() {
		return minY;
	}
	public int getMaxY() {
		return maxY;
	}
	public ArrayList<CubeElement> getECube() {
		return eCube;
	}
	public int getLevelUp() {
		return levelUp;
	}
	public Map<Integer, Integer> getBonus() {
		return bonus;
	}
	public int getObstacle() {
		return obstacle;
	}
}
