package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Caixote extends MovableElement {
	
//////// Metodos Estaticos, apoio de eficiencia para contagem do n de caixotes ////////////////////	
	private static int numCaixote = 0;
	
	public static int getNum() {
		return numCaixote;
	}
	
	public static void reduce() {
		numCaixote--;
	}
	
	public static void reset() {
		numCaixote = 0;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Caixote(Point2D Point2D){
		super(Point2D);
		numCaixote++;
	}
	
	@Override
	public String getName() {
		return "Caixote";
	}

	@Override
	public int getLayer() {
		return 2;
	}

}
