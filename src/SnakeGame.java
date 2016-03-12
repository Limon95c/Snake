
/**
 * Segundo examen parcial: Snake
 * A01280927 - A01280734
 *
 * El clasico juego de "Snake" pero con un tema espacial en el que la vibora es
 * formada por un cohete y el fuego que deja detrás. El cohete debe recolectar
 * bateria, comida y combustible en el espacio y evitar chocar con la tierra.
 *
 * @author Jorge Gonzalez Borboa (A01280927) - Jorge Limón Cabrera (A01280734)
 * @version 1.0
 * @date 11/Marzo/2016
 */

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JOptionPane;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Objeto para escibir en archivo.
     */
    private PrintWriter fileOut;
    
    /**
     * Objeto para leer en archivo.
     */
    private BufferedReader fileIn;

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
    
    //The Audio used in the game
    
    //GameOver SoundClip
    private SoundClip auGameOver; 
    //Catching a good item SoundClip
    private SoundClip auGrab;
    //Background Song 
    private SoundClip auSong;
    
	
    /**
     * Creates a new SnakeGame instance. Creates a new window,
     * and sets up the controller input.
     */
    private SnakeGame() {
	super("Rocket Snake");
        //this.<error> = new SoundClip ("GameOver.wav");
	setLayout(new BorderLayout());
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
        
        //Assign the Corresponding SoundClip to our Sound Data
        auGameOver = new SoundClip ("GameOver.wav");
        auGrab = new SoundClip ("Grab.wav");
        auSong = new SoundClip ("Song.wav");
				
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
                            
                            //If the Game is Getting Paused
                            if(!bIsPaused){
                                
                                //Stop the Song
                                auSong.stop();
                            }
                            //If the Game is Resuming
                            else {
                                
                                //Play the Song
                                auSong.play();
                            }
                            
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
                    
                    /*
                     * Pauses the game to save it.
                     */
                    case KeyEvent.VK_G:
                        if(!bIsGameOver && !bIsNewGame) {
                            try {
                                // Set everything to pause and saveGame()
                                clkLogicTimer.setPaused(true);
                                bIsPaused = true;
                                
                                //Stop the Song when Saving
                                auSong.stop();
                                
                                saveGame();
                            }
                            catch (IOException ex) {
                                Logger.getLogger(SnakeGame.class.getName()).log(
                                                 Level.SEVERE, null, ex);
                            }
                        }
                        break;
                        
                    /*
                     * Load Game - When pressed, check to see that
                     * we're not in a game over. If we're not,
                     * load the game.
                     */
                    case KeyEvent.VK_C:
                        if(!bIsGameOver && !bIsNewGame) {
                            try {
                                // Set everything to pause and loadGame()
                                clkLogicTimer.setPaused(true);
                                bIsPaused = true;
                                
                                //Stop the Song when Loading
                                auSong.stop();
                                
                                loadGame();
                            }
                            catch (IOException ex) {
                                Logger.getLogger(SnakeGame.class.getName()).log(
                                                    Level.SEVERE, null, ex);
                            }
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
        
        //Set the Song to Loop
        auSong.setLooping(true);
        
        
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
            auGrab.play();
            spawnFruit1();
	}
        else if (collision == TileType.Fruit2) {
            
            iFruitsEaten++;
            iScore += iNextFruitScore;
            auGrab.play();
            spawnFruit2();
        }
        else if (collision == TileType.Fruit3) {
            
            iFruitsEaten++;
            iScore += iNextFruitScore;
            auGrab.play();
            spawnFruit3();
        }
        else if(collision==TileType.BadFruit){
            bIsGameOver=true;
            clkLogicTimer.setPaused(true);
            auGameOver.play();
            auSong.stop();
        }
        else if(collision == TileType.SnakeBody) {
            bIsGameOver = true;
            clkLogicTimer.setPaused(true);
            auGameOver.play();
            auSong.stop();
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
        
        //Start the song Again
        auSong.play();
		
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
        for(int iI = 1 ; iI <= 4 ; iI++) {
            
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
     * saveGame
     * 
     * Saves the game based on a name.
     */
    public void saveGame() throws IOException {
            
        // Pregunta el nombre del usuario para guardar
        String nombre = JOptionPane.showInputDialog("Cual es tu nombre?");
        // Poner todo en minusculas y agregar .txt
        nombre = nombre.toLowerCase() + ".txt";
            
        // Abrir archivo
        fileOut = new PrintWriter(new FileWriter(nombre));
        fileOut.println(Integer.toString(this.iScore)); // Guardar score
        // Guardar el score al agarrar el siguiente objeto bueno
        fileOut.println(Integer.toString(this.iNextFruitScore));
        // Guardar el numero de frutas comidas
        fileOut.println(Integer.toString(this.iFruitsEaten));
            
        // Guardar longitud de lklDirections
        fileOut.println(Integer.toString(lklDirections.size()));
            
        // Guardar todas las direcciones
        for(int iI = 0; iI < lklDirections.size(); iI++) {
            fileOut.println(Integer.toString(lklDirections.get(iI).ordinal()));
        }
            
        // Guardar longitud de lklSnake
        fileOut.println(Integer.toString(lklSnake.size()));
        
        saveBoard(fileOut);
        
        fileOut.close();
    }
        
    /**
     * saveBoard
     * 
     * Saves the board and it's a continuation from saveGame()
     * 
     * @param fileOut objeto de tipo PrintWriter que mantiene el flujo de datos
     * entre un archivo y el programa
     */
    public void saveBoard(PrintWriter fileOut) throws IOException {
        // Guardar todo el lklSnake
        for(int iI = 0; iI < lklSnake.size(); iI++) {
            // Guardar X
            fileOut.println(Integer.toString(lklSnake.get(iI).x));
            // Guardar Y
            fileOut.println(Integer.toString(lklSnake.get(iI).y));
        }
            
        // Guardar board
        for(int iI = 0; iI < bpBoard.iROW_COUNT ; iI++) {
            for(int iJ = 0; iJ < bpBoard.iCOL_COUNT; iJ++) {
                if(bpBoard.getTile(iJ, iI) != null) {
                    // Guardar X
                    fileOut.println(Integer.toString(iI));
                    // Guardar Y
                    fileOut.println(Integer.toString(iJ));
                    // Guardar tipo de tile
                    fileOut.println(Integer.toString(
                                        bpBoard.getTile(iJ, iI).ordinal()));
                }
            }
        }
    }
    
    /**
     * loadGame
     * 
     * Loads the game based on a name.
     */
    public void loadGame() throws IOException {
        // Pregunta el nombre del usuario para guardar
        String nombre = JOptionPane.showInputDialog("Cual es tu nombre?");
        // Poner todo en minusculas y agregar .txt
        nombre = nombre.toLowerCase();
            
        try {
            // Abrir archivo
            fileIn = new BufferedReader(new FileReader(nombre + ".txt"));
            // Cargar iScore
            iScore = Integer.parseInt(fileIn.readLine());
            // Cargar iNextFruitScore
            iNextFruitScore = Integer.parseInt(fileIn.readLine());
            // Cargar iFruitsEaten
            iFruitsEaten = Integer.parseInt(fileIn.readLine());
            
            // Vaciar la lista de direcciones actuales
            lklDirections.clear();
            
            // Recibir cantidad de direcciones nuevas y agregarlas
            for(int iX = Integer.parseInt(fileIn.readLine()); iX > 0; iX--) {
                lklDirections.add(Direction.values()
                                      [Integer.parseInt(fileIn.readLine())]);
            }
            
            // Vaciar la lista de lklSnake actual
            lklSnake.clear();
            
            // Recibir cantidad de nodos de lklSnake nuevos y agregarlas
            for(int iX = Integer.parseInt(fileIn.readLine()); iX > 0; iX--){
                // Carga X
                int iRow = Integer.parseInt(fileIn.readLine());
                // Carga Y
                int iCol = Integer.parseInt(fileIn.readLine());
                // Añadir
                lklSnake.add(new Point(iRow, iCol));
            }
            
            loadSnakeDirectionsAndBoard(fileIn);
            
            // Cerrar el archivo
            fileIn.close();
        }
        catch (FileNotFoundException ex){
        // Si no se encuentra archivo guardado se debe avisar al usuario
        // que no hay datos guardados con ese nombre
        JOptionPane.showMessageDialog(null, "No existen datos de un juego " +
                "guardado con el nombre de: " + nombre, "Error",
                JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    /**
     * loadSnakeDirectionsAndBoard
     * 
     * Loads the actual board and the snake and it's a continuation from
     * loadGame()
     * 
     * @param fileIn objeto de tipo BufferedReader que mantiene el flujo
     * de datos entre un archivo y el programa
     */
    public void loadSnakeDirectionsAndBoard(BufferedReader fileIn)
                                                        throws IOException {
        // Poner todos los tiles del board como nulo
        for(int iI = 0; iI < bpBoard.iROW_COUNT ; iI++) {
            for(int iJ = 0; iJ < bpBoard.iCOL_COUNT; iJ++) {
                bpBoard.nullTile(iJ, iI);
            }
        }
            
        // Cargar longitud de lklDirections
        fileOut.println(Integer.toString(lklDirections.size()));
            
        // Cargar todas las direcciones como su numero ordinal del archivo
        for(int iI = 0; iI < lklDirections.size(); iI++) {
            fileOut.println(Integer.toString(lklDirections.get(iI).ordinal()));
        }
            
        // Cargar longitud de lklSnake
        fileOut.println(Integer.toString(lklSnake.size()));
            
        // Cargar todo el snake
        for(int iI = 0; iI < lklSnake.size(); iI++) {
            // Carga X
            fileOut.println(Integer.toString(lklSnake.get(iI).x));
            // Carga Y
            fileOut.println(Integer.toString(lklSnake.get(iI).y));
        }
            
        // Lee la siguiente linea
        String sScan = fileIn.readLine();
        // Mientras no se acabe el archivo...
        while(sScan != null) { // Cargar X
            // Cargar Y
            int iColum = Integer.parseInt(fileIn.readLine());
            // Poner el tile con el numero ordinal guardado en el archivo
            bpBoard.setTile(iColum, Integer.parseInt(sScan), TileType.values()
                                      [Integer.parseInt(fileIn.readLine())]);
            // Leer la siguiente linea
            sScan = fileIn.readLine();
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
		if(type == null || type == TileType.Fruit1 ||
                        type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {
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
		if(type == null || type == TileType.Fruit1 ||
                        type == TileType.Fruit2|| type == TileType.Fruit3) {
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
		if(type == null || type == TileType.Fruit1 ||
                        type == TileType.Fruit2|| type == TileType.Fruit3) {
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
		if(type == null || type == TileType.Fruit1 ||
                        type == TileType.Fruit2|| type == TileType.Fruit3) {
                    if(++freeFound == index) {                        
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