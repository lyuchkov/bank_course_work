package ru.lyuchkov.bank_course_work.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Transfer {
    public int id;
    public int amount;
    public int accountFromId;
    public int accountToId;
    @DateTimeFormat(pattern = "YYYY-MM-DD HH:MI:SS")
    public Date date;

    public String toNumber;

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public Transfer() {
    }

    public Transfer(int id, int amount, int accountFromId, int accountToId, Date date) {
        this.id = id;
        this.amount = amount;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.date = date;
    }

    public Transfer(int amount, int accountFromId, int accountToId, Date date) {
        this.amount = amount;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
