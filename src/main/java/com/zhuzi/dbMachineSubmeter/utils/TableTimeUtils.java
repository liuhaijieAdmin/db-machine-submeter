package com.zhuzi.dbMachineSubmeter.utils;

import com.zhuzi.dbMachineSubmeter.dao.MonthBillsMapper;
import com.zhuzi.dbMachineSubmeter.entity.MonthBills;
import com.zhuzi.dbMachineSubmeter.service.impl.MonthBillsServiceImpl;
import sun.security.x509.SerialNumber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class TableTimeUtils {
    /*
     * 使用ThreadLocal来确保线程安全，或者可以使用Java8新引入的DateTimeFormatter类：
     *      monthTL：负责将一个日期处理成 YYYYMM 格式
     */
    private static ThreadLocal<SimpleDateFormat> monthTL =
            ThreadLocal.withInitial(() ->
                    new SimpleDateFormat("YYYYMM"));
    private static ThreadLocal<SimpleDateFormat> dateTL =
            ThreadLocal.withInitial(() ->
                    new SimpleDateFormat("YYYY-MM-DD"));

    // 表的前缀
    private static String tablePrefix = "month_bills_";

    // 将一个日期格式化为YYYYMM格式
    public static String getYearMonth(Date date) {
        return monthTL.get().format(date);
    }

    // 获取目标数据的表名（操作单条数据公用的方法：增删改查）
    public static void getDataByTable(MonthBills monthBills){
        // 获取传入对象的时间
        Date billDate = monthBills.getBillDate();
        // 根据该对象中的时间，计算出要操作的表名后缀
        String yearMonth = getYearMonth(billDate);
        // 将表前缀和后缀拼接，得到完整的表名，如：month_bills_202211
        monthBills.setTargetTable(tablePrefix + yearMonth);
    }

    // 根据流水号得到表名
    public static void getTableBySerialNumber(MonthBills monthBills){
        // 获取流水号的后13位（时间戳）
        String timeMillis = monthBills.getSerialNumber().
                substring(monthBills.getSerialNumber().length() - 13);
        // 将字符串类型的时间戳转换为long类型
        long millis = Long.parseLong(timeMillis);
        // 调用getYearMonth()方法获取时间戳中的年月
        String yearMonth = getYearMonth(new Date(millis));
        // 用表的前缀名拼接年月，得到最终要操作的表名
        monthBills.setTargetTable(tablePrefix + yearMonth);
    }

    // 获取按时间范围查询时，两个日期之间，所有月份账单表的表名
    public static List<String> getRangeQueryByTables(String startTime, String endTime){
        // 声明一个日期格式化类
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 声明保存表名的集合
        List<String> tables = new ArrayList<>();
        try {
            // 将两个传入的字符日期转换成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用 Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()){
                // 把生成的月份拼接表前缀名，加入到集合中
                tables.add(tablePrefix + monthTL.get().format(startDate));
                // 设置日期，并把比对器的日期增加一月
                calendar.setTime(startDate);
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tables;
    }

    // 根据日期生成SQL语句的方法
    public static String getRangeQuerySQL(String startTime, String endTime){
        // 先获取两个日期之间的所有表名
        List<String> tables = getRangeQueryByTables(startTime, endTime);
        // 提前创建一个字符串对象保存SQL语句
        StringBuffer sql = new StringBuffer();
        // 如果查询的两个日期是同一张表，则直接生成 BETWEEN AND 的SQL语句
        if (tables.size() == 1){
            sql.append("select * from ")
                    .append(tables.get(0))
                    .append(" where bill_date BETWEEN '")
                    .append(startTime)
                    .append("' AND '")
                    .append(endTime)
                    .append("';");
            // 如果本次范围查询的两个日期之间有多张表
        }else {
            // 则用for循环遍历所有表名
            for (String table : tables) {
                // 对于第一张表则只需要查询开始日期之后的数据
                if (table.equals(tables.get(0))){
                    sql.append("select * from ")
                            .append(table)
                            .append(" where bill_date > '")
                            .append(startTime)
                            .append("' union all ");
                }
                // 对于最后一张表只需要查询结束日期之前的数据
                else if (table.equals(tables.get(tables.size()-1))){
                    sql.append("select * from ")
                            .append(table)
                            .append(" where bill_date < '")
                            .append(endTime)
                            .append("';");
                    // 对于其他表则获取所有数据
                } else {
                    sql.append("select * from ")
                            .append(table)
                            .append("' union all ");
                }
            }
        }
        // 返回最终生成的SQL语句
        return sql.toString();
    }



    public static void main(String[] args) throws ParseException {
        String startStr = "2022-11-26";
        String endStr = "2022-12-09";
        List<String> tables = getRangeQueryByTables(startStr, endStr);
        System.out.println(tables);
        String sql = getRangeQuerySQL(startStr,endStr);
        System.out.println(sql);
    }

}
