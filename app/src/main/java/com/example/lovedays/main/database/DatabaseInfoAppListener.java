package com.example.lovedays.main.database;

public interface DatabaseInfoAppListener {
    void updPersonSuccess(String startDay);
    void updPersionError(String error);

    void getPersonError(String error);

    void updateInforError(String error);
}
