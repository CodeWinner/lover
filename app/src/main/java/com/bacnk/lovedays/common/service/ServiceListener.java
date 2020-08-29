package com.bacnk.lovedays.common.service;

import com.bacnk.lovedays.common.DateUnitCommon;

public interface ServiceListener {
    void onDataReceived(DateUnitCommon dateUnitCommon);
    void onMorning(int isMorning);
}
