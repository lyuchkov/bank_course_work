package ru.lyuchkov.bank_course_work.model;

public class AtmOperation {
    private int id;
    int amount;
    boolean input;
    int accountId;

    public AtmOperation(int amount, boolean input, int accountId) {
        this.amount = amount;
        this.input = input;
        this.accountId = accountId;
    }

    public AtmOperation(int id, int amount, boolean input, int accountId) {
        this.id = id;
        this.amount = amount;
        this.input = input;
        this.accountId = accountId;
    }

    public AtmOperation() {

    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }


    public int getAccountId() {
        return accountId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
