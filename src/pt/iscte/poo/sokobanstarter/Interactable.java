package pt.iscte.poo.sokobanstarter;

public interface Interactable {
	
	GameEngine engine = GameEngine.getInstance();
	
	public void interacts(GameElement e);
}
