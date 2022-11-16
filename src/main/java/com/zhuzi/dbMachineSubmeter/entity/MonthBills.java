package com.zhuzi.dbMachineSubmeter.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthBills {
    private Integer monthBillsId;

    private String serialNumber;

    private BigDecimal payMoney;

    private String machineSerialNo;

    private Date billDate;

    private String billComment;

    private String billsInfo;

    private String targetTable;

    public MonthBills(Integer monthBillsId, String serialNumber, BigDecimal payMoney, String machineSerialNo, Date billDate, String billComment, String billsInfo) {
        this.monthBillsId = monthBillsId;
        this.serialNumber = serialNumber;
        this.payMoney = payMoney;
        this.machineSerialNo = machineSerialNo;
        this.billDate = billDate;
        this.billComment = billComment;
        this.billsInfo = billsInfo;
    }

    public MonthBills() {
        super();
    }

    public Integer getMonthBillsId() {
        return monthBillsId;
    }

    public void setMonthBillsId(Integer monthBillsId) {
        this.monthBillsId = monthBillsId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getMachineSerialNo() {
        return machineSerialNo;
    }

    public void setMachineSerialNo(String machineSerialNo) {
        this.machineSerialNo = machineSerialNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBillComment() {
        return billComment;
    }

    public void setBillComment(String billComment) {
        this.billComment = billComment;
    }

    public String getBillsInfo() {
        return billsInfo;
    }

    public void setBillsInfo(String billsInfo) {
        this.billsInfo = billsInfo;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    @Override
    public String toString() {
        // 将日期转换为YYYY-MM-dd HH:MM:ss格式
        String datetime = new SimpleDateFormat("YYYY-MM-dd HH:MM:ss").format(billDate);
        return "MonthBills{" +
                "monthBillsId=" + monthBillsId +
                ", serialNumber='" + serialNumber + '\'' +
                ", payMoney=" + payMoney +
                ", machineSerialNo='" + machineSerialNo + '\'' +
                ", billDate=" + datetime +
                ", billComment='" + billComment + '\'' +
                ", billsInfo='" + billsInfo + '\'' +
                '}';
    }
}