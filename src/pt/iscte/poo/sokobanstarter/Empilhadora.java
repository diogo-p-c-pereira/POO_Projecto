package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends PlayableElement{

	private String imageName;
	private Charge charge;
	private final int MOVE_CONSUMPTION = 1;
	private final int PUSH_CONSUMPTION = 2;
	private boolean hammer;
	
	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
		imageName = "Empilhadora_D";
		moves=0;
		charge = new Charge(100);
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public Charge getCharge() {
		return charge;
	}
	
	@Override
	public boolean move(Direction d) {     //Se devolver false faz reset do nivel no update (o nivel falhou)
		if(charge.canUse(MOVE_CONSUMPTION)) {
			Point2D newPosition = getPosition().plus(d.asVector()); 
			setImageDirection(d);
			if (newPosition.getX()>=0 && newPosition.getX()<engine.GRID_WIDTH && newPosition.getY()>=0 && newPosition.getY()<engine.GRID_HEIGHT ){
				MovableElement e = engine.colides(newPosition);
				GameElement i = engine.interacts(newPosition);
				if((!engine.isBlocked(newPosition)) && (e==null || e.move(d)) && (!(i instanceof ParedeRachada) || hammer)) {
					setPosition(newPosition);
					moves++;
					if(e!=null) {
						charge.consumeCharge(PUSH_CONSUMPTION);
					}else{
						charge.consumeCharge(MOVE_CONSUMPTION);
					}
					if(i instanceof Interactable) {
						((Interactable) i).interacts(this);
					}
					if((e instanceof Caixote) && !(engine.numOfBox())) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	private void setImageDirection(Direction d) {
		switch(d) {
			case UP: imageName = "Empilhadora_U"; break;
			case DOWN: imageName = "Empilhadora_D"; break;
			case LEFT: imageName = "Empilhadora_L"; break;
			case RIGHT: imageName = "Empilhadora_R"; break;
			default: throw new IllegalArgumentException();
		}
	}
	
	public void useHammer() {
		hammer = false;
	}
	
	public void addHammer() {
		hammer = true;
	}
	
	public boolean hasHammer() {
		return hammer;
	}
	
	@Override
	public void reset() {
		super.reset();
		charge.reset();
		useHammer();
	}
	
}