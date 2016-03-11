
/**
 * The {@code Clock} class is responsible for tracking the number of cycles
 * that have elapsed over time. 
 * @author Brendan Jones
 *
 */
public class Clock {
	
    /**
     * The number of milliseconds that make up one cycle.
     */
    private float flMillisPerCycle;
	
    /**
     * The last time that the clock was updated (used for calculating the
     * flDelta time).
     */
    private long lLastUpdate;
	
    /**
     * The number of cycles that have elapsed and have not yet been polled.
     */
    private int iElapsedCycles;
	
    /**
     * The amount of excess time towards the next elapsed cycle.
     */
    private float flExcessCycles;
	
    /**
     * Whether or not the clock is bPaused.
     */
    private boolean bIsPaused;
	
    /**
     * Creates a new clock and sets it's cycles-per-second.
     * @param flCyclesPerSecond The number of cycles that elapse per second.
     */
    public Clock(float flCyclesPerSecond) {
	setCyclesPerSecond(flCyclesPerSecond);
	reset();
    }
	
    /**
     * Sets the number of cycles that elapse per second.
     * @param flCyclesPerSecond The number of cycles per second.
     */
    public void setCyclesPerSecond(float flCyclesPerSecond) {
	this.flMillisPerCycle = (1.0f / flCyclesPerSecond) * 1000;
    }
	
    /**
     * Resets the clock stats. Elapsed cycles and cycle excess will be reset
     * to 0, the last update time will be reset to the current time, and the
     * bPaused flag will be set to false.
     */
    public void reset() {
    	this.iElapsedCycles = 0;
        this.flExcessCycles = 0.0f;
	this.lLastUpdate = getCurrentTime();
	this.bIsPaused = false;
    }
	
    /**
     * Updates the clock stats. The number of elapsed cycles, as well as the
     * cycle excess will be calculated only if the clock is not bPaused. This
     * method should be called every frame even when bPaused to prevent any
     * nasty surprises with the flDelta time.
     */
    public void update() {
    	//Get the current time and calculate the flDelta time.
	long lCurrUpdate = getCurrentTime();
	float flDelta = (float)(lCurrUpdate - lLastUpdate) + flExcessCycles;
		
	//Update the number of elapsed and excess ticks if we're not bPaused.
	if(!bIsPaused) {
            this.iElapsedCycles += (int)Math.floor(flDelta / flMillisPerCycle);
            this.flExcessCycles = flDelta % flMillisPerCycle;
	}
		
	//Set the last update time for the next update cycle.
	this.lLastUpdate = lCurrUpdate;
    }
	
    /**
     * Pauses or unpauses the clock. While bPaused, a clock will not update
     * elapsed cycles or cycle excess, though the {@code update} method should
     * still be called every frame to prevent issues.
     * @param bPaused Whether or not to pause this clock.
     */
    public void setPaused(boolean bPaused) {
	this.bIsPaused = bPaused;
    }
	
    /**
     * Checks to see if the clock is currently bPaused.
     * @return Whether or not this clock is bPaused.
     */
    public boolean bIsPaused() {
    	return bIsPaused;
    }
	
    /**
     * Checks to see if a cycle has elapsed for this clock yet. If so,
     * the number of elapsed cycles will be decremented by one.
     * @return Whether or not a cycle has elapsed.
     * @see peekElapsedCycle
     */
    public boolean hasElapsedCycle() {
	if(iElapsedCycles > 0) {
            this.iElapsedCycles--;
            return true;
	}
	return false;
    }
	
    /**
     * Checks to see if a cycle has elapsed for this clock yet. Unlike
     * {@code hasElapsedCycle}, the number of cycles will not be decremented
     * if the number of elapsed cycles is greater than 0.
     * @return Whether or not a cycle has elapsed.
     * @see hasElapsedCycle
     */
    public boolean peekElapsedCycle() {
	return (iElapsedCycles > 0);
    }
	
    /**
     * Calculates the current time in milliseconds using the computer's high
     * resolution clock. This is much more reliable than
     * {@code System.getCurrentTimeMillis()}, and quicker than
     * {@code System.nanoTime()}.
     * @return The current time in milliseconds.
     */
    private static final long getCurrentTime() {
	return (System.nanoTime() / 1000000L);
    }
}
