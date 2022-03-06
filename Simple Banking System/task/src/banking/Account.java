package banking;
import java.util.*;

public class Account {
    enum subMenu {

    }
    Random random = new Random(29);
    private StringBuilder accountNumber;
    private final String accountPIN;
    private boolean loggedIn = false;

    Account() {
        this.accountPIN = String.valueOf(random.ints(4));
        this.accountNumber = this.accountNumber.append("400000").append(random.ints(9));
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s
                
                """, this.accountNumber, this.accountPIN);
    }

    Account(String account, String pin) {
        this.accountNumber.append(account);
        this.accountPIN = pin;

    }

    @Override
    public boolean equals(Object b) {
        if (this.getClass() != b.getClass()) {
            return false;
        }

        Account other = (Account) b;

        return getAccountNumber().equals(other.getAccountNumber()) && getAccountPIN().equals(other.getAccountPIN());
    }

    public String getAccountPIN() {
        return this.accountPIN;
    }

    public String getAccountNumber() {
        return this.accountNumber.toString();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logIn() {
        this.loggedIn = true;
    }

    public void logOut() {
        this.loggedIn = false;
    }
}
