package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Palete extends MovableElement {

	public Palete(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Palete";
	}

	@Override
	public int getLayer() {
		return 2;
	}
}
