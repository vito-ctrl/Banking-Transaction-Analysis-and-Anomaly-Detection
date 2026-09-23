package model;

public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(Long id, String number, double balance, Long clientId, double interestRate) {

        super(id, number, balance, clientId);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}