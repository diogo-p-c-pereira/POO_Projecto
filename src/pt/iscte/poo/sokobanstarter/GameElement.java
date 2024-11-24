package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageTile;

public abstract class GameElement implements ImageTile{
	
	private Point2D position;
	
	public abstract String getName();
	
	public GameElement(Point2D Point2D) {
		this.position = Point2D;
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	protected void setPosition(Point2D position) {
		this.position=position;
	}
	
	
	@Override
	public abstract int getLayer();
	
}
