package hidatohippy;

import java.awt.Point;

public enum HidatoAction {
	MOVE_UP_LEFT(-1,-1), 
	MOVE_UP(0,-1), 
	MOVE_UP_RIGHT(1,-1),
	MOVE_LEFT(-1,0), 
	MOVE_RIGHT(1,0),
	MOVE_DOWN_LEFT(-1,1), 
	MOVE_DOWN(0,1), 
	MOVE_DOWN_RIGHT(1,1);
	
	public final int movesX;
	public final int movesY;
	
	HidatoAction(int movesX, int movesY) {
		this.movesX = movesX;
		this.movesY = movesY;
	}
	
	public Point move(Point p) {
		return new Point(p.x + movesX, p.y + movesY);
	}
}