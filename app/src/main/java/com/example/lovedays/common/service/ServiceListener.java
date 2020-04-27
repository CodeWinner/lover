package com.example.lovedays.common.service;

import com.example.lovedays.common.DateUnitCommon;

public interface ServiceListener {
    void onDataReceived(DateUnitCommon dateUnitCommon);
    void onMorning(int isMorning);
}
