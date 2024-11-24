package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableElement extends GameElement{
	
	protected GameEngine engine = GameEngine.getInstance();
	
	public MovableElement(Point2D Point2D) {
		super(Point2D);
	}
	
	public boolean move(Direction d) { //boolean para verificar se o elemento bate contra a parede qd empurrado, logo se moveu o nãp
		Point2D newPosition = getPosition().plus(d.asVector()); 
		if (newPosition.getX()>=0 && newPosition.getX()<10 && newPosition.getY()>=0 && newPosition.getY()<10 ){
			GameElement i = engine.interacts(newPosition);
			if(!engine.isBlocked(newPosition) && engine.colides(newPosition)==null && !(i instanceof ParedeRachada)) {
				if(i==null || (i.getLayer()!=this.getLayer())) {  //verifica se a lauer é a mesma do interactable pq nao pode colidir com a bateria e o martelo, logo implementamos o layer para fazer distincao dos que podem sobrepor-se
					setPosition(newPosition);
					if(i instanceof Interactable) {
						((Interactable) i).interacts(this);
					}
					return true;
				}
			}
		}
		return false;
	}
	
}
