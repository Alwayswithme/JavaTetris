package ui;

import java.awt.Graphics;

public class LayerLevel extends Layer {
	
	//等级数字出现位置
	private static int SCORE_X;
	public LayerLevel (int x, int y, int w, int h) {
		super (x, y, w, h);
		SCORE_X = layerWidth-PADDING-(NUM_W>>1);
	}

	public void paint(Graphics g) {
		this.drawContainer(g);
		//副标题 显示等级图片
		g.drawImage(GameImg.LV, TITLE_X, TITLE_Y, null);
		//显示当前等级数
		drwaNumber(SCORE_X, TITLE_Y + 50, gameDto.getStatus().getLevel(), g);
	}
}
