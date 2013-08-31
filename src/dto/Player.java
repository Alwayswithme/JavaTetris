package dto;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {


	/**
	 * 
	 */
    private static final long serialVersionUID = -3865678383124758357L;

	private String name;

	private int score;

	public Player(String name, int point) {
		super();
		this.name = name;
		this.score = point;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setScore(int point) {
		this.score = point;
	}
	public int getScore() {
		return score;
	}
	@Override
	public int compareTo(Player p) {
		//分数降序排列
		int temp = this.score - p.score ;
		return temp == 0 ? this.name.compareTo(p.name): -temp;
	}
	
	@Override
	public String toString() {
	    return this.name + ":" + this.score;
	}

}
