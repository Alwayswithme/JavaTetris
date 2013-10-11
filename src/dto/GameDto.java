package dto;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Timer;
import util.GameFunction;
import util.GameMethod;
import config.GameConfig;
import entity.Tetriminos;

public class GameDto implements ActionListener{
	private static final int HARDER = GameConfig.getSYSTEM().getObstacle();
	
	
	// TODO
	public static int MIN_X = GameConfig.getSYSTEM().getMinX();
	
	public static int MIN_Y = GameConfig.getSYSTEM().getMinY();
	
	public  static int MAX_X = GameConfig.getSYSTEM().getMaxX();
	
	public  static int MAX_Y = GameConfig.getSYSTEM().getMaxY();
	
	// 方块容器宽高
	public static final int BOARD_W = MAX_X + 1;
	
	public static final int BOARD_H = MAX_Y + 1;
	
	// 数据库记录
	private List<Player> dbRecord;
	// 本地记录
	private List<Player> localRecord;

//	private boolean[][] gameZone;

	private byte[] gameBoard;
	//下落方块
	private Tetriminos fallPiece;
	
	//游戏状态
	private GameStatus status = new GameStatus();

	/**
	 * 将要出现的方块
	 */
	private LinkedList<Tetriminos> dropQueue = new LinkedList<Tetriminos>();

	/**
	 * 定时制造障碍物的计时器
	 */
	private Timer obstacleTimer;	
	private int obstacleLevel = GameConfig.getSYSTEM().getObstacle();

	public GameDto() {
		doInit();
		//TODO
		obstacleTimer = new Timer(60000, this);
	}

	public void doInit() {
		gameBoard = new byte[BOARD_W * BOARD_H];
		for (int i = 0, len = gameBoard.length; i < len; i++) {
	        gameBoard[i] = (byte)-1;
        }

		status.level = 1;
		status.score = 0;
		status.removeLine = 0;
		this.status.speed = GameFunction.getSleepTimeByLevel(this.status.level);
	}

	public void actionPerformed(ActionEvent e) {
		// when the level is bigger than the setting
		// level, begin to create Obstacle randomly
		if (status.level >= obstacleLevel) {
			createObstacle();
		} 
	}

	/**
	 * 在底部制造障碍物
	 */
	public void createObstacle() {
		// 按列进行迭代，即整体上移一格
		for (int x = 0; x < BOARD_W; x++) {
			// 在该行以上的方块均等于下一个方块
			for (int y = 0; y < BOARD_H - 1; y++)
				setShapeAt(x, y, shapeAt(x, y+1));
			setShapeAt(x, BOARD_H-1, (byte)-1);
		}
		for (int x = (int)(Math.random()*2); x< BOARD_W; x+=2) {
			setShapeAt(x, BOARD_H-1, (byte)9);
		}
	}
	public void setShapeAt(int x, int y, byte mark) {
		gameBoard[x + y * BOARD_W] = mark;
	}
	
	public byte shapeAt(int x, int y) {
		return gameBoard[ y * BOARD_W + x];
	}
	/* 移动下落数组 */
	public boolean fallPieceMove(int newX, int newY) {
		Point[] oldCoords = fallPiece.getFallCoords();
		boolean canMove = tryMove(oldCoords, newX, newY);
		
		return canMove;						
	}
	/* 旋转下落数组 */
	public void fallPieceRotate() {
		
		Point[] rotated = fallPiece.rotate();
		if(tryMove(rotated, 0, 0)) {
			fallPiece.setFallCoords(rotated);
		}
	}

	public boolean tryMove(Point[] oldCoords, int newX, int newY) {
		int len = oldCoords.length;
		for (int i = 0; i < len; ++i) {
			int x = newX + oldCoords[i].x;
			int y = newY + oldCoords[i].y;
			if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y)
				return false;
			 if (shapeAt(x, y) != -1)
			 return false;
		}
		/* side effect */
		for (int i = 0; i < len; i++) {
			oldCoords[i].x += newX;
			oldCoords[i].y += newY;
		}
		copyToBottom(oldCoords);
		return true;
	}
	
	public void copyToBottom(Point[] coords) {
		Point[] bottomCoords = GameMethod.copyCoords(coords);
		while(tryMove(bottomCoords, 0, 1))
			fallPiece.setBottomCoords(bottomCoords);
	}


	public void setDbRecord(List<Player> dbRecord) {
		this.dbRecord = this.setFullRecord(dbRecord);
	}

	public List<Player> getDbRecord() {
		return dbRecord;
	}

	public void setLocalRecord(List<Player> localRecord) {
		this.localRecord = this.setFullRecord(localRecord);
	}

	public List<Player> getLocalRecord() {
		return localRecord;
	}

	/**
	 * 填充并排序对数据排序
	 */
	private List<Player> setFullRecord(List<Player> keepers) {
		if (keepers == null) {
			keepers = new ArrayList<Player>();
		}
		while (keepers.size() < 5) {
			keepers.add(new Player("No Data", 0));
		}
		Collections.sort(keepers);
		return keepers;
	}

	//TODO
	public byte[] getGameBoard() {
		return gameBoard;
	}
	public void setLevel(int level) {
		status.level = level;
		status.speed = GameFunction.getSleepTimeByLevel(status.level);
		if (level > HARDER)
			obstacleTimer.setDelay(GameFunction.getIntervalMinute(level));
	}

	public void setStart(boolean start) {
		status.start = start;
		if (start) {
			obstacleTimer.start();
		} else {
			obstacleTimer.stop();
		}
	}

	public void changeShadow() {
		status.shadow = !status.shadow;
	}
	
	public void changePause() {
		this.status.pause = !status.pause;
		if (status.pause) {
			obstacleTimer.stop();
		} else {
			obstacleTimer.start();
		}
	}
	public Tetriminos getFallPiece() {
		return fallPiece;
	}

	public void setFallPiece(Tetriminos fallPiece) {
		this.fallPiece = fallPiece;
	}

	public LinkedList<Tetriminos> getDropQueue() {
		return dropQueue;
	}

	public GameStatus getStatus() {
		return status;
	}
}
