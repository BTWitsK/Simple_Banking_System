package banking;
import java.util.*;

class Account {
    enum subMenu {
        BALANCE(1),
        LOGOUT(2),
        EXIT(0);

        int input;

        subMenu(int input) {
            this.input = input;
        }
    }
    Random random;
    private StringBuilder accountNumber = new StringBuilder();
    private final String accountPIN;
    private boolean loggedIn = false;
    private int balance;
    subMenu menu;

    Account() {
        random = new Random();
        this.accountPIN = String.valueOf(random.nextInt(1000, 10000));
        this.accountNumber = this.accountNumber.append("400000")
                .append(random.nextInt(10000, 100000))
                .append(random.nextInt(1000, 10000))
                .append(addCheckSum(accountNumber));
        this.balance = 0;
    }

    Account(String account, String pin) {
        random = new Random();
        this.accountNumber.append(account);
        this.accountPIN = pin;
        this.balance = 0;

    }

    @Override
    public boolean equals(Object b) {
        if (this.getClass() != b.getClass()) {
            return false;
        }

        Account other = (Account) b;

        return getAccountNumber().equals(other.getAccountNumber()) && getAccountPIN().equals(other.getAccountPIN());
    }

    public int addCheckSum(StringBuilder accountNumber) {
        ArrayList<String> accountList = new ArrayList<>(Arrays.asList(accountNumber.toString().split("")));
        ArrayList<Integer> intList = new ArrayList<>();

        accountList.forEach(num -> intList.add(Integer.parseInt(num)));
        int sum = 0;

        for (int i = 0; i < intList.size(); i ++) {
            if (i % 2 == 0) {
                intList.set(i, intList.get(i) * 2 > 9 ? intList.get(i) * 2 - 9 : intList.get(i) * 2 );
            }
            sum += intList.get(i);
        }

        return 10 - sum % 10 == 10 ? 0 : 10 - sum % 10;
    }

    public void setSubMenu(int input) {
        for (subMenu entry: subMenu.values()) {
            if (entry.input == input) {
                this.menu = entry;
            }
        }
    }

    public void printSubMenu() {
        System.out.print("""
                1. Balance
                2. Log out
                0. Exit
                """);
    }

    public String getAccountPIN() {
        return this.accountPIN;
    }

    public String getAccountNumber() {
        return this.accountNumber.toString();
    }

    public int getBalance() {
        return this.balance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void logIn() {
        this.loggedIn = true;
        System.out.println("\nYou have successfully logged in!");
    }

    public void logOut() {
        this.loggedIn = false;
        System.out.println("\nYou have successfully logged out!");
    }
}
