package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import config.FrameConfig;
import config.GameConfig;
import dto.GameDto;

/**
 * 游戏各分离层，小型窗口 相当于容器
 * 
 * @author phx
 */
public abstract class Layer {
	protected static GameDto gameDto;
	// 窗口的边框大小
	public static int BORDER_SIZE;
	
	// 内边距
	protected static final int PADDING;
	// 绘制专用字体
	protected static final Font MY_FONT = new Font("AR PL UMing CN", Font.PLAIN, 17);
	// 绘制专用数字格式右对齐
	protected static final String form = "%1$7s";
	// 窗口素材宽高
	protected static final int WINDOW_W, WINDOW_H;
	// 值槽图片宽高
	protected static final int RECT_W, RECT_H;
	// 数字图片宽高
	protected static final int NUM_W, NUM_H;
	static {
		// 获取游戏配置,获取各层所需素材宽高
		FrameConfig fCfg = GameConfig.getFRAME();
		BORDER_SIZE = fCfg.getBorderSize();
		PADDING = fCfg.getPadding();

		WINDOW_W = GameImg.WINDOW.getWidth(null);
		WINDOW_H = GameImg.WINDOW.getHeight(null);
		NUM_W = GameImg.NUM.getWidth(null) / 10;
		NUM_H = GameImg.NUM.getHeight(null);
		RECT_W = GameImg.RECT.getWidth(null);
		RECT_H = GameImg.RECT.getHeight(null);
	}
	
	// 定义层坐标，宽高 空白final，根据对象而有所不同
	// locate (x,y) 定位各层的绘制起点
	// layerWidth, layerHeight 窗口宽高
	protected final int locateX;
	
	protected final int locateY;
	
	protected final int layerWidth;
	
	protected final int layerHeight;
	// 部分窗口有标题，这两个坐标定义标题出现的位置
	protected final int TITLE_X, TITLE_Y;
	// 经验值槽宽度
	protected static int EXP_WIDTH;
	
	public Layer( int x, int y, int w, int h) {
		locateX = x;
		locateY = y;
		layerWidth = w;
		layerHeight = h;
		TITLE_X = this.locateX + PADDING;
		TITLE_Y = this.locateY + PADDING;
		EXP_WIDTH = layerWidth - (PADDING << 1);
	}
	
	abstract public void paint(Graphics g);
	
	/*
	 * 根据传入参数计算各切片的坐标和大小 然后进行绘制容器
	 */
	public void drawContainer(Graphics g) {
		// 边框的长度和高度，不包括四个角
		int subWidth = layerWidth - (BORDER_SIZE << 1);
		int subHeight = layerHeight - (BORDER_SIZE << 1);
		
		// 中间四边形的四个角坐标,其中两两对应相同
		int left_x = locateX + BORDER_SIZE;
		int right_x = locateX + BORDER_SIZE + subWidth;
		int top_y = locateY + BORDER_SIZE;
		int bottom_y = locateY + BORDER_SIZE + subHeight;
		drawTool tool = new drawTool();
		// 分别绘制九个切片
		tool.topLeftCorner(g, locateX, locateY);
		tool.topRightCorner(g, right_x, locateY);
		tool.bottomleftCorner(g, locateX, bottom_y);
		tool.bottomRightCorner(g, right_x, bottom_y);
		
		tool.center(g, left_x, top_y, subWidth, subHeight);
		
		tool.topBorder(g, left_x, locateY, subWidth);
		tool.rightBorder(g, right_x, top_y, subHeight);
		tool.bottomBorder(g, left_x, bottom_y, subWidth);
		tool.leftCorner(g, locateX, top_y, subHeight);
		
	}
	
	/**
	 * 左填充绘制数字
	 * 
	 * @param lastX
	 *            最后一位数字定位的x值
	 * @param y
	 *            所有数字定位y值
	 * @param num
	 *            需要绘制的数字
	 * @param g
	 *            画笔
	 */
	protected void drwaNumber(int lastX, int y, int num, Graphics g) {
		int i = 0;
		do {
			
			int temp = num % 10; // 求模获得最后一位数字
			int offsetX = locateX + lastX - (i * (NUM_W >> 1));// 向左偏移
			int imgX = temp * NUM_W;
			g.drawImage(GameImg.NUM, offsetX, y, offsetX + (NUM_W >> 1), y + (NUM_H >> 1),
			                imgX, 0, imgX + NUM_W, NUM_H, null);
			num /= 10; // 去除已获得的数字
			i++;
		} while (num > 0);
	}
	
	/**
	 * 正中间显示图片
	 */
	protected void drawImgAtCenter(Graphics g, Image... imgs) {
		//  获取传入图片数量
		int num = imgs.length;
		
		int[] x = new int[num];
		// 存放边距的变量
		int marginW = layerWidth;
		for (int i = 0; i < num; i++) {
	        marginW -= imgs[i].getWidth(null);
        }
		marginW = marginW / (num+1);
		/* 后一个坐标等于前一个坐标和前一张图片的宽度加上间距 */
		x[0] = locateX + marginW;
		for (int i = 1; i < num; i++) {

			x[i] = x[i-1] + imgs[i-1].getWidth(null) + marginW ;
			
		}
		for (int i = 0; i < num; i++) {
	        int offsetY = locateY + (layerHeight - imgs[i].getHeight(null) >> 1) ;
			g.drawImage(imgs[i], x[i], offsetY, null);
        }

	}
	
	/**
	 * 对窗口进行切片处理的内部类
	 * 
	 * @author phx
	 */
	protected class drawTool {
		public void topLeftCorner(Graphics g, int x, int y) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + BORDER_SIZE, 0, 0,
			                BORDER_SIZE, BORDER_SIZE, null);
		};
		
		public void topRightCorner(Graphics g, int x, int y) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + BORDER_SIZE, WINDOW_W
			                - BORDER_SIZE, 0, WINDOW_W, BORDER_SIZE, null);
		}
		
		public void bottomleftCorner(Graphics g, int x, int y) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + BORDER_SIZE, 0, WINDOW_W
			                - BORDER_SIZE, BORDER_SIZE, WINDOW_W, null);
		}
		
		public void bottomRightCorner(Graphics g, int x, int y) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + BORDER_SIZE, WINDOW_W
			                - BORDER_SIZE, WINDOW_W - BORDER_SIZE, WINDOW_W, WINDOW_W, null);
		}
		
		public void topBorder(Graphics g, int x, int y, int length) {
			g.drawImage(GameImg.WINDOW, x, y, x + length, y + BORDER_SIZE, BORDER_SIZE, 0,
			                WINDOW_W - BORDER_SIZE, BORDER_SIZE, null);
		}
		
		public void rightBorder(Graphics g, int x, int y, int length) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + length, WINDOW_W
			                - BORDER_SIZE, BORDER_SIZE, WINDOW_W, WINDOW_H - BORDER_SIZE, null);
		}
		
		public void bottomBorder(Graphics g, int x, int y, int length) {
			g.drawImage(GameImg.WINDOW, x, y, x + length, y + BORDER_SIZE, BORDER_SIZE,
			                WINDOW_W - BORDER_SIZE, WINDOW_W - BORDER_SIZE, WINDOW_W, null);
		}
		
		public void leftCorner(Graphics g, int x, int y, int length) {
			g.drawImage(GameImg.WINDOW, x, y, x + BORDER_SIZE, y + length, 0, BORDER_SIZE,
			                BORDER_SIZE, WINDOW_W - BORDER_SIZE, null);
		}
		
		public void center(Graphics g, int x, int y, int width, int height) {
			g.drawImage(GameImg.WINDOW, x, y, x + width, y + height, BORDER_SIZE, BORDER_SIZE,
			                WINDOW_W - BORDER_SIZE, WINDOW_H - BORDER_SIZE, null);
		}
	}
	
	/**
	 * 绘制矩形值槽，由当前值和最大值的比例绘制动态增长的值槽 绘制起始的x坐标皆为 winX + PADDING，即绘制窗口的x坐标加边距
	 * 绘制起始的y坐标需要传入 值槽总宽度为 winWidth - 2*PADDING,窗口总宽度减去左右边距
	 * 
	 * @param y      值槽绘制的y坐标 
	 * @param rate   比值          
	 * @param g
	 * @param desc
	 * @param num          
	 */
	protected void drawRect(int y, double rate, Graphics g, String describe, String num) {
		// 绘制外围边框
		
		// 绘制一个黑色外围边框
		g.setColor(Color.BLACK);
		g.drawRect(TITLE_X, y, EXP_WIDTH, RECT_H + 4);
		// 绘制白色中间边框
		g.setColor(Color.WHITE);
		g.drawRect(TITLE_X + 1, y + 1, EXP_WIDTH - 2, RECT_H + 2);
		// 绘制黑色背景
		g.setColor(Color.BLACK);
		g.fillRect(TITLE_X + 2, y + 2, EXP_WIDTH - 4, RECT_H);
		
		// 绘制填充量
		// 宽度，值槽填充量
		int w = (int) (rate * (EXP_WIDTH - 4));
		// 求出颜色 比例乘以图片宽度 即可定位到值槽图片不同颜色的点
		int index = (int) (rate * RECT_W) - 1;
		g.drawImage(GameImg.RECT, TITLE_X + 2, y + 2, TITLE_X + 3 + w, y + 3 + RECT_H,
		                index, 0, index + 1, RECT_H, null);
		
		// 绘制文字
		g.setColor(Color.WHITE);
		g.setFont(MY_FONT);
		g.drawString(describe, TITLE_X + 10, y + 25);
		// 如果传入数字不为空则绘制数字
		if (num != null) {
			g.drawString(String.format(form, num), TITLE_X + 220, y + 25);
		}
	}
	
	public void setDto(GameDto dto) {
		gameDto = dto;
	}
}
