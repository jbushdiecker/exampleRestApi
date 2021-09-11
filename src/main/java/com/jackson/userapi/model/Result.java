package com.jackson.userapi.model;

public class Result {
    private User user;
    private Address address;

    public Result(User user, Address address) {
        this.address = address;
        this.user = user;
    }

    public Result() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
