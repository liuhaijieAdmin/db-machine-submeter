package com.zhuzi.dbMachineSubmeter.service;

import com.zhuzi.dbMachineSubmeter.entity.MonthBills;

import java.util.List;

public interface IMonthBillsService {

    int insert(MonthBills monthBills);

    MonthBills selectBySerialNumber(MonthBills monthBills);

    List<MonthBills> rangeQueryByDate(String startTime, String endTime);

}
