package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement implements Interactable{
	
	private static Bateria b1;   /// Como só podem existir 2 baterias por nivel, estao pre-definidas estaticamente para garantir que so existem 2
	private static Bateria b2;
	
	private final int charge = 50;
	
	public Bateria(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Bateria";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public int getCharge() {
		return charge;
	}
	
	public static Bateria create(Point2D point) {
		if(b1==null) {
			b1 = new Bateria(point);
			return b1;
		}
		if(b2==null) {
			b2 = new Bateria(point);
			return b2;
		}
		throw new IllegalStateException();
	}
	
	public static void reset() {
		b1=null;
		b2=null;
	}

	@Override
	public void interacts(GameElement e) {
		if(e instanceof Empilhadora) {
			((Empilhadora)e).getCharge().addCharge(charge);
			engine.removeElementfromGui(this);
			engine.removeInteractable(this);
		}
		
	}
	
}
