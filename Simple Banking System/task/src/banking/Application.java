package banking;
import java.util.*;

public class Account {
    enum subMenu {
        BALANCE(1),
        LOGOUT(2),
        EXIT(0);

        int input;

        subMenu(int input) {
            this.input = input;
        }
    }
    Random random = new Random(29);
    private StringBuilder accountNumber;
    private final String accountPIN;
    private boolean loggedIn = false;
    private int balance;
    subMenu menu;

    Account() {
        this.accountPIN = String.valueOf(random.ints(4));
        this.accountNumber = this.accountNumber.append("400000").append(random.ints(9));
        this.balance = 0;
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
        System.out.println("You have successfully logged in!");
    }

    public void logOut() {
        this.loggedIn = false;
        System.out.println("You have successfully logged out!");
    }
}

class Application {
    enum MainMenu {
        CREATE(1),
        LOGIN(2),
        EXIT(0);

        int state;

        MainMenu(int input) {
            this.state = input;
        }
    }

    static Scanner scanner;
    MainMenu mainMenu;
    ArrayList<Account> accounts;

    public Application() {
        scanner = new Scanner(System.in);
        accounts = new ArrayList<>();
    }

    public MainMenu setState(int input) {
        for (MainMenu currentState : MainMenu.values()) {
            if (currentState.state == input) {
                return currentState;
            }
        }
        return null;
    }

    public void printMenu() {
        System.out.print(""" 
                1. Create an account
                2. Log into account
                0. Exit
               
                """);
    }

    public void createAccount() {
        accounts.add(new Account());
    }

    public void logInAccount() {
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        Account userAccount = new Account(number, pin);

        if (!accounts.contains(userAccount)) {
            System.out.println("Wrong card number or PIN!");
        } else {
            userAccount.logIn();

            do {
                userAccount.printSubMenu();
                userAccount.setSubMenu(scanner.nextInt());

                switch (userAccount.menu) {
                    case BALANCE -> System.out.println(userAccount.getBalance());
                    case LOGOUT -> userAccount.logOut();
                    case EXIT -> this.mainMenu = MainMenu.EXIT;
                }
            } while (userAccount.isLoggedIn());
        }

    }
}
