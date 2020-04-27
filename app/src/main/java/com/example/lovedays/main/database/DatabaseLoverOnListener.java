package com.example.lovedays.main.database;

import com.example.lovedays.main.units.InfoPersonal;

public interface DatabaseLoverOnListener {
    void updPersonSuccess(String id);
    void updPersonError(String error);

    void getPersonError(String error);
}
