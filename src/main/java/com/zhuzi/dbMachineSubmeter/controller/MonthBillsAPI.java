package com.zhuzi.dbMachineSubmeter.controller;

import com.zhuzi.dbMachineSubmeter.entity.MonthBills;
import com.zhuzi.dbMachineSubmeter.service.IMonthBillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bills")
public class MonthBillsAPI {

    @Autowired
    private IMonthBillsService billsService;

    // 账单结算的API
    @RequestMapping("/settleUp")
    public String settleUp(MonthBills monthBills){
        // 设置账单交易时间为当前时间
        monthBills.setBillDate(new Date(System.currentTimeMillis()));
        // 使用收银机器序列号+时间戳作为流水号
        monthBills.setSerialNumber(monthBills.getMachineSerialNo()+System.currentTimeMillis());
        // 调用新增账单数据的service方法
        if (billsService.insert(monthBills) > 0){
            return ">>>>账单结算成功<<<<";
        }
        return ">>>>账单结算失败<<<<";
    }

    // 根据流水号查询数据的API
    @RequestMapping("/selectBySerialNumber")
    public String selectBySerialNumber(MonthBills monthBills){
        // 调用Service层根据流水号查询数据的方法
        MonthBills result = billsService.selectBySerialNumber(monthBills);
        if (result != null){
            return result.toString();
        }
        return ">>>>未查询到流水号对应的数据<<<<";
    }

    // 按照范围查询两个日期之间的所有账单数据
    @RequestMapping("/rangeQueryByTime")
    public String rangeQueryByTime(@RequestParam("start") String start, @RequestParam("end")String end){
        // 调用Service层根据流水号查询数据的方法
        List<MonthBills> bills = billsService.rangeQueryByDate(start, end);
        if (bills != null){
            return bills.toString();
        }
        return ">>>>指定的日期中没有账单数据<<<<";
    }

}
