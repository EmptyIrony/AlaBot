package me.cunzai.uphelper.util.cooldown;

public enum TimeUnit {
    MilliSecond(1),
    Second(1000),
    Minute(1000 * 60),
    Hour(1000 * 60 * 60),
    Day(1000 * 60 * 60 * 24);

    private long time;

    TimeUnit(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
