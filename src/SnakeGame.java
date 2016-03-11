
/**
 * Segundo examen parcial: Snake
 * A01280927 - A01280734
 *
 * Descripción...
 *
 * @author Jorge Gonzalez Borboa (A01280927) - Jorge Limón Cabrera (A01280734)
 * @version 1.0
 * @date 11/Marzo/2016
 */

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * The {@code SnakeGame} class is responsible for handling much of the game's
 * logic.
 *
 */
public class SnakeGame extends JFrame {
		
    /**
     * The Serial Version UID.
     */
    private static final long lSerialVersionUID = 6678292058307426314L;

    /**
     * The number of milliseconds that should pass between each frame.
     */
    private static final long lFRAME_TIME = 1000L / 50L;
	
    /**
     * The minimum length of the lklSnake. This allows the lklSnake to grow
     * right when the game starts, so that we're not just a head moving
     * around on the bpBoard.
     */
    private static final int iMIN_SNAKE_LENGTH = 5;
	
    /**
     * The maximum number of lklDirections that we can have polled in the
     * direction list.
     */
    private static final int iMAX_DIRECTIONS = 3;
	
    /**
     * The BoardPanel instance.
     */
    private BoardPanel bpBoard;
	
    /**
     * The SidePanel instance.
     */
    private SidePanel spSide;
	
    /**
     * The rRandom number generator (used for spawning fruits).
     */
    private Random rRandom;
	
    /**
     * The Clock instance for handling the game logic.
     */
    private Clock clkLogicTimer;
	
    /**
     * Whether or not we're running a new game.
     */
    private boolean bIsNewGame;
		
    /**
     * Whether or not the game is over.
     */
    private boolean bIsGameOver;
	
    /**	
     * Whether or not the game is paused.
     */
    private boolean bIsPaused;
	
    /**
     * The list that contains the points for the lklSnake.
     */
    private LinkedList<Point> lklSnake;
	
    /**
     * The list that contains the queued lklDirections.
     */
    private LinkedList<Direction> lklDirections;
	
    /**
     * The current iScore.
     */
    private int iScore;
	
    /**
     * The number of fruits that we've eaten.
     */
    private int iFruitsEaten;
	
    /**
     * The number of points that the next fruit will award us.
     */
    private int iNextFruitScore;
    
    //Variable used to control how much the snake grows, depending on what fruit
    //He gets
    private int iSizeController =0 ;
    
	
    /**
     * Creates a new SnakeGame instance. Creates a new window,
     * and sets up the controller input.
     */
    private SnakeGame() {
	super("Snake Remake");
	setLayout(new BorderLayout());
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
				
	/*
	 * Initialize the game's panels and add them to the window.
	 */
	this.bpBoard = new BoardPanel(this);
	this.spSide = new SidePanel(this);
	
	add(bpBoard, BorderLayout.CENTER);
	add(spSide, BorderLayout.EAST);
		
	/*
	 * Adds a new key listener to the frame to process input. 
	 */
	addKeyListener(new KeyAdapter() {
			
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {

                    /*
                     * If the game is not paused, and the game is not over...
                     * 
                     * Ensure that the direction list is not full, and that the
                     * most recent direction is adjacent to North before adding
                     * the direction to the list.
                     */
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if(!bIsPaused && !bIsGameOver) {
                            if(lklDirections.size() < iMAX_DIRECTIONS) {
				Direction last = lklDirections.peekLast();
				if(last != Direction.South &&
                                   last != Direction.North) {
                                    lklDirections.addLast(Direction.North);
				}
                            }
			}
			break;

                    /*
                     * If the game is not paused, and the game is not over...
                     * 
                     * Ensure that the direction list is not full, and that the
                     * most recent direction is adjacent to South before adding 
                     * the direction to the list.
                     */	
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
			if(!bIsPaused && !bIsGameOver) {
                            if(lklDirections.size() < iMAX_DIRECTIONS) {
				Direction last = lklDirections.peekLast();
				if(last != Direction.North &&
                                   last != Direction.South) {
                                    lklDirections.addLast(Direction.South);
				}
                            }
			}
                    break;
				
                    /*
                     * If the game is not paused, and the game is not over...
                     * 
                     * Ensure that the direction list is not full, and that the 
                     * most recent direction is adjacent to West before adding
                     * the direction to the list.
                     */						
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if(!bIsPaused && !bIsGameOver) {
                            if(lklDirections.size() < iMAX_DIRECTIONS) {
				Direction last = lklDirections.peekLast();
				if(last != Direction.East &&
                                   last != Direction.West) {
                                    lklDirections.addLast(Direction.West);
				}
                            }
			}
                    break;
			
                    /*
                     * If the game is not paused, and the game is not over...
                     * 
                     * Ensure that the direction list is not full, and that the 
                     * most recent direction is adjacent to East before adding
                     * the direction to the list.
                     */		
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
			if(!bIsPaused && !bIsGameOver) {
                            if(lklDirections.size() < iMAX_DIRECTIONS) {
				Direction last = lklDirections.peekLast();
				if(last != Direction.West &&
                                   last != Direction.East) {
                                    lklDirections.addLast(Direction.East);
				}
                            }
			}
                    break;
				
                    /*
                     * If the game is not over, toggle the paused flag and
                     * update the clkLogicTimer's pause flag accordingly.
                     */
                    case KeyEvent.VK_P:
			if(!bIsGameOver) {
                            bIsPaused = !bIsPaused;
                            clkLogicTimer.setPaused(bIsPaused);
			}
                    break;
				
                    /*
                     * Reset the game if one is not currently in progress.
                     */
                    case KeyEvent.VK_ENTER:
                    	if(bIsNewGame || bIsGameOver) {
                            resetGame();
			}
                    break;
		}
            }
			
	});
		
	/*
	 * Resize the window to the appropriate size, center it on the
	 * screen and display it.
	 */
	pack();
	setLocationRelativeTo(null);
	setVisible(true);
    }
	
    /**
     * Starts the game running.
     */
    private void startGame() {
	/*
	 * Initialize everything we're going to be using.
	 */
	this.rRandom = new Random();
	this.lklSnake = new LinkedList<>();
	this.lklDirections = new LinkedList<>();
	this.clkLogicTimer = new Clock(9.0f);
	this.bIsNewGame = true;
		
	//Set the timer to paused initially.
	clkLogicTimer.setPaused(true);

	/*
	 * This is the game loop. It will update and render the game and will
	 * continue to run until the game window is closed.
	 */
	while(true) {
            //Get the current frame's start time.
            long start = System.nanoTime();
			
            //Update the logic timer.
            clkLogicTimer.update();
			
            /*
             * If a cycle has elapsed on the logic timer, then update the game.
             */
            if(clkLogicTimer.hasElapsedCycle()) {
		updateGame();
            }
			
            //Repaint the bpBoard and spSide panel with the new content.
            bpBoard.repaint();
            spSide.repaint();
			
            /*
             * Calculate the delta time between since the start of the frame
             * and sleep for the excess time to cap the frame rate. While not
             * incredibly accurate, it is sufficient for our purposes.
             */
            long delta = (System.nanoTime() - start) / 1000000L;
            if(delta < lFRAME_TIME) {
		try {
                    Thread.sleep(lFRAME_TIME - delta);
		}
                catch(Exception e) {
                    e.printStackTrace();
		}
            }
	}
    }
	
    /**
     * Updates the game's logic.
     */
    private void updateGame() {
     	/*
	 * Gets the type of tile that the head of the lklSnake collided with. If 
	 * the lklSnake hit a wall, SnakeBody will be returned, as both
	 * conditions are handled identically.
	 */
	TileType collision = updateSnake();
		
	/*
	 * Here we handle the different possible collisions.
	 * 
	 * Fruit: If we collided with a fruit, we increment the number of
	 * fruits that we've eaten, update the iScore, and spawn a new fruit.
	 * 
	 * SnakeBody: If we collided with our tail (or a wall), we flag that
	 * the game is over and pause the game.
	 * 
	 * If no collision occurred, we simply decrement the number of points
	 * that the next fruit will give us if it's high enough. This adds a
	 * bit of skill to the game as collecting fruits more quickly will
	 * yield a higher iScore.
	 */
	if(collision == TileType.Fruit1) {
            iFruitsEaten++;
            iScore += iNextFruitScore;
            spawnFruit1();
	}
        else if (collision == TileType.Fruit2) {
            
            iFruitsEaten++;
            iScore += iNextFruitScore;
            spawnFruit2();
        }
        else if (collision == TileType.Fruit3) {
            
            iFruitsEaten++;
            iScore += iNextFruitScore;
            spawnFruit3();
        }
        else if(collision==TileType.BadFruit){
            bIsGameOver=true;
            clkLogicTimer.setPaused(true);
        }
        else if(collision == TileType.SnakeBody) {
            bIsGameOver = true;
            clkLogicTimer.setPaused(true);
	}
        else if(iNextFruitScore > 10) {
            iNextFruitScore--;
	}
    }
	
    /**
     * Updates the lklSnake's position and size.
     * @return Tile tile that the head moved into.
     */
    private TileType updateSnake() {

	/*
	 * Here we peek at the next direction rather than polling it. While
	 * not game breaking, polling the direction here causes a small bug
	 * where the lklSnake's direction will change after a game over (though
	 * it will not move).
	 */
	Direction direction = lklDirections.peekFirst();
				
	/*
	 * Here we calculate the new point that the lklSnake's head will be at
	 * after the update.
	 */		
	Point head = new Point(lklSnake.peekFirst());
        switch(direction) {
            case North:
                head.y--;
                break;
			
            case South:
                head.y++;
                break;
			
            case West:
                head.x--;
                break;
			
            case East:
                head.x++;
                break;
        }
		
        /*
         * If the lklSnake has moved out of bounds ('hit' a wall), we can just
         * return that it's collided with itself, as both cases are handled
         * identically.
         */
        if(head.x < 0 || head.x >= BoardPanel.iCOL_COUNT || head.y < 0 ||
           head.y >= BoardPanel.iROW_COUNT) {
            return TileType.SnakeBody; //Pretend we collided with our body.
        }
		
        /*
         * Here we get the tile that was located at the new head position and
         * remove the tail from of the lklSnake and the bpBoard if the lklSnake
         * is long enough, and the tile it moved onto is not a fruit.
         * 
         * If the tail was removed, we need to retrieve the old tile again
         * incase the tile we hit was the tail piece that was just removed
         * to prevent a false game over.
         */
        TileType old = bpBoard.getTile(head.x, head.y);
        
        if(old == TileType.Fruit2){
            iSizeController+=2;
        }
        else if (old == TileType.Fruit3){
            
            iSizeController+=3;
        }
        
        if(old != TileType.Fruit1 && old != TileType.Fruit2 
                && old != TileType.Fruit3 
                && lklSnake.size() > iMIN_SNAKE_LENGTH && iSizeController==0) {
            
            Point tail = lklSnake.removeLast();
            bpBoard.setTile(tail, null);
            old = bpBoard.getTile(head.x, head.y);
        }
        
        if(iSizeController!=0){
            iSizeController--;
        }
        
		
        /*
         * Update the lklSnake's position on the bpBoard if we didn't collide
         * with our tail:
         * 
         * 1. Set the old head position to a body tile.
         * 2. Add the new head to the lklSnake.
         * 3. Set the new head position to a head tile.
         * 
         * If more than one direction is in the queue, poll it to read new
         * input.
         */
        if(old != TileType.SnakeBody) {
            bpBoard.setTile(lklSnake.peekFirst(), TileType.SnakeBody);
            lklSnake.push(head);
            bpBoard.setTile(head, TileType.SnakeHead);
            if(lklDirections.size() > 1) {
                lklDirections.poll();
            }
        }
				
        return old;
    }
	
    /**
     * Resets the game's variables to their default states and starts a new
     * game.
     */
    private void resetGame() {
	/*
	 * Reset the iScore statistics. (Note that nextFruitPoints is reset in
	 * the spawnFruit function later on).
	 */
	this.iScore = 0;
	this.iFruitsEaten = 0;
	
	/*
	 * Reset both the new game and game over flags.
	 */
	this.bIsNewGame = false;
	this.bIsGameOver = false;
		
	/*
	 * Create the head at the center of the bpBoard.
	 */
	Point head = new Point(BoardPanel.iCOL_COUNT / 2,
                               BoardPanel.iROW_COUNT / 2);

	/*
	 * Clear the lklSnake list and add the head.
	 */
	lklSnake.clear();
	lklSnake.add(head);
		
	/*
	 * Clear the bpBoard and add the head.
	 */
	bpBoard.clearBoard();
	bpBoard.setTile(head, TileType.SnakeHead);
		
	/*
	 * Clear the lklDirections and add north as the
	 * default direction.
	 */
	lklDirections.clear();
	lklDirections.add(Direction.North);
		
	/*
	 * Reset the logic timer.
	 */
	clkLogicTimer.reset();
		
	/*
	 * Spawn a new fruit.
	 */
        for(int iI = 1 ; iI<=4 ; iI++) {
            
            switch (iI) {
                case 1:
                    spawnFruit1();
                    break;
                case 2:
                    spawnFruit2();
                    break;
                case 3:
                    spawnFruit3();
                    break;
                case 4:
                    spawnFruit4 ();
                default: 
                    break;
            }
            
        }
	
    }
	
    /**
     * Gets the flag that indicates whether or not we're playing a new game.
     * @return The new game flag.
     */
    public boolean bIsNewGame() {
    	return bIsNewGame;
    }
	
    /**
     * Gets the flag that indicates whether or not the game is over.
     * @return The game over flag.
     */
    public boolean bIsGameOver() {
    	return bIsGameOver;
    }
	
    /**
     * Gets the flag that indicates whether or not the game is paused.
     * @return The paused flag.
     */
    public boolean bIsPaused() {
    	return bIsPaused;
    }
	
    /**
     * Spawns a new fruit onto the bpBoard.
     */
    private void spawnFruit1() {
	//Reset the iScore for this fruit to 100.
	this.iNextFruitScore = 100;

	/*
	 * Get a rRandom index based on the number of free spaces left on the
         * bpBoard.
	 */
	int index = rRandom.nextInt(BoardPanel.iCOL_COUNT *
                BoardPanel.iROW_COUNT - lklSnake.size());
		
	/*
	 * While we could just as easily choose a rRandom index on the bpBoard
	 * and check it if it's free until we find an empty one, that method
	 * tends to hang if the lklSnake becomes very large.
	 * 
	 * This method simply loops through until it finds the nth free index
	 * and selects uses that. This means that the game will be able to
	 * locate an index at a relatively constant rate regardless of the
	 * size of the lklSnake.
	 */
	int freeFound = -1;
	for(int x = 0; x < BoardPanel.iCOL_COUNT; x++) {
            for(int y = 0; y < BoardPanel.iROW_COUNT; y++) {
		TileType type = bpBoard.getTile(x, y);
		if(type == null || type == TileType.Fruit1|| type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {
                        
                            //int RandomFruit = (int) (Math.random() * 3) + 1;
                            
                            
                                
                                bpBoard.setTile(x, y, TileType.Fruit1);
                            
                            
                        
			break;
                    }
		}
            }
	}
    }
    
    private void spawnFruit2() {
	//Reset the iScore for this fruit to 100.
	this.iNextFruitScore = 100;

	/*
	 * Get a rRandom index based on the number of free spaces left on the
         * bpBoard.
	 */
	int index = rRandom.nextInt(BoardPanel.iCOL_COUNT *
                BoardPanel.iROW_COUNT - lklSnake.size());
		
	/*
	 * While we could just as easily choose a rRandom index on the bpBoard
	 * and check it if it's free until we find an empty one, that method
	 * tends to hang if the lklSnake becomes very large.
	 * 
	 * This method simply loops through until it finds the nth free index
	 * and selects uses that. This means that the game will be able to
	 * locate an index at a relatively constant rate regardless of the
	 * size of the lklSnake.
	 */
	int freeFound = -1;
	for(int x = 0; x < BoardPanel.iCOL_COUNT; x++) {
            for(int y = 0; y < BoardPanel.iROW_COUNT; y++) {
		TileType type = bpBoard.getTile(x, y);
		if(type == null || type == TileType.Fruit1|| type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {
                        
                            //int RandomFruit = (int) (Math.random() * 3) + 1;
                            
                           
                                
                                bpBoard.setTile(x, y, TileType.Fruit2);
                                
                            
                            
                        
			break;
                    }
		}
            }
	}
    }
    
    private void spawnFruit3() {
	//Reset the iScore for this fruit to 100.
	this.iNextFruitScore = 100;

	/*
	 * Get a rRandom index based on the number of free spaces left on the
         * bpBoard.
	 */
	int index = rRandom.nextInt(BoardPanel.iCOL_COUNT *
                BoardPanel.iROW_COUNT - lklSnake.size());
		
	/*
	 * While we could just as easily choose a rRandom index on the bpBoard
	 * and check it if it's free until we find an empty one, that method
	 * tends to hang if the lklSnake becomes very large.
	 * 
	 * This method simply loops through until it finds the nth free index
	 * and selects uses that. This means that the game will be able to
	 * locate an index at a relatively constant rate regardless of the
	 * size of the lklSnake.
	 */
	int freeFound = -1;
	for(int x = 0; x < BoardPanel.iCOL_COUNT; x++) {
            for(int y = 0; y < BoardPanel.iROW_COUNT; y++) {
		TileType type = bpBoard.getTile(x, y);
		if(type == null || type == TileType.Fruit1|| type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {
                        
                            //int RandomFruit = (int) (Math.random() * 3) + 1;
                            
                            
                                
                                bpBoard.setTile(x, y, TileType.Fruit3);
                                
                            
                            
                        
			break;
                    }
		}
            }
	}
    }
    
    private void spawnFruit4() {
	//Reset the iScore for this fruit to 100.
	this.iNextFruitScore = 100;

	/*
	 * Get a rRandom index based on the number of free spaces left on the
         * bpBoard.
	 */
	int index = rRandom.nextInt(BoardPanel.iCOL_COUNT *
                BoardPanel.iROW_COUNT - lklSnake.size());
		
	/*
	 * While we could just as easily choose a rRandom index on the bpBoard
	 * and check it if it's free until we find an empty one, that method
	 * tends to hang if the lklSnake becomes very large.
	 * 
	 * This method simply loops through until it finds the nth free index
	 * and selects uses that. This means that the game will be able to
	 * locate an index at a relatively constant rate regardless of the
	 * size of the lklSnake.
	 */
	int freeFound = -1;
	for(int x = 0; x < BoardPanel.iCOL_COUNT; x++) {
            for(int y = 0; y < BoardPanel.iROW_COUNT; y++) {
		TileType type = bpBoard.getTile(x, y);
		if(type == null || type == TileType.Fruit1|| type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {
                        
                            //int RandomFruit = (int) (Math.random() * 3) + 1;
                            
                            
                                
                                bpBoard.setTile(x, y, TileType.BadFruit);
                                
                            
                            
                        
			break;
                    }
		}
            }
	}
    }
    
    /**
     * Gets the current iScore.
     * @return The iScore.
     */
    public int getScore() {
    	return iScore;
    }
	
    /**
     * Gets the number of fruits eaten.
     * @return The fruits eaten.
     */
    public int getFruitsEaten() {
    	return iFruitsEaten;
    }
	
    /**
     * Gets the next fruit iScore.
     * @return The next fruit iScore.
     */
    public int getNextFruitScore() {
    	return iNextFruitScore;
    }
	
    /**
     * Gets the current direction of the lklSnake.
     * @return The current direction.
     */
    public Direction getDirection() {
    	return lklDirections.peek();
    }
	
    /**
     * Entry point of the program.
     * @param args Unused.
     */
    public static void main(String[] args) {
	SnakeGame lklSnake = new SnakeGame();
	lklSnake.startGame();
    }
}