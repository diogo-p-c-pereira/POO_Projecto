package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class PlayableElement extends GameElement{
	
	protected GameEngine engine = GameEngine.getInstance();
	protected int moves;
	
	public PlayableElement(Point2D Point2D) {
		super(Point2D);
	}
	
	public abstract boolean move(Direction d);
	
	public void reset() {
		moves=0;
	}
	
	public int getMoves() {
		return moves;
	}
	
}
