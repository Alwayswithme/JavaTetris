package entity;

import java.awt.Point;

public class I_Shape extends Tetriminos {

	public I_Shape( byte ID, boolean rotatable, int rotateFlag, Point[] fall) {
	    super(ID, rotatable, rotateFlag, fall);
	    
    }
	public Point[] rotate() {
		int len = fallCoords.length;
		int y = fallCoords[0].y;
		Point[] rotated = new Point[len];
		
		if( (fallCoords[0].x == MIN_X+1 && fallCoords[0].x == fallCoords[1].x) || fallCoords[0].x == MIN_X ) {
			for (int i = 0; i< len ; i++) {
				rotated[i] = new Point((i + 1 ) % len, y );
			}
			return rotated;
		}else if((fallCoords[0].x == MAX_X-1 && fallCoords[0].x == fallCoords[1].x) || fallCoords[0].x == MAX_X ) {
			for (int i = 0; i< len ; i++) {
				rotated[i] = new Point((MAX_X - 3 + ( i + 2 ) % len) , y );
			}
			return rotated;
		}else {
			return super.rotate();
		}
	}

}
