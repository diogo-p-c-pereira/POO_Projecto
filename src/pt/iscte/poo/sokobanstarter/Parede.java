package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement{
	
	public Parede(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Parede";
	}

	@Override
	public int getLayer() {
		return 0; 
	}
	

}
