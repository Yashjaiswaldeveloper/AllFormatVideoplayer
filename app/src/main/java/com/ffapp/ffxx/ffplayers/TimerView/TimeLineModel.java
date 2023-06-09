package com.ffapp.ffxx.ffplayers.TimerView;

public class TimeLineModel implements TimelineObject {
    long timeline;
    String name, url;
    public TimeLineModel(long timeline, String name, String url) {
        this.timeline = timeline;
        this.name = name;
        this.url = url;
    }

    @Override
    public long getTimestamp() {
        return timeline;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return url;
    }
}
