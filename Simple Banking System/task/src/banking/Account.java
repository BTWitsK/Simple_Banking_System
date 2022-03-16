package banking;
import java.util.*;

class Account {
    enum subMenu {
        BALANCE(1),
        INCOME(2),
        TRANSFER(3),
        CLOSE(4),
        LOGOUT(5),
        EXIT(0);
        final int input;
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
    static int privateKey = 0;
    private final int key;

    Account() {
        random = new Random();
        this.accountPIN = String.valueOf(random.nextInt(1000, 10000));
        this.accountNumber = this.accountNumber.append("400000")
                .append(random.nextInt(10000, 100000))
                .append(random.nextInt(1000, 10000))
                .append(addCheckSum(accountNumber));
        this.balance = 0;
        this.key = privateKey++;
    }

    Account(String account, String pin) {
        random = new Random();
        this.accountNumber.append(account);
        this.accountPIN = pin;
        this.balance = 0;
        this.key = privateKey++;
    }

    public static int luhnSum(ArrayList<String> accountNumber) {
        ArrayList<Integer> intList = new ArrayList<>();
        accountNumber.forEach(num -> intList.add(Integer.parseInt(num)));
        int sum = 0;

        for (int i = 0; i < intList.size(); i ++) {
            if (i % 2 == 0) {
                intList.set(i, intList.get(i) * 2 > 9 ? intList.get(i) * 2 - 9 : intList.get(i) * 2 );
            }
            sum += intList.get(i);
        }
        return sum;
    }

    public int addCheckSum(StringBuilder accountNumber) {
        ArrayList<String> accountList = new ArrayList<>(Arrays.asList(accountNumber.toString().split("")));
        int sum = luhnSum(accountList);
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
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
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

    public int getKey() {
        return this.key;
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