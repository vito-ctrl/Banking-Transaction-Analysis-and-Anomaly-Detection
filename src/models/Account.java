package model;

public sealed abstract class Account
        permits CurrentAccount, SavingsAccount {

    private Long id;
    private String number;
    private double balance;
    private Long clientId;

    public Account(
            Long id,
            String number,
            double balance,
            Long clientId) {

        this.id = id;
        this.number = number;
        this.balance = balance;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}