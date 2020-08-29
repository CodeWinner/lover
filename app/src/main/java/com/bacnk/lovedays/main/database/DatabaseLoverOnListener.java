package com.bacnk.lovedays.main.database;

public interface DatabaseLoverOnListener {
    void updPersonSuccess(String id);
    void updPersonError(String error);

    void getPersonError(String error);
}
