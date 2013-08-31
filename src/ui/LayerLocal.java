package ui;

import java.awt.Graphics;

public class LayerLocal extends LayerData {
	
	public LayerLocal (int x, int y, int w, int h) {
		super (x, y, w, h);
	}

	@Override
	public void paint(Graphics g) {
		this.drawContainer(g);
		this.showData(GameImg.LOCAL, gameDto.getLocalRecord(), g);
	}
}
