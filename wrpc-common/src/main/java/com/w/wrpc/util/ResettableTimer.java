package com.w.wrpc.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author wsy
 * @date 2022/10/26 9:36 PM
 * @Description 可重置时间的定时器
 */
public class ResettableTimer {
    private Timer timer;
    private final Supplier<TimerTask> generateTimerTask;
    private final Long delay;
    private final TimeUnit timeUnit;

    public ResettableTimer(Supplier<TimerTask> generateTimerTask, Long delay, TimeUnit timeUnit) {
        timer = new Timer();
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.generateTimerTask = generateTimerTask;
    }

    public void schedule() {
        timer.schedule(generateTimerTask.get(), timeUnit.toMillis(delay));
    }

    public void reset() {
        timer.cancel();
        timer = new Timer();
        this.schedule();
    }
}
