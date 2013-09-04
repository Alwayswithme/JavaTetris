
package ui;

import java.awt.Graphics;
import java.awt.Image;
import util.ImageTool;
import entity.Tetriminos;

public class LayerHint extends Layer {
	
	public LayerHint (int x, int y, int w, int h) {
		super (x, y, w, h);
	}

	public void paint(Graphics g) {
		this.drawContainer(g);
		//如果游戏开始则画出提示方块
		if(gameDto.getStatus().isStart()) {
			// 准备下落的方块
			Tetriminos next = gameDto.getDropQueue().peek();
			//旋转次数
			int rotateCount = next.getRotateFlag();
			// 源提示图片
			Image src = GameImg.HINT_CUBE[next.getShapeID()];
			
			if  (rotateCount > 0) {
				Image i = ImageTool.rotate(src, rotateCount*90);
				drawImgAtCenter(g, i);
			}else {
				drawImgAtCenter(g, src);
			}
		}
	}

}
