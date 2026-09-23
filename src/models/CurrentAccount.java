package model;

public final class CurrentAccount extends Account {
    private double authorizedOverdraft;

    public CurrentAccount(Long id, String number, double balance, Long clientId, double authorizedOverdraft) {

        super(id, number, balance, clientId);
        this.authorizedOverdraft = authorizedOverdraft;
    }

    public double getAuthorizedOverdraft() {
        return authorizedOverdraft;
    }

    public void setAuthorizedOverdraft(double authorizedOverdraft) {
        this.authorizedOverdraft = authorizedOverdraft;
    }
}