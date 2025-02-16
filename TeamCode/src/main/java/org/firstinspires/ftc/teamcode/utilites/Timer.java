package org.firstinspires.ftc.teamcode.utilites;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

public class Timer {
    private final long timerLength;
    private final TimeUnit unit;
    private ElapsedTime time;
    private long pauseTime; // in nanoseconds, regardless of unit
    private boolean timerOn;

    /**
     * Creates a new timer object.
     *
     * @param timerLength The length of the timer, in the units specified by unit.
     * @param unit        The unit of timerLength.
     */
    public Timer(long timerLength, TimeUnit unit) {
        this.timerLength = timerLength;
        this.unit = unit;
        this.time = new ElapsedTime();
        time.reset();
    }

    /**
     * Creates a new timer object.
     *
     * @param timerLength The length of the timer, in seconds.
     */
    public Timer(long timerLength) {
        this(timerLength, TimeUnit.SECONDS);
    }

    /**
     * Starts this timer.
     */
    public void start() {
        time.reset();
        pauseTime = 0;
        timerOn = true;
    }

    /**
     * Pauses this timer. While the timer is paused, elapsedTime() and remainingTime() will not change.
     */
    public void pause() {
        if (timerOn) {
            pauseTime = time.nanoseconds();
            timerOn = false;
        }
    }

    /**
     * Resumes this timer if it is running and paused.
     */
    public void resume() {
        if (!timerOn) {
            // we start the timer with a time in the past, since we're starting in the middle of the timer
            time = new ElapsedTime(System.nanoTime() - pauseTime);
            timerOn = true;
        }
    }

    /**
     * Get the elapsed time since this time was started.
     *
     * @return The elapsed time, in the units specified in the constructor.
     * If the timer was not started, return 0.
     * If the timer is paused, return the time at which the timer was paused.
     */
    public long elapsedTime() {
        if (timerOn) return time.time(unit);
        else return unit.convert(pauseTime, TimeUnit.NANOSECONDS);
    }

    /**
     * Get the remaining time until this timer is done.
     *
     * @return The remaining time, in the units specified in the constructor.
     * If it was not started, returns the timer length.
     * If it was paused, return the remaining time at the time the timer was paused.
     */
    public long remainingTime() {
        return timerLength - elapsedTime();
    }

    /**
     * Check if this timer has finished.
     *
     * @return True if at least timerLength of unpaused time has elapsed since the start of this timer. False otherwise.
     */
    public boolean done() {
        return elapsedTime() >= timerLength;
    }

    /**
     * Check if this timer is running.
     *
     * @return True if this timer has been started and is not paused, false otherwise.
     */
    public boolean isTimerOn() {
        return timerOn;
    }

    /**
     * Resets this timer to 0 elapsed time, without starting it.
     */
    public void reset() {
        time.reset();
        pauseTime = 0;
        timerOn = false; // Make sure timer is not running after reset
    }
}
