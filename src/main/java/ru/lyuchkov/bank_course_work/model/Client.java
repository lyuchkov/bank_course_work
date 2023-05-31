package ru.lyuchkov.bank_course_work.model;

public class Client {
    private int id;
    String name;
    String surname;
    String email;
    String phone;
    private int accountId;
    boolean active = true;
    String accountName;
    int branchId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Client(int id, String name, String surname, String email, String phone, int accountId, boolean active, String accountName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.accountId = accountId;
        this.active = active;
        this.accountName = accountName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Client() {
    }

    public Client(int id, String name, String surname, String email, String phone, int accountId, boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.accountId = accountId;
        this.active = active;
    }

    public Client(String name, String surname, String email, String phone, boolean active) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.active = active;
    }

    public Client(String name, String surname, String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
    }

    public Client(int id, String name, String surname, String email, String phone, int accountId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.accountId = accountId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getAccountId() {
        return accountId;
    }
}
