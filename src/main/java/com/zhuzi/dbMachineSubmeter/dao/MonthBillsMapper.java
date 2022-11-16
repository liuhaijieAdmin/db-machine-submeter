package com.zhuzi.dbMachineSubmeter.dao;

import com.zhuzi.dbMachineSubmeter.entity.MonthBills;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MonthBillsMapper {
    int deleteBySerialNumber(MonthBills record);

    int insertSelective(MonthBills record);

    MonthBills selectBySerialNumber(MonthBills record);

    int updateBySerialNumber(MonthBills record);

    List<MonthBills> rangeQueryByDate(@Param("sql") String sql);
}