package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Score {
	
	//private static List<Score> scores = new ArrayList<>();
	private static Score SCORE_INSTANCE;
	
	public static Score getInstance(String player) {
		if (SCORE_INSTANCE==null)
			return SCORE_INSTANCE = new Score(player);
		return SCORE_INSTANCE;
	}
	
	//private final LocalDateTime dateTime; 
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private Map<Integer, List<LevelScore>> levelMap; //mapa dos scores de cada nivel, Integer - numero do nivel, List lista das pontuacoes de cada nivel
	private final String player;
	private final String SCORE_DIR = "scores";
	
	public Score(String player){
		this.player = player;
		levelMap = new HashMap<>();
		if (!new File(SCORE_DIR).exists()) {
			new File(SCORE_DIR).mkdir();
		}
	}
	
	private class LevelScore{
		private final int points;
		private final int moves;  //preservação de dados
		private final String player;
		private final LocalDateTime dateTime;
		
		public LevelScore(String player, int moves) {
			this.moves=moves;
			points = Math.max(0, 1000-(moves*10));
			this.player=player;
			dateTime = LocalDateTime.now();
		}
		
		public LevelScore(String str){    //Formato:   <player>;<dateTime>;<points>;<moves>
			String[] data = str.split("\\;");
			player = data[0];
			dateTime = LocalDateTime.parse(data[1], dtf);
			points = Integer.parseInt(data[2]);
			moves = Integer.parseInt(data[3]);
		}
		
		public int getPoints(){
			return points;
		}
		
		public String getPlayer() {
			return player;
		}
		
		public LocalDateTime getDateTime() {
			return dateTime;
		}
		
		@Override
		public String toString() {
			return player + ";" + dtf.format(dateTime) + ";" + points + ";" + moves;
		}
	}
	
	private class scoreComparator implements Comparator<LevelScore>{
		@Override
	    public int compare(LevelScore a, LevelScore b) {
	        return b.getPoints()-a.getPoints();
	    }
	}
	
	
	public void addScore(int level,int moves) {
		loadScores(level);
		List<LevelScore> currentLevel = levelMap.get(level);
		if(currentLevel==null) {
			currentLevel = new ArrayList<>();
			levelMap.put(level, currentLevel);
		}
		LevelScore score = new LevelScore(player,moves);
		currentLevel.add(score);
		Collections.sort(currentLevel, new scoreComparator());  //Os scores ficam ordenados por pontuacao, da maior para a menor no ficheiro
		if(currentLevel.get(0).equals(score)){
			GameEngine.getInstance().setMessage("New Highscore!!!");
		}
		try { writeLevelToFile(level); } catch (FileNotFoundException e) {}
	}
	
	public void loadScores(int level){ 
		Scanner scanner;
		try {
			scanner = new Scanner(new File(SCORE_DIR + "/level" + level + ".txt"));
			List<LevelScore> currentLevel = new ArrayList<>();
			levelMap.put(level, currentLevel);
			while(scanner.hasNextLine()) {
				String data = scanner.nextLine();
				 if (data.startsWith("#")){
					 continue;
				 }
				currentLevel.add(new LevelScore(data));
			}
			scanner.close();
		} catch (FileNotFoundException e) {}
	}
	
	//Criado um ficheiro por nivel que no cabecalho guarda o top 3 e por baixo guarda os scores todos ordenados port pontuacao no formato indicado no loadScores()
	private void writeLevelToFile(int level) throws FileNotFoundException { 
		PrintWriter fileWriter = new PrintWriter(new File(SCORE_DIR + "/level" + level + ".txt")); 
		List<LevelScore> currentLevel = levelMap.get(level);
		fileWriter.println("###Top-3 Highscores###");
		for(int i = 0; i<3; i++) {
			try{
				LevelScore e = currentLevel.get(i);
				fileWriter.println( "# " + String.valueOf(i+1) +" - " +e.getPlayer() + " " + e.getPoints() + " " + dtf.format(e.getDateTime()));
			}catch(IndexOutOfBoundsException e){ //Caso o index nao exista quer dizer que não ha dados suficiente pata o top 3 
				fileWriter.println("# " + i +" -");  //Caso nao haja scores suficientes para o top 3
			}
		}
		fileWriter.println("#####################");
		for(LevelScore l : currentLevel) {
			fileWriter.println(l);
		}
		fileWriter.close();
	}
	
}
