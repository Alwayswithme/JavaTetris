package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import config.GameConfig;
import dto.GameDto;
import entity.Tetriminos;

public class LayerGame extends Layer {
	
	// 阴影颜色
	private static final Color C = new Color(64, 64, 64, 64);
	// 位运算:左移5位即乘32 方块大小
	private static final int CUBE_SIZE = GameConfig.getFRAME().getCubeSize();
	private static final int LOSE_PIC = GameConfig.getFRAME().getLostIdx();
	private static final int RIGHT_SIZE = GameConfig.getSYSTEM().getMaxX();
	private static final int LEFT_SIZE = GameConfig.getSYSTEM().getMinX();
	
	public LayerGame( int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	public void paint(Graphics g) {
		this.drawContainer(g);
		Tetriminos fallPiece = gameDto.getFallPiece();
		if (fallPiece != null) {
			// 获得方块坐标
			Point[] points = fallPiece.getFallCoords();
			// 显示阴影
			this.drawShadow(points, g);
			// 绘制活动方块
			this.drawFallCube(g, points, fallPiece.getShapeID());
			// 预览显示位置			
			this.drawFallCube(g, fallPiece.getBottomCoords(), 8);
		}
		// 绘制方块容器
		this.drawGameMap(g);
		//暂停
		if(gameDto.getStatus().isPause()) {
			this.drawImgAtCenter(g, GameImg.PAUSE);
		}
	}


	private void drawGameMap(Graphics g) {
		byte[] board = gameDto.getGameBoard();

		for (int x = 0, wid = GameDto.BOARD_W; x < wid; x++) {
			for (int y = 0; y < GameDto.BOARD_H; y++) {
				// 如果游戏图中位置不为-1，则绘制出填充用的方块
				byte shapeID = board[x + y * wid];
				if (shapeID != -1) {
					this.drawCube(x, y, shapeID, g);
				}
			}
		}
	}
	/* 根据方块编号取得对应图片索引*/	
	private void drawFallCube(Graphics g, Point[] points, int imgIndex) {
			// 计算切割和出现位置
		if (points != null)
		for (int i = 0; i < points.length; i++) {
			this.drawCube(points[i].x, points[i].y, imgIndex, g);
		}
		
	}
	
	/**
	 * 根据方块的坐标showX showY计算该方块在对应窗口的出现位置 由imgIndex获得需要切割的方块图片
	 * 
	 * @param showX
	 *            方块x坐标，乘以32,加上窗口左上角x坐标和边框大小即相对出现坐标
	 * @param showY
	 *            方块y坐标，乘以32,加上窗口左上角y坐标和边框大小即相对出现坐标
	 * @param imgIndex
	 *            对应CUBE图片中个颜色方块
	 * @param g
	 */
	public void drawCube(int showX, int showY, int imgIndex, Graphics g) {
		// 游戏失败则绘制失败方块
		imgIndex = gameDto.getStatus().isStart() ? imgIndex : LOSE_PIC;
		// 计算相对出现坐标
		int relatX = locateX + (showX << CUBE_SIZE) + BORDER_SIZE;
		int relatY = locateY + (showY << CUBE_SIZE) + BORDER_SIZE;
		// 由图片索引获得对应切割坐标
		int splitX = imgIndex << CUBE_SIZE;
		g.drawImage(GameImg.CUBE, relatX, relatY, relatX + 32, relatY + 32,
		                splitX, 0, splitX + 32, 32, null);
	}
	
	private void drawShadow(Point[] points, Graphics g) {
		if (gameDto.getStatus().isShadow())
			return;
		// 求出左右的最大边距
		int min = RIGHT_SIZE;
		int max = LEFT_SIZE;
		for (Point p : points) {
			min = min > p.x ? p.x : min;
			max = max < p.x ? p.x : max;
		}
		// 设置阴影颜色
		g.setColor(C);
		g.fill3DRect(locateX + BORDER_SIZE + (min << 5), locateY + BORDER_SIZE,
		                (max - min + 1) << 5, layerHeight - (BORDER_SIZE << 1), true);
	}
	

}
