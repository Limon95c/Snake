
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

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
		largeMessage = "Snake Game!";
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
		
            /*
             * A fruit is depicted as a small red circle that with a bit of 
             * padding on each side.
             */
            case Fruit:
		g.setColor(Color.RED);
		g.fillOval(x + 2, y + 2, iTILE_SIZE - 4, iTILE_SIZE - 4);
		break;
		
            /*
             * The snake body is depicted as a green square that takes up the
             * entire tile.
             */
            case SnakeBody:
		g.setColor(Color.GREEN);
		g.fillRect(x, y, iTILE_SIZE, iTILE_SIZE);
		break;
			
            /*
             * The snake head is depicted similarly to the body, but with two
             * lines (representing eyes) that indicate it's direction.
             */
            case SnakeHead:
		//Fill the tile in with green.
		g.setColor(Color.GREEN);
		g.fillRect(x, y, iTILE_SIZE, iTILE_SIZE);
			
		//Set the color to black so that we can start drawing the eyes.
		g.setColor(Color.BLACK);
			
		/*
		 * The eyes will always 'face' the direction that the snake is
		 * moving.
		 * 
		 * Vertical lines indicate that it's facing North or South, and
		 * Horizontal lines indicate that it's facing East or West.
		 * 
		 * Additionally, the eyes will be closer to whichever edge it's
		 * facing.
		 * 
		 * Drawing the eyes is fairly simple, but is a bit difficult to
		 * explain. The basic process is this:
		 * 
		 * First, we add (or subtract) iEYE_SMALL_INSET to or from the
		 * side of the tile representing the direction we're facing.
		 * This will be constant for both eyes, and is represented by
		 * the variable 'baseX' or 'baseY' (depending on orientation).
		 * 
		 * Next, we add (or subtract) iEYE_LARGE_INSET to and from the
		 * two neighboring directions (Example; East and West if we're
		 * facing north).
		 * 
		 * Finally, we draw a line from the base offset that is 
		 * iEYE_LENGTH pixels in length at whatever the offset is from 
		 * the neighboring directions.
		 * 
		 */
		switch(snkGame.getDirection()) {
                    case North: {
			int baseY = y + iEYE_SMALL_INSET;
			g.drawLine(x + iEYE_LARGE_INSET,
                                   baseY, x + iEYE_LARGE_INSET,
                                   baseY + iEYE_LENGTH);
			g.drawLine(x + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseY, x + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseY + iEYE_LENGTH);
			break;
                    }
				
                    case South: {
			int baseY = y + iTILE_SIZE - iEYE_SMALL_INSET;
			g.drawLine(x + iEYE_LARGE_INSET,
                                   baseY, x + iEYE_LARGE_INSET,
                                   baseY - iEYE_LENGTH);
			g.drawLine(x + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseY, x + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseY - iEYE_LENGTH);
			break;
                    }
			
                    case West: {
			int baseX = x + iEYE_SMALL_INSET;
			g.drawLine(baseX, y + iEYE_LARGE_INSET,
                                   baseX + iEYE_LENGTH, y + iEYE_LARGE_INSET);
			g.drawLine(baseX, y + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseX + iEYE_LENGTH,
                                   y + iTILE_SIZE - iEYE_LARGE_INSET);
			break;
                    }
				
                    case East: {
			int baseX = x + iTILE_SIZE - iEYE_SMALL_INSET;
			g.drawLine(baseX, y + iEYE_LARGE_INSET,
                                   baseX - iEYE_LENGTH, y + iEYE_LARGE_INSET);
			g.drawLine(baseX, y + iTILE_SIZE - iEYE_LARGE_INSET,
                                   baseX - iEYE_LENGTH,
                                   y + iTILE_SIZE - iEYE_LARGE_INSET);
			break;
                    }
		}
		break;
        }
    }
}