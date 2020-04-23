package me.cunzai.uphelper.util.cooldown;

public class Cooldown {
    private long startTime;
    private long endTime;

    public Cooldown(long time,TimeUnit unit) {
        this.startTime = System.currentTimeMillis();
        endTime = time * unit.getTime() + startTime;
    }

    public long getRemaining(){
        return endTime - startTime;
    }
    public boolean isComplete(){
        return endTime - System.currentTimeMillis() <= 0;
    }

    public long getStartedTime(){
        return System.currentTimeMillis() - startTime;
    }
}
