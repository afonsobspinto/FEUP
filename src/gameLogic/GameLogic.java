
package gameLogic;

import java.util.concurrent.ThreadLocalRandom;

import console.Interaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.lang.Character;


public class GameLogic {

	private Board board;
	private Hero hero;
	private Guard guard;
	private ArrayList<CrazyOgre> crazyOgres;
	private GameConfig gameConfig;
	boolean won;
	boolean gameOn;
	Level level;
	
	public Hero getHero() {
		return hero;
	}

	
	
	public ArrayList<CrazyOgre> getCrazyOgres() {
		return crazyOgres;
	}



	public GameLogic(Level level, GameConfig gameConfig){

		this.gameConfig = gameConfig;
		this.gameOn = true;
		this.won = false;
		this.level = level;

		this.board = level.getBoard();
		this.hero = level.getHero();
		
		this.board.setBoardAt(hero.position, hero.symbol);
		
		int guardIndex = gameConfig.getGuardIndex();
		
		if(level.isHaveGuard()){
			
			switch (guardIndex) {
			case 0:
				this.guard = new Rookie(level.getLevel());
				break;
			case 1:
				this.guard = new Drunken(level.getLevel());
				break;
			case 2:
				this.guard = new Suspicious(level.getLevel());
				break;

			default:
				randomGuard(level.getLevel());
				break;
			}

			this.board.setBoardAt(guard.position, guard.symbol);

		}
		if(level.isHaveOgre()){
			fillCrazyOgres(level.getLevel(), gameConfig.getNumOfOgres());
			setOgresOnBoard();
			}

	}

	private void applyLever(){
		for(int i =0; i < gameConfig.getrows(); i++){
			for(int j = 0; j < gameConfig.getcolumns(); j++){
				if(this.board.getBoardAt(i, j)=='I'){
					this.board.setBoardAt(i,j, 'S');
				}
			}
}
	}
	

	public void updateGame(Direction move){

		Action action = hero.move(this.board, move);
		
		if(guard != null)
			guard.move(board);
		if(crazyOgres != null)
			moveOgres();
		

		if(action != Action.OPENDOOR){
			if(hero.isSymbolNearby(board, 'G')){
				action = Action.GUARD;
			}
			if(!hero.isArmed){
				if(hero.isSymbolNearby(board, 'O') || hero.isSymbolNearby(board, '$') || hero.isSymbolNearby(board, '*')){
					action = Action.CRAZYOGRE;
				}
			}
			else{
				if(hero.isSymbolNearby(board, '*')){
					action = Action.CRAZYOGRE;
				}
				else if(hero.isOgreNearby(board, crazyOgres)){
					action = Action.STUNNED;
				}
			}
		}

		switch (action) {
		case NOACTION:
			break;
		case GUARD:
			this.won = false;
			gameOn = false;
			break;
		case CRAZYOGRE:
			this.won = false;
			gameOn = false;
			break;
		case OPENDOOR:
			this.won= true;
			gameOn= false;
			break;
		case LEVER:
			applyLever();
		case MOVE:
			break;
		case KEY:
			break;
		case STUNNED:
			break;

		default:
			break;
		}
	}


	public boolean isWon() {
		return won;
	}
	
	public boolean isGameOn() {
		return gameOn;
	}
	
	public void showBoard(){
		board.showBoard();
	}
	
	private void chooseGuard(int level){
		
		System.out.println("Choose Guard: ");
		System.out.println("0 - Rookie");
		System.out.println("1 - Drunken");
		System.out.println("2 - Suspicious");
		
		
		
		Interaction readGuard = new Interaction();
		char keyPressed = readGuard.getKeyPressed();
		
		switch (keyPressed) {
		case '0':
			this.guard = new Rookie(level);
			System.out.println("Rookie Selected");
			break;
		case '1':
			this.guard = new Drunken(level);
			System.out.println("Drunken Selected");
			break;
		case '2':
			this.guard = new Suspicious(level);
			System.out.println("Suspicious Selected");
			break;
		default:
			this.guard = new Rookie(level);
			System.out.println("Invalid Input. Rookie Selected");
		}
	}
	
	private void randomGuard(int level){
		int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);

		switch (randomNum) {
		case 0:
			this.guard = new Rookie(level);
			break;
		case 1:
			this.guard = new Drunken(level);
			break;
		case 2:
			this.guard = new Suspicious(level);
			break;
		default:
			this.guard = new Rookie(level);	
		}
		
	}
	
	private void fillCrazyOgres(int level, int ogresNum){
		CrazyOgre crazyOgre;
		
		this.crazyOgres = new ArrayList<CrazyOgre>();
		switch (level) {
		case -2:
			crazyOgre = new CrazyOgre(new Coord(1,4), true, this.board);
			this.crazyOgres.add(crazyOgre);
			break;
		case -1:
			crazyOgre = new CrazyOgre(new Coord(1,3), false, this.board);
			crazyOgre.setStunned(true);
			this.crazyOgres.add(crazyOgre);
			break;
		case 3:
			crazyOgre = new CrazyOgre(new Coord(1,4), false, this.board);
			this.crazyOgres.add(crazyOgre);
			break;
		case 4:
			crazyOgre = new CrazyOgre(new Coord(1,4), true, this.board);
			this.crazyOgres.add(crazyOgre);
			break;
		case 5:
			randomOgres(ogresNum);
			break;

		default:
			break;
		}
	}
	
	public Board getBoard() {
		return board;
	}


	private void setOgresOnBoard(){
		
		for (int i = 0; i < crazyOgres.size(); i++){
			this.board.setBoardAt(crazyOgres.get(i).position, crazyOgres.get(i).symbol);
			if(crazyOgres.get(i).isArmed){
				this.board.setBoardAt(crazyOgres.get(i).weaponLocation, crazyOgres.get(i).weapon);
			}
				
		}
	}
	
	private void moveOgres(){
		for (int i = 0; i<crazyOgres.size(); i++){
			crazyOgres.get(i).move(board, crazyOgres);
		}
	}
	
	private void chooseOgresNum(){
		System.out.println("Choose Number of Ogres (1-3): ");
		
		
		
		Interaction readNumber = new Interaction();
		int ogresNum = Character.getNumericValue(readNumber.getKeyPressed());
		
		if(ogresNum < 1 || ogresNum > 3){
			System.out.println("Invalid Input. Random number of ogres selected");
			randomOgres(0);
		}
		else{
			System.out.println(ogresNum + " ogres selected");
			randomOgres(ogresNum);
		}
		

	}
	
	private void randomOgres(int ogresNum){
		
		
		if(ogresNum == 0)
			ogresNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		
		Coord pos = new Coord(-1,-1);
		HashSet<Coord> temp = new HashSet<Coord>();
		
		for(int i = 0; i < ogresNum; i++){

			pos = randomPos();
			
			if(!validPos(pos) || temp.contains(pos))
				i--;
			else{
				temp.add(pos);
				this.crazyOgres.add(new CrazyOgre(pos, true, this.board));
			}
		}
		
	}
	
	private boolean validPos(Coord position){
		if(isSymbolNearby(position, this.hero.symbol))
			return false;
		if(this.board.getBoardAt(position.getX(), position.getY()) == ' ')
			return true; 
		return false;
	}

	private Coord randomPos(){
		int x, y;
		
		x = ThreadLocalRandom.current().nextInt(0, this.board.getRows() + 1);
		y = ThreadLocalRandom.current().nextInt(0, this.board.getColumns() + 1);
		
		return new Coord(x,y);
		
	}
	
	public boolean isSymbolNearby(Coord position, char symbol){

		int xPos = position.getX();
		int yPos = position.getY();

		if(board.getBoardAt(xPos+1, yPos)==symbol)
			return true;

		if(board.getBoardAt(xPos-1, yPos)==symbol)
			return true;

		if(board.getBoardAt(xPos, yPos+1)==symbol)
			return true;

		if(board.getBoardAt(xPos, yPos-1)==symbol)
			return true;

		if(board.getBoardAt(xPos, yPos) == symbol)
			return true;
		return false;
	}


	public Level getLevel() {
		return level;
	}
	
	
	
	
}