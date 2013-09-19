package service;

import java.awt.Point;
import java.util.Map;
import config.GameConfig;
import dto.GameDto;
import entity.TetriminosGenerator;

public class GameTetris implements GameService {
	
	/**
	 * 游戏数据对象
	 */
	private GameDto gameDto;
	/**
	 * 消行奖励机制
	 */
	private static final Map<Integer, Integer> RM_SOCRE = GameConfig.getSYSTEM().getBonus();
	
	/**
	 * 升级所需消行数
	 */
	private static final int LEVEL_UP = GameConfig.getSYSTEM().getLevelUp();
	public GameTetris( GameDto dto) {
		this.gameDto = dto;
	}
	
	/**
	 * 按键操作--上
	 */
	public boolean keyUp() {
		if (gameDto.getStatus().isPause())
			return true;
		gameDto.fallPieceRotate();
		return true;
	}
	
	
	/* 核心方法 */
	/**
	 * 按键操作--下 进方块进行下落操作，如不能下落则填充到容器 
	 * 然后判断能否消行，进行奖励结算等 
	 * 检测是否满足游戏失败条件，然后进行结束处理
	 */
	public boolean keyDown() {
		// 判断游戏是否没有启动或处于暂停
		if (!gameDto.getStatus().isStart() || gameDto.getStatus().isPause())
			return false;
		
		// 获取地图数组
		byte[] board = gameDto.getGameBoard();
		// 方块可以移动，move方法返回true
		boolean canMove = gameDto.fallPieceMove( 0, 1);
		if (canMove) {
			// 流程终止
			return true;
		} else {
			// 方块不可以下落,将方块填充到容器
			// 获取下落方块各坐标
			Point[] coords = gameDto.getFallPiece().getFallCoords();
			// 填充到地图数组
			byte mark = gameDto.getFallPiece().getShapeID();
			for (int i = 0; i < coords.length; i++) {
				gameDto.setShapeAt(coords[i].x, coords[i].y, mark);
			}
			// 判断是否可以消行并进行消行操作
			int exp = plusExp(board);
			// 消行数大于0则进行得分操作
			if (exp > 0)
				this.score(exp);
			
			// 刷新下一个方块
			gameDto.setFallPiece(gameDto.getDropQueue().poll());
			// 设置下一个方块到队列
			gameDto.getDropQueue().offer(TetriminosGenerator.randomTerrominoe());
			if (this.isLose()) {
				// 游戏结束的处理
				gameDto.setStart(false);
			}
			return false;
		}
		
	}
	
	// 检查游戏是否失败
	private boolean isLose() {
		// 获得下落方块坐标
		Point[] cube = gameDto.getFallPiece().getFallCoords();
		for (Point point : cube) {
			if (gameDto.shapeAt(point.x, point.y) != -1) {
				return true;
			}
		}
		return false;
	}
	
	// 消行操作
	public int plusExp(byte[] map) {
		// 統計消行數
		int count = 0;
		// 行代号
		for (int row = GameDto.MAX_Y; row > 0; row--) {
			if (isRemovable(map, row)) {
				// 可以消行,则消行，并继续判断当前行
				removeLine(map, row);
				// 消行后整体下移一行，所以y要自增，重新判断当前行
				row++;
				count++;
			}
		}
		return count;
	}
	
	// 判断某一行是否可以消除
	public boolean isRemovable(byte[] map, int row) {
		for (int x = 0; x < GameDto.BOARD_W; x++) {
			// 该行只要有一个坐标为-1，即跳出循环
			// 返回false
			if (map[x + row * GameDto.BOARD_W] == -1)
				return false;
		}
		return true;
	}
	
	// 将一行消除
	public void removeLine(byte[] map, int row) {
		// 按列进行迭代，即整体下移一格
		for (int x = 0; x < GameDto.BOARD_W; x++) {
			// 在该行以上的方块均等于上一个方块
			for (int y = row; y > 0; y--) {
				byte mark = gameDto.shapeAt(x, y-1);
				gameDto.setShapeAt(x, y, mark);
			}
			// 最上面一格等于false
			map[x + 0] = -1;
		}
	}
	
	// 得分操作
	private void score(int exp) {
		int lv = gameDto.getStatus().getLevel();
		int rmLine = gameDto.getStatus().getRemoveLine();
		int point = gameDto.getStatus().getScore();
		// 判断是否可以升级
		if ((rmLine % LEVEL_UP) + exp >= LEVEL_UP) {
			this.gameDto.setLevel(++lv);
		}
		gameDto.getStatus().setRemoveLine(rmLine + exp);
		gameDto.getStatus().setScore(point + RM_SOCRE.get(exp));
	}
	
	/**
	 * 按键操作--左
	 */
	public boolean keyLeft() {
		if (gameDto.getStatus().isPause() || !gameDto.getStatus().isStart())
			return false;
		
		return gameDto.fallPieceMove(-1, 0);
	}
	
	/**
	 * 按键操作--右
	 */
	public boolean keyRight() {
		if (gameDto.getStatus().isPause() || !gameDto.getStatus().isStart())
			return false;
		return gameDto.fallPieceMove(1, 0);
	}
	/**
	 * pause key
	 */
	@Override
	public void keyExtra1() {
		// 暂停
		if (gameDto.getStatus().isStart()) {
			gameDto.changePause();
		}
	}
	/**
	 * suddenly drop down
	 */
	@Override
	public void keyExtra2() {
		if (gameDto.getStatus().isPause())
			return;
		// 瞬间下落
		while (this.keyDown())
			;
		
	}
	/**
	 * shadow switch button
	 */
	@Override
	public void keyExtra3() {
		// 阴影开关
		gameDto.changeShadow();
	}
	
	@Override
	public void keyExtra4() {

	}
	
	/**
	 * 开始游戏 : 生成下落方块，填充刷新方块的队列，改变状态标记位，初始化
	 */
	@Override
	public void gameRun() {
		// 随机生成下落方块
		//TODO
		gameDto.setFallPiece(TetriminosGenerator.randomTerrominoe());
		// 隨機生成下一个方块
		for (int i = 0; i < 3; ++i) {
			gameDto.getDropQueue().offer(TetriminosGenerator.randomTerrominoe());
		}
		// 游戏进入开始状态
		gameDto.setStart(true);
		// dto 初始化
		gameDto.doInit();
	}
	
	@Override
	public void mainAction() {
		this.keyDown();
	}
}
