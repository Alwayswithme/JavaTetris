package dto;


public class GameStatus   {

	// 游戏当前等级
	int level;
	// 游戏得分
	int score;
	// 消行数目
	int removeLine;
	// 游戏开始状态
	boolean start;
	// 阴影开关
	boolean shadow;
	// 游戏暂停状态
	boolean pause;
	// 下落速度
	long speed;
	
	
	public GameStatus() {
		
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public void setRemoveLine(int removeLine) {
		this.removeLine = removeLine;
	}

	

	public int getLevel() {
		return level;
	}

	public int getScore() {
		return score;
	}

	public int getRemoveLine() {
		return removeLine;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isShadow() {
		return shadow;
	}

	public boolean isPause() {
		return pause;
	}


	public long getSpeed() {
		return speed;
	}

}