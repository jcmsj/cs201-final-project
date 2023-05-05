package util;

import java.awt.Component;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Animator extends Timer {
    // in ms
    public static final int INTERVAL = 50;
    public int interval;
    public boolean animating = false;

    public Animator() {
        this(INTERVAL);
    }

    public Animator(int interval) {
        super("Animator");
        this.interval = interval;
    }

    /**
     * executes the task every {@link Animator#interval} ms
     */
    public void every(TimerTask task, int delay) {
        scheduleAtFixedRate(task, delay, interval);
    }

    /**
     * executes the task every {@link Animator#interval} ms
     */
    public void every(Consumer<TimerTask> task, int delay) {
        scheduleAtFixedRate(toTask(task), delay, interval);
    }

    public void schedule(TimerTask task) {
        schedule(task, interval);
    }

    public void schedule(Consumer<TimerTask> task, int _interval) {
        schedule(toTask(task), _interval);
    }

    public TimerTask toTask(Consumer<TimerTask> cb) {
        return new TimerTask() {
            @Override
            public void run() {
                cb.accept(this);
            }
        };
    }

    public void schedule(Consumer<TimerTask> task) {
        schedule(toTask(task));
    }

    public void schedule(Runnable task) {
        schedule(toTask(t -> task.run()));
    }

    public void schedule(Consumer<TimerTask> task, Runnable after) {
        schedule(new TimerTask() {
            @Override
            public void run() {
                task.accept(this);
                after.run();
            }
        });
    }

    /**
     * Attaches a listener to the target. Then, when an event happens, the task is
     * run after
     * {@link Animator#interval}
     */
    public void onPress(int keyCode, Component target, Consumer<TimerTask> task) {
        On.press(keyCode, target, e -> {
            schedule(task);
        });
    }
}