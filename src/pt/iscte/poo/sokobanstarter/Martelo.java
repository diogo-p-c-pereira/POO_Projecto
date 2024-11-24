package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement implements Interactable{

	public Martelo(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Martelo";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public void interacts(GameElement e) {
		if(e instanceof Empilhadora) {
			((Empilhadora) e).addHammer();
			engine.removeElementfromGui(this);
			engine.removeInteractable(this);
		}
		
	}
	
}
