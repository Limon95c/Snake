
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for managing and displaying the
 * contents of the snkGame board.
 * @author Brendan Jones
 *
 */
public class BoardPanel extends JPanel {
	
    /**
     * Serial Version UID.
     */
    private static final long lSerialVersionUID = -1102632585936750607L;

    /**
     * The number of columns on the board. (Should be odd so we can start in
     * the center).
     */
    public static final int iCOL_COUNT = 25;
	
    /**
     * The number of rows on the board. (Should be odd so we can start in
     * the center).
     */
    public static final int iROW_COUNT = 25;
	
    /**
     * The size of each tile in pixels.
     */
    public static final int iTILE_SIZE = 20;
	
    /**
     * The number of pixels to offset the eyes from the sides.
     */
    private static final int iEYE_LARGE_INSET = iTILE_SIZE / 3;
	
    /**
     * The number of pixels to offset the eyes from the front.
     */
    private static final int iEYE_SMALL_INSET = iTILE_SIZE / 6;
	
    /**
     * The length of the eyes from the base (small inset).
     */
    private static final int iEYE_LENGTH = iTILE_SIZE / 5;
	
    /**
     * The font to draw the text with.
     */
    private static final Font fFONT = new Font("Tahoma", Font.BOLD, 25);
		
    /**
     * The SnakeGame instance.
     */
    private SnakeGame snkGame;
	
    /**
     * The array of ttArrTiles that make up this board.
     */
    private TileType[] ttArrTiles;
    
    // Imagen del cohete hacia arriba
    private Image imaCoheteArriba;
    
    // Imagen del cohete a la derecha
    private Image imaCoheteDerecha;
    
    // Imagen del cohete hacia abajo
    private Image imaCoheteAbajo;
    
    // Imagen del cohete hacia la izquierda
    private Image imaCoheteIzquierda;
    
    // Imagen del fuego
    private Image imaFuego;
    
    // Imagen de la comida
    private Image imaComida;
    
    // Imagen de la bateria
    private Image imaBattery;
    
    // Imagen del combustible 
    private Image imaFuel;
    
    // Imagen de la tierra
    private Image imaTierra;
    
    // Imagen del fondo
    private Image imaFondo;  
    
    /**
     * Creates a new BoardPanel instance.
     * @param snkGame The SnakeGame instance.
     */
    public BoardPanel(SnakeGame snkGame) {
	this.snkGame = snkGame;
	this.ttArrTiles = new TileType[iROW_COUNT * iCOL_COUNT];
		
	setPreferredSize(new Dimension(iCOL_COUNT * iTILE_SIZE,
                                       iROW_COUNT * iTILE_SIZE));
	setBackground(Color.BLACK);
        
        // Crear imagen del cohete hacia arriba
        imaCoheteArriba = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("cohete1.png"));
    
        // Crear imagen del cohete a la derecha
        imaCoheteDerecha = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("cohete2.png"));
    
        // Crear imagen del cohete hacia abajo
        imaCoheteAbajo = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("cohete3.png"));
    
        // Crear imagen del cohete hacia la izquierda
        imaCoheteIzquierda = Toolkit.getDefaultToolkit().
                getImage(this.getClass().getResource("cohete4.png"));
    
        // Crear imagen del fuego
        imaFuego = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("Fuego.png"));
    
        // Crear imagen de la comida
        imaComida = Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("Food.png")); 
    
        // Crear imagen de la bateria
        imaBattery = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("Battery.png"));
    
        // Crear imagen del combustible 
        imaFuel = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("Fuel.png"));  
    
        // Crear imagen de la tierra
        imaTierra = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("Tierra.gif"));  
    
        // Crear imagen del fondo
        imaFondo = Toolkit.getDefaultToolkit().getImage(this.getClass().
               getResource("Fondo.png"));  
    }
	
    /**
     * Clears all of the ttArrTiles on the board and sets their values to null.
     */
    public void clearBoard() {
	for(int i = 0; i < ttArrTiles.length; i++) {
            ttArrTiles[i] = null;
	}
    }
	
    /**
     * Sets the tile at the desired coordinate.
     * @param point The coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(Point point, TileType type) {
	setTile(point.x, point.y, type);
    }
	
    /**
     * Sets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @param type The type to set the tile to.
     */
    public void setTile(int x, int y, TileType type) {
    	ttArrTiles[y * iROW_COUNT + x] = type;
    }
	
    /**
     * Gets the tile at the desired coordinate.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return
     */
    public TileType getTile(int x, int y) {
    	return ttArrTiles[y * iROW_COUNT + x];
    }
	
    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	
        if(imaFondo!=null) {
                    
            g.drawImage(imaFondo, 0, 0, 500, 500, this);
        }
        
	/*
	 * Loop through each tile on the board and draw it if it
	 * is not null.
	 */
	for(int x = 0; x < iCOL_COUNT; x++) {
            for(int y = 0; y < iROW_COUNT; y++) {
		TileType type = getTile(x, y);
		if(type != null) {
                    drawTile(x * iTILE_SIZE, y * iTILE_SIZE, type, g);
		}
            }
	}
		
	/*
	 * Draw the grid on the board. This makes it easier to see exactly
	 * where we in relation to the fruit.
	 * 
	 * The panel is one pixel too small to draw the bottom and right
	 * outlines, so we outline the board with a rectangle separately.
	 */
	g.setColor(Color.DARK_GRAY);
	g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	for(int x = 0; x < iCOL_COUNT; x++) {
            for(int y = 0; y < iROW_COUNT; y++) {
		g.drawLine(x * iTILE_SIZE, 0, x * iTILE_SIZE, getHeight());
		g.drawLine(0, y * iTILE_SIZE, getWidth(), y * iTILE_SIZE);
            }
	}		
		
	/*
	 * Show a message on the screen based on the current snkGame state.
	 */
	if(snkGame.bIsGameOver() || snkGame.bIsNewGame() ||
           snkGame.bIsPaused()) {
            g.setColor(Color.WHITE);
			
            /*
             * Get the center coordinates of the board.
             */
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
			
            /*
             * Allocate the messages for and set their values based on the 
             * snkGame state.
             */
            String largeMessage = null;
            String smallMessage = null;
            if(snkGame.bIsNewGame()) {
		largeMessage = "Rocket Snake!";
		smallMessage = "Press Enter to Start";
            }
            else if(snkGame.bIsGameOver()) {
		largeMessage = "Game Over!";
		smallMessage = "Press Enter to Restart";
            }
            else if(snkGame.bIsPaused()) {
		largeMessage = "Paused";
		smallMessage = "Press P to Resume";
            }
			
            /*
             * Set the message font and draw the messages in the center of the
             * board.
             */
            g.setFont(fFONT);
            g.drawString(largeMessage,
                         centerX -
                         g.getFontMetrics().stringWidth(largeMessage) / 2,
                         centerY - 50);
            g.drawString(smallMessage,
                         centerX -
                         g.getFontMetrics().stringWidth(smallMessage) / 2,
                         centerY + 50);
	}
    }
	
    /**
     * Draws a tile onto the board.
     * @param x The x coordinate of the tile (in pixels).
     * @param y The y coordinate of the tile (in pixels).
     * @param type The type of tile to draw.
     * @param g The graphics object to draw to.
     */
    private void drawTile(int x, int y, TileType type, Graphics g) {
    	/*
	 * Because each type of tile is drawn differently, it's easiest
	 * to just run through a switch statement rather than come up with some
	 * overly complex code to handle everything.
	 */
	switch(type) {
            
            //Dibuja la Comida, Objeto bueno que da Puntos
            case Fruit1:
                if(imaComida!=null){
                    g.drawImage(imaComida, x, y, 20, 20, this);
                }
		break;
            
           //Dibuja el Combustible, Objeto bueno que da Puntos     
            case Fruit2:
                if(imaFuel!=null){
                    g.drawImage(imaFuel, x, y, 20, 20, this); 
                }
		break;
           
           //Dibuja el Combustible, Objeto bueno que da Puntos
            case Fruit3:
                if(imaBattery!=null){    
                    g.drawImage(imaBattery, x, y, 20, 20, this); 
                }
		break;
            
            //Dibuja el Planeto, Objeto Malo que te hace perder el juego
            case BadFruit:
                if(imaTierra!=null){
                    g.drawImage(imaTierra, x, y, 20, 20, this); 
                }
		break;
                
            //Dibuja el cuerpo del Jugador
            case SnakeBody:
                if(imaFuego != null){
                    g.drawImage(imaFuego, x, y, 20, 20, this);
                }
		break;
            
            //Sibuja la Cabeza del Jugador
            case SnakeHead:
		switch(snkGame.getDirection()) {
                    
                    //Si esta volteando hacia Arriba
                    case North: {
                        if(imaCoheteArriba != null) {
                            g.drawImage(imaCoheteArriba, x, y, 20, 20, this);
                        }
                        break;
                    }
                    //Si esta volteando hacia Abajo
                    case South: {
                        if(imaCoheteAbajo != null) {
                            g.drawImage(imaCoheteAbajo, x, y, 20, 20, this);
                        }
			break;
                    }
                    
                    //Si esta volteando hacia la Izquierda
                    case West: {
                        if(imaCoheteIzquierda != null) {
                            g.drawImage(imaCoheteIzquierda, x, y, 20, 20, this);
                        }
			break;
                    }
		    
                    ////Si esta volteando hacia la Derecha
                    case East: {
                        if(imaCoheteDerecha != null) {
                            g.drawImage(imaCoheteDerecha, x, y, 20, 20, this);
                        }
			break;
                    }
		}
		break;
        }
    }
    
    /**
     * Sets a tilTile located at the desired column and iRow in null
     * @param iX The column.
     * @param iY The iRow.
     */
    public void nullTile(int  iX, int iY) {
	ttArrTiles[iY * iROW_COUNT + iX] = null;
    }
}