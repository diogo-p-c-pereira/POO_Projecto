package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement implements Interactable{
	
//////// Metodos estaticos //////////////////////////////
	private static Teleporte t1;   //Como só existem 2 teleportes por nivel, foram ambos definidos estaticamente por questoes
	private static Teleporte t2;   //de eficiencia para evitar procuras lineares como para limitar o seu numero
	
	public static Teleporte create(Point2D point) {
		if(t1==null) {
			t1 = new Teleporte(point);
			return t1;
		}
		if(t2==null) {
			t2 = new Teleporte(point);
			return t2;
		}
		throw new IllegalStateException();
	}
	
	public static void reset() {
		t1=null;
		t2=null;
	}
////////////////////////////////////////////////////
	
	public Teleporte(Point2D Point2D) {
		super(Point2D);
	}

	@Override
	public String getName() {
		return "Teleporte";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void interacts(GameElement e){
		try{ 
			if(this.equals(t1)) {
				if(GameEngine.getInstance().colides(t2.getPosition())==null) {
					e.setPosition(t2.getPosition());
				}
			}else{
				if(GameEngine.getInstance().colides(t1.getPosition())==null) {
					e.setPosition(t1.getPosition());
				}
			}
		}catch(NullPointerException ex) { } //Nao faz nada caso um dos teleportes não esteja atribuido
	}
}
