
package ui;

import java.awt.Graphics;
import config.GameConfig;

public class LayerScore extends Layer {
	
	//数字共用的x坐标
	private final int shareX;
	//第二个标题(消行)需要修正
	private final int titleFixY;
	//经验y坐标
	private final int expY;
	//经验值槽y坐标
	
	//升级行数
	private static final int LEVEL_UP = GameConfig.getSYSTEM().getLevelUp();
	public LayerScore (int x, int y, int w, int h) {
		super (x, y, w, h);
		//分数x坐标,分数出现在距窗口右边框一个PADDING处
		shareX = layerWidth-PADDING-(NUM_W>>1);
		//消行标题y坐标，消行标题距离分数标题PADDING像素
		titleFixY =	TITLE_Y + GameImg.POINT.getHeight(null) + PADDING;
		//经验值槽y坐标，距离消行标题y坐标PADDING像素
		expY = titleFixY + GameImg.RMLINE.getHeight(null) + PADDING;
	}

	public void paint(Graphics g) {
		this.drawContainer(g);
		//显示标题(分数)
		g.drawImage(GameImg.POINT, TITLE_X, TITLE_Y, null);
		//显示分数
		drwaNumber(shareX, TITLE_Y, gameDto.getStatus().getScore(), g);
		//显示标题(消行)
		g.drawImage(GameImg.RMLINE, TITLE_X, titleFixY, null);
		int rmLine = gameDto.getStatus().getRemoveLine();
		//显示消行数
		drwaNumber(shareX, titleFixY, rmLine, g);
		//绘制值槽
		this.drawRect(expY, (double)(rmLine % LEVEL_UP)/LEVEL_UP, g, "Next Level", null);
	}


}
