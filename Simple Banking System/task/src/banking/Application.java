package banking;
import java.util.*;

class Application {
    enum MainMenu {
        CREATE(1),
        LOGIN(2),
        EXIT(0);
        final int state;
        MainMenu(int input) {
            this.state = input;
        }
    }
    static Scanner scanner;
    MainMenu mainMenu;
    Database db;

    public Application(String fileName) {
        scanner = new Scanner(System.in);
        db = new Database(fileName);
        printMenu();
        mainMenu = setState();
    }

    public MainMenu setState() {
        int input = Integer.parseInt(scanner.nextLine());
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
        Account newAccount = new Account();
        System.out.printf("\nYour card has been created\nYour card number:\n%d\nYour card PIN:\n%s\n",
                Long.parseLong(newAccount.getAccountNumber()), newAccount.getAccountPIN());
        db.insert(newAccount);
    }

    public boolean accountIsVerifiedForTransfer(String fromAccount, String toAccount) {
        if (fromAccount.equals(toAccount)) {
            System.out.println("You can't transfer money to the same account!");
            return false;
        }
        if (!passesLuhnAlgorithm(toAccount)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
            return false;
        }
        if (!db.isInDataBase(toAccount)) {
            System.out.println("Such a card does not exist.\n");
            return false;
        }
        return true;
    }

    public boolean passesLuhnAlgorithm(String accountNumber) {
        ArrayList<String> accountNumberAsList = new ArrayList<>(Arrays.asList(accountNumber.split("")));
        return Account.luhnSum(accountNumberAsList) % 10 == 0;
    }

    public void logInAccount() {
        System.out.println("\nEnter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        Account userAccount = new Account(number, pin);

        if (!db.isInDataBase(userAccount)) {
            System.out.println("Wrong card number or PIN!");
        } else {
            userAccount.logIn();
            accountMenu(userAccount);
        }
    }

    public void accountMenu(Account userAccount) {
        do {
            userAccount.printSubMenu();
            userAccount.setSubMenu(Integer.parseInt(scanner.nextLine()));

            switch (userAccount.menu) {
                case BALANCE -> System.out.printf("Balance: %d\n", userAccount.getBalance());
                case INCOME -> {
                    System.out.println("Enter income:");
                    db.addBalance(userAccount, Integer.parseInt(scanner.nextLine()));
                }
                case TRANSFER -> {
                    System.out.println("\nTransfer\nEnter card number:");
                    String toAccount = scanner.nextLine();
                    if (accountIsVerifiedForTransfer(userAccount.getAccountNumber(), toAccount)) {
                        System.out.println("Enter how much money you want to transfer:");
                        db.transfer(userAccount, toAccount, Integer.parseInt(scanner.nextLine()));
                    }
                }
                case CLOSE -> {
                    db.deleteAccount(userAccount);
                    userAccount.logOut();
                }
                case LOGOUT -> userAccount.logOut();
                case EXIT -> {
                    this.mainMenu = MainMenu.EXIT;
                    System.out.println("Bye!");
                }
            }
        } while (userAccount.isLoggedIn() && mainMenu != MainMenu.EXIT);
    }
}