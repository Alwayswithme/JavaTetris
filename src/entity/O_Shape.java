package entity;

import java.awt.Point;

public class O_Shape extends Tetriminos {

	public O_Shape( byte ID, boolean rotatable, int rotateFlag, Point[] fall) {
	    super(ID, rotatable, rotateFlag, fall);
	    
    }
	@Override
	public Point[] rotate() {
		return fallCoords;
	}
}
