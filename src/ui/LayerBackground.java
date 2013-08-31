package ui;

import java.awt.Graphics;

public class LayerBackground extends Layer {
	public LayerBackground (int x, int y, int w, int h) {
		super (x, y, w, h);
	}

	public void paint(Graphics g) {
		int bgIdx = (gameDto.getStatus().getLevel() - 1) % GameImg.BG_IMG.size();
		g.drawImage(GameImg.BG_IMG.get(bgIdx), 0, 0, null);
	}
}
