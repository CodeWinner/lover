package com.example.lovedays.main.database;

import com.example.lovedays.main.units.InfoPersonal;

public interface DatabaseOnListener {
    void updPersonSuccess(String id);
    void updPersionError(String error);

    void getPersonError(String error);
}
