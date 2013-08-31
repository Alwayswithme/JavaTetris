package ui;

import java.awt.Graphics;
import java.awt.Image;

public class LayerAbout extends Layer {
	public LayerAbout (int x, int y, int w, int h) {
		super (x, y, w, h);
	}

	public void paint(Graphics g) {
		this.drawContainer(g);
		if(gameDto.getStatus().isStart()) {
			drawImgAtCenter(g, imgOfQueue(1),imgOfQueue(2));
		}
	}

	/**
	 * 通过索引获取队列中方块的提示图片
	 * @param index  nextCube队列中的方块的索引
	 * @return 队列中第no+1个方块的提示图
	 */
	public Image imgOfQueue(int index) {
	    return GameImg.HINT_CUBE[gameDto.getDropQueue().get(index).getShapeID()];
    }
}
