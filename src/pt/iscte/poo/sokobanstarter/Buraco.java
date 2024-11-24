package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement implements Interactable{
	
	public Buraco(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void interacts(GameElement e) {
		if(e instanceof Empilhadora) {
			engine.resetLevel();
		}
		if(e instanceof Palete) {
			engine.removeElementfromGui(this);
			engine.removeInteractable(this);
			engine.removeElementfromGui(e);
			engine.removeMovableElement((MovableElement)e);
		}
		if(e instanceof Caixote) {
			engine.removeElementfromGui(e);
			engine.removeMovableElement((MovableElement)e);
			Caixote.reduce();
			/*if(!(engine.numOfBox())){
				engine.resetLevel();
			}*/
		}
	}
	
}
