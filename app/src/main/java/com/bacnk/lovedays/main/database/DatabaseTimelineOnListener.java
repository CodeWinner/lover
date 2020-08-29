package com.bacnk.lovedays.main.database;

public interface DatabaseTimelineOnListener {
    void addTimelineSuccess(String id);
    void addTimeleError(String error);

    void updateTimelineSuccess(String id);
    void updateTimelineError(String error);

    void delTimelineSuccess(int position);
    void delTimelineError(String error);

    void getTimelineError(String error);
}
