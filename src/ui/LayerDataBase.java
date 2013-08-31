package ui;

import java.awt.Graphics;

public class LayerDataBase extends LayerData {
	
	public LayerDataBase (int x, int y, int w, int h) {
		super (x, y, w, h);
	}

	@Override
	public void paint(Graphics g) {
		this.drawContainer(g);
		this.showData(GameImg.DB, gameDto.getDbRecord(), g);
	}
}
