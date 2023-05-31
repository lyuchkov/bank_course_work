package ru.lyuchkov.bank_course_work.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Loan {
    private int id;
    int amount;
    int accountId;
    int percent;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date deadline;
    boolean isRedeemed = false;
    boolean isExpired = false;

    public Loan(int amount, int accountId, int percent, Date deadline) {
        this.amount = amount;
        this.accountId = accountId;
        this.percent = percent;
        this.deadline = deadline;
    }

    public Loan() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
