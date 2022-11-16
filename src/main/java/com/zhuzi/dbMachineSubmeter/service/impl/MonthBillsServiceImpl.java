package com.zhuzi.dbMachineSubmeter.service.impl;

import com.zhuzi.dbMachineSubmeter.dao.MonthBillsMapper;
import com.zhuzi.dbMachineSubmeter.entity.MonthBills;
import com.zhuzi.dbMachineSubmeter.service.IMonthBillsService;
import com.zhuzi.dbMachineSubmeter.utils.TableTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MonthBillsServiceImpl implements IMonthBillsService {

    @Autowired
    private MonthBillsMapper billsMapper;

    @Override
    public int insert(MonthBills monthBills) {
        // 获取要插入数据的表名
        TableTimeUtils.getDataByTable(monthBills);
        // 返回插入数据的状态
        return billsMapper.insertSelective(monthBills);
    }

    @Override
    public MonthBills selectBySerialNumber(MonthBills monthBills) {
        // 根据流水号获取要查询数据的具体表名
        TableTimeUtils.getTableBySerialNumber(monthBills);
        return billsMapper.selectBySerialNumber(monthBills);
    }

    @Override
    public List<MonthBills> rangeQueryByDate(String startTime, String endTime) {
        // 获取范围查询时的SQL语句
        String sql = TableTimeUtils.getRangeQuerySQL(startTime,endTime);
        return billsMapper.rangeQueryByDate(sql);
    }

}
