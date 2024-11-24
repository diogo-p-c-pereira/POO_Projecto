package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement implements Interactable{
	
/////////// Metodos Estaticos, apoio de eficiencia para contagem do n de alvos //////////////
	private static int numAlvos = 0;
	
    public static int getNum() {
    	return numAlvos;
    }
    public static void reset() {
    	numAlvos=0;
    }
 /////////////////////////////////////////////////////////////////////////////////////////
    
	private boolean status;  //saber se o alvo tem um caixote
	
	public Alvo(Point2D Point2D) {
		super(Point2D);
		status = false;
		numAlvos++;
	}

	@Override
	public String getName() {
		return "Alvo";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	public void setStatus(Boolean status) {
       this.status=status;
    }

    public boolean getStatus() {
        return status;
    }
	@Override
	public void interacts(GameElement e) {
		if(e instanceof Caixote) {
			engine.target();
		}
	}

	
}
