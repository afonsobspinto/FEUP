package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import gameLogic.CrazyOgre;
import gameLogic.Direction;
import gameLogic.GameConfig;
import gameLogic.GameLogic;
import gameLogic.Hero;
import gameLogic.Level;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int numberOfLevels = 5;

	private boolean showBackground = true;
	
	private Image background;
	private Image wall;
	private Image door;
	private Image openDoor;
	private Image hero;
	private Image heroWithWeapon;
	private Image heroWithKey;
	private Image guard;
	private Image guardSleeping;
	private Image ogre;
	private Image ogreSleeping;
	private Image club;
	private Image key;
	private Image lever;
	private Image leverActivated;
	
	private GameLogic game;
	private GameConfig gameConfig;
	
	private int charactersWidth;
	private int charactersHeight;
	private int level;
	
	public GamePanel() {
		addKeyListener(new MyKeyAdapter());
		setFocusable(true);
		loadImages();
	}
	
	
	private void loadImages(){
		ImageIcon temp;
		temp = new ImageIcon(this.getClass().getResource("res/background.png"));
		background = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/wall.png"));
		wall = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/door.png"));
		door = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/openDoor.png"));
		openDoor = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/hero.png"));
		hero = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/heroWithWeapon.png"));
		heroWithWeapon = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/heroWithKey.png"));
		heroWithKey = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/guard.png"));
		guard = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/guardSleeping.png"));
		guardSleeping = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/ogre.png"));
		ogre = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/ogreSleeping.png"));
		ogreSleeping = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/club.png"));
		club = temp.getImage();

		temp = new ImageIcon(this.getClass().getResource("res/key.png"));
		key = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/lever1.png"));
		lever = temp.getImage();
		
		temp = new ImageIcon(this.getClass().getResource("res/lever2.png"));
		leverActivated = temp.getImage();

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		
		if(showBackground){
		g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), 0, 0, 
				background.getWidth(null), background.getHeight(null), null);
		}
		else{
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, getWidth(), getHeight());
			drawGame(g2d);
		}
			
			
		
	}
	
	
	public void drawGame(Graphics g2d){

		drawHero(g2d);
		drawOgres(g2d);
		
		
		for(int i = 0; i < game.getBoard().getRows(); i++){
			for(int j = 0; j < game.getBoard().getColumns(); j++){
				
				if(game.getBoard().getBoardAt(i, j) == 'X')
					drawCharacter(wall, g2d, i,j);	
				
				else if(game.getBoard().getBoardAt(i, j) == 'I')
					drawCharacter(door, g2d, i,j);	
				

				
				else if(game.getBoard().getBoardAt(i, j) == 'S')
					drawCharacter(openDoor, g2d, i,j);	
				
				else if (game.getBoard().getBoardAt(i, j) == 'k' && game.getLevel().isHaveLever())
					drawCharacter(lever, g2d, i,j); //Adicionar LeverIsActivated
					
				else if (game.getBoard().getBoardAt(i, j) == 'k') //In this case is key
					drawCharacter(key, g2d, i,j);
				
				else if(game.getBoard().getBoardAt(i, j) == 'G')
					drawCharacter(guard, g2d, i,j);
				
				else if(game.getBoard().getBoardAt(i, j) == 'g')
					drawCharacter(guardSleeping, g2d, i,j);
			}
		}
	}
	
	private void drawCharacter(Image img, Graphics g2d, int i, int j){ //Adicionar Orientação - Check Last Move 
		int distX = j * charactersWidth;
		int distY = i * charactersHeight;
		
		distX += (getWidth() - charactersWidth * game.getBoard().getColumns()) / 2.0;
		distY += (getHeight() - charactersHeight * game.getBoard().getRows()) / 2.0;

		g2d.drawImage(img, distX, distY, distX + charactersWidth, distY + charactersHeight, 0,
				0, img.getWidth(null), img.getHeight(null), null);
		
	}
	
	private void drawHero(Graphics g2d){
		
		Hero herocp = game.getHero();
		Direction orientation = herocp.getOrientation();
		
		int distX = herocp.getPosition().getY() * charactersWidth;
		int distY = herocp.getPosition().getX() * charactersHeight;
		
		distX += (getWidth() - charactersWidth * game.getBoard().getColumns()) / 2.0;
		distY += (getHeight() - charactersHeight * game.getBoard().getRows()) / 2.0;
		
		double locationX = hero.getWidth(null) / 2;
		double locationY = hero.getHeight(null) / 2;
		
		double rotationRequired;
		
		switch (orientation) {
		case RIGHT:
			rotationRequired = Math.toRadians(0);
			break;
		case LEFT:
			rotationRequired = Math.toRadians(180);
			break;
		case UP:
			rotationRequired = Math.toRadians(270);
			break;
		case DOWN:
			rotationRequired = Math.toRadians(90);
			break;
		default:
			rotationRequired = 0;
			break;
		}
		
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	
		
		BufferedImage bimage;
		
		if(herocp.isGotKey())
			bimage = toBufferedImage(heroWithKey);
		else if(herocp.isArmed())
			bimage = toBufferedImage(heroWithWeapon);
		else
			bimage = toBufferedImage(hero);
		
		
		g2d.drawImage(op.filter(bimage, null), distX, distY, distX + charactersWidth, distY + charactersHeight, 0,
				0, bimage.getWidth(null), bimage.getHeight(null), null);
	}
	
	private void drawOgres(Graphics g2d){
		
		ArrayList<CrazyOgre> temp = game.getCrazyOgres();

		if(temp!=null){

			for(int i = 0; i < temp.size(); i++){
				CrazyOgre temp_ogre = temp.get(i);
				if(temp_ogre.isStunned())
					drawCharacter(ogreSleeping, g2d, temp_ogre.getPosition().getX(),temp_ogre.getPosition().getY());
				else
					drawCharacter(ogre, g2d, temp_ogre.getPosition().getX(),temp_ogre.getPosition().getY());
				if(temp_ogre.isArmed())
					drawCharacter(club, g2d, temp_ogre.getWeaponLocation().getX(),temp_ogre.getWeaponLocation().getY());
			}
		}
	}
	
	public void startNewGame(GameConfig gameConfig, int level) {
		this.game = new GameLogic(new Level(level), gameConfig);
		this.gameConfig = gameConfig;
		this.level = level;
		showBackground = false;
		charactersHeight = this.getHeight() / gameConfig.getrows();
		charactersWidth = this.getWidth() / gameConfig.getcolumns();
		repaint();
		requestFocus();
	}


	public boolean isShowBackground() {
		return showBackground;
	}


	public void setShowBackground(boolean showBackground) {
		this.showBackground = showBackground;
	}
	
	private class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (showBackground)
				return;

			char key = e.getKeyChar();
			
			final char downKey = gameConfig.getDownKey();
			final char upKey = gameConfig.getUpKey();
			final char rigthKey = gameConfig.getRightKey();
			final char leftKey = gameConfig.getLeftKey();
			
			Direction direction;

			if (key == downKey) {
				direction = Direction.DOWN;
				
			} else if (key == upKey) {
				direction = Direction.UP;
				
			} else if (key == rigthKey) {
				direction = Direction.RIGHT;
				
			} else if (key == leftKey) {
				direction = Direction.LEFT;

			} else {
				direction = Direction.INVALID;
			}

			game.updateGame(direction);

			if(!game.isGameOn() && game.isWon()){
				String msg = "You win!";
				JOptionPane.showMessageDialog(getRootPane(), msg);
				if(++level<=numberOfLevels)
					startNewGame(gameConfig, level);
				else
					showBackground = true;
					
			}
			else if(!game.isGameOn() && !game.isWon()){
				String msg = "Game Over!";
				JOptionPane.showMessageDialog(getRootPane(), msg);
				startNewGame(gameConfig, level);
			}

			repaint();
		}
	}


	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	private static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
