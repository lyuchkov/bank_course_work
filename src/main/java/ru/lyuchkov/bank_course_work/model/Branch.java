package ru.lyuchkov.bank_course_work.model;

public class Branch {
    int id;
    public String address;
    public String phone;

    public Branch() {

    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Branch(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public Branch(int id, String address, String phone) {
        this.id = id;
        this.address = address;
        this.phone = phone;
    }
}
