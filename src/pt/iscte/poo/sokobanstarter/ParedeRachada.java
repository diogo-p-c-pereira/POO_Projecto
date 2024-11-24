package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends GameElement implements Interactable{
	
	public ParedeRachada(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "ParedeRachada";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void interacts(GameElement e) {
		if(e instanceof Empilhadora) {
			if(((Empilhadora)e).hasHammer()) {
				engine.removeElementfromGui(this);
				engine.removeInteractable(this);
				((Empilhadora)e).useHammer();
			}
		}
	}

}
