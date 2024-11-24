package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
/*import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;*/
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
//import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

// Note que esta classe e' um exemplo - nao pretende ser o inicio do projeto, 
// embora tambem possa ser usada para isso.
//
// No seu projeto e' suposto haver metodos diferentes.
// 
// As coisas que comuns com o projeto, e que se pretendem ilustrar aqui, sao:
// - GameEngine implementa Observer - para  ter o metodo update(...)  
// - Configurar a janela do interface grafico (GUI):
//        + definir as dimensoes
//        + registar o objeto GameEngine ativo como observador da GUI
//        + lancar a GUI
// - O metodo update(...) e' invocado automaticamente sempre que se carrega numa tecla
//
// Tudo o mais podera' ser diferente!


public class GameEngine implements Observer{

	// Dimensoes da grelha de jogo
	public final int GRID_HEIGHT = 10;
	public final int GRID_WIDTH = 10;
	

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui;  		// Referencia para ImageMatrixGUI (janela de interface com o utilizador) 
	private Empilhadora bobcat;	        // Referencia para a empilhadora
	private List<GameElement> bgElements; 
	private List<Parede> walls;
	private List<MovableElement> movables;
	private List<GameElement> interactables;
	private int currentLevel;
	private final String FILE_DIR = "levels";
	private Score scores;
	//private final String[] FILE_LIST;
	//private List<String> FILE_LIST;
	//private String level;

	// Construtor
	private GameEngine() {
		bgElements = new ArrayList<>();
		walls = new ArrayList<>();
		movables = new ArrayList<>();
		interactables = new ArrayList<>();
		
		/*FILE_LIST = Arrays.asList(new File(FILE_DIR).list(new FilenameFilter(){
			@Override
		    public boolean accept(File dir, String name) {
		        return name.matches("level\\d+\\.txt"); 
		    }
		}));*/
		currentLevel = 0;
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE==null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	// Inicio
	public void start(){

		// Setup inicial da janela que faz a interface com o utilizador
		// algumas coisas poderiam ser feitas no main, mas estes passos tem sempre que ser feitos!
		
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI
		
		scores = Score.getInstance(gui.askUser("Insira o se nome:")); 
		bobcat = new Empilhadora(null);
		//loadLevel(fileList[levelNumber]);
		gui.setMessage("Instrucoes: \n Movimento: Setas \n Reset: R \n Sair: Esc \n\nPressione Enter para Comecar!");
		//try { writeScoreInit(); } catch (FileNotFoundException e) {}
		try {
			/*for(String level: FILE_LIST) {
				loadLevel(new File(FILE_DIR + "/" + level));
				gui.update();
			}
			end(true);*/
			loadLevel(new File(FILE_DIR + "/level" + currentLevel + ".txt"));
			//sendImagesToGUI();      // Only use with loadElement() active
			
		} catch (FileNotFoundException e) {
			/*if(!(levelNumber==0)) {
				end(true);
			}*/
			throw new IllegalStateException("Level files missing");
		}
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban");
		gui.update();
	}
	
	protected void end(boolean status) { // se true passou os niveis todos, se false pressionou escape para sair
		gui.update();
		//scores.writeScores();
		//writeScoreFin();
		if(status) {
			gui.setMessage("Parabens! Todos os niveis concluidos!!!");
			System.exit(0);
		}
		gui.setMessage("GameOver");
		System.exit(0);
	}
	
	protected void resetLevel() {
		gui.update();
		gui.setMessage("Level Failed");
		try {loadLevel(new File(FILE_DIR + "/level" + currentLevel + ".txt"));} catch (FileNotFoundException e) {}
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado (neste caso a GUI)
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();    // obtem o codigo da tecla pressionada
		if(key == 82) {
			gui.setMessage("Reset Successful");
			try {
                loadLevel(new File("levels/level" + currentLevel + ".txt"));
            } catch (FileNotFoundException e) {}
		}
		if(key == 27) {
			end(false);
		}
		try{
			Direction d = Direction.directionFor(key);
			if(!bobcat.move(d)) { //Charge acabou, termina o jogo
				//end(false);
				resetLevel();
			}else{
				gui.setStatusMessage("Level: " + currentLevel + "  Moves: " + bobcat.getMoves() + "  Charge: " + bobcat.getCharge().getCharge());
				 if (nextlevel()) {
					 gui.update();
					 //try { writeScore(); } catch (FileNotFoundException e) {}
					 //scores.addLevelScore("Level " + String.valueOf(levelNumber), bobcat.getMoves());
					 scores.addScore(currentLevel, bobcat.getMoves());
					 gui.setMessage("Level Cleared!");
					 currentLevel++;
			            try {
			                loadLevel(new File("levels/level" + currentLevel + ".txt"));
			            } catch (FileNotFoundException e) {
			            	end(true);
			            }
			        }
			}
		}catch(IllegalArgumentException E) {}
		gui.update();                  // redesenha a lista de ImageTiles na GUI, 
		                               // tendo em conta as novas posicoes dos objetos
	}
	
	
	private void loadLevel(File file) throws FileNotFoundException{
			resetElements();
			char[][] level = new char[GRID_HEIGHT][GRID_WIDTH];
			Scanner scanner = new Scanner(file);
			for(int i=0; i<GRID_HEIGHT; i++) {
				String line = scanner.nextLine();
				level[i]=line.toCharArray();
				for(int j=0; j<GRID_WIDTH; j++) {
					loadElement(new Point2D(j,i),level[i][j]);
				}
			}
            scanner.close();
            if(bobcat.getPosition()==null) {
            	gui.setMessage("Empilhadora em falta!");
            	end(false);
            }
            sendImagesToGUI();
	}
	
	/*private void writeScore() throws FileNotFoundException {
		PrintWriter fileWriter;
		try {
			fileWriter = new PrintWriter(new FileWriter("scores.txt",true));
			fileWriter.println("Level" + String.valueOf(levelNumber) + " " + String.valueOf(Math.max(0, 1000-(bobcat.getMoves()*10))));
			fileWriter.close();
		} catch (IOException e) {}
		
	}*/
	
	/*private void writeScoreInit() throws FileNotFoundException {
		PrintWriter fileWriter;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			fileWriter = new PrintWriter(new FileWriter("scores.txt",true));
			fileWriter.println(dtf.format(now));
			fileWriter.close();
		} catch (IOException e) {}
	}
	
	private void writeScoreFin() {
		PrintWriter fileWriter;
		try {
			fileWriter = new PrintWriter(new FileWriter("scores.txt",true));
			fileWriter.println("\n\n");
			fileWriter.close();
		} catch (IOException e) {}
	}*/
	
	private void loadElement(Point2D Point2D, char element) {  //original loadElement
		switch(element){
			case ' ': bgElements.add(new Chao(Point2D)); break;
			case '=': bgElements.add(new Vazio(Point2D)); break;
			case '#': walls.add(new Parede(Point2D)); break;
			default:
				bgElements.add(new Chao(Point2D));
				switch(element) {
					case 'O': interactables.add(new Buraco(Point2D)); break;
					case 'T': try{ interactables.add(Teleporte.create(Point2D)); } catch(IllegalStateException e) { gui.setMessage("Mais do que 2 Teleportes inseridos!"); }
							  break;
					case 'B': try{ interactables.add(Bateria.create(Point2D)); } catch(IllegalStateException e) { gui.setMessage("Mais do que 2 Baterias inseridas!"); }
						      break;
					case 'X': interactables.add(new Alvo(Point2D)); break;
					case 'M': interactables.add(new Martelo(Point2D)); break;
					case '%': interactables.add(new ParedeRachada(Point2D)); break;
					case 'C': movables.add(new Caixote(Point2D)); break;
					case 'E': bobcat.setPosition(Point2D); break;
					case 'P': movables.add(new Palete(Point2D)); break;
					default: throw new IllegalArgumentException("Caracter Invalido!");
				}
		}
	}
	
	
	private void resetElements() {
		gui.clearImages();
		bgElements.clear();
		walls.clear();
		movables.clear();
		interactables.clear();
		bobcat.reset();
		Bateria.reset();
		Teleporte.reset();
		Alvo.reset();
		Caixote.reset();
	}
	

	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no inicio
	// Nao e' suposto re-enviar os objetos se a unica coisa que muda sao as posicoes  
	private void sendImagesToGUI() {  //Only use with loadElement()
		List<ImageTile> tmp = new ArrayList<>();
		tmp.addAll(bgElements);
		tmp.addAll(walls);
		tmp.addAll(movables);
		tmp.addAll(interactables);
		tmp.add(bobcat);
		gui.addImages(tmp);
	}
	
	public boolean isBlocked(Point2D Point2D) {
		for(Parede p: walls) {
			if(p.getPosition().equals(Point2D)) {
				return true;
			}
		}
		return false;
	}
	
	public MovableElement colides(Point2D Point2D) {
		for(MovableElement e: movables) {
			if(e.getPosition().equals(Point2D)) {
				return e;
			}
		}
		return null;
	}
	
	public GameElement interacts(Point2D Point2D) {
		for(GameElement e: interactables) {
			if(e.getPosition().equals(Point2D)) {
				return e;
			}
		}
		return null;
	}
	
	public void target() {
		
		List<GameElement> alvostmp = interactables.stream().filter(p -> p instanceof Alvo).collect(Collectors.toList());
		List<MovableElement> caixotestmp = movables.stream().filter(p -> p instanceof Caixote).collect(Collectors.toList());
		
		for (GameElement alvo : alvostmp) {
			boolean hit = false;
			for (MovableElement caixote : caixotestmp) {
				if ((alvo.getPosition()).equals(caixote.getPosition())) {
					hit = true;
					break;
				}
			}
			((Alvo)alvo).setStatus(hit);
		}
	}
			


    public boolean nextlevel() {
        for (GameElement e : interactables) {
            if (e instanceof Alvo) {
                if ((!((Alvo) e).getStatus())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    protected boolean numOfBox() {
		if (Alvo.getNum() > Caixote.getNum()) {
			gui.setMessage("Insuficient Boxes!");
			return false;
		}
		return true;
	}

	
	public void removeElementfromGui(GameElement e) {
		gui.removeImage(e);
	}
	
	public void removeMovableElement(MovableElement e) {
		movables.remove(e);
	}
	
	public void removeInteractable(GameElement e) {
		interactables.remove(e);
	}
	
	public void setMessage(String s) {
		gui.setMessage(s);
	}
	
}
