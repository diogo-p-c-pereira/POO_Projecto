package pt.iscte.poo.sokobanstarter;

public class Charge {
	private final int INITIAL_CHARGE;  //Valor maximo de carga
	private int chargeLevel;
	
	public Charge(int value) {
		INITIAL_CHARGE = value;
		chargeLevel= INITIAL_CHARGE;
	}
	
	public int getCharge() {
		return chargeLevel;
	}
	
	public boolean canUse(int value) {
		return (chargeLevel-value)>=0;
	}
	
	public void addCharge(int value) {
		chargeLevel+=value;
		if(chargeLevel>INITIAL_CHARGE) {
			chargeLevel=INITIAL_CHARGE;
		}
	}
	
	public void consumeCharge(int value) {
		chargeLevel-=value;
		if(chargeLevel<0) {
			chargeLevel=0;
		}
	}
	
	public void reset() {
		chargeLevel=INITIAL_CHARGE;
	}
}
