package banking;
import java.util.*;

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
        accounts.add(newAccount);
    }

    public void logInAccount() {
        System.out.println("\nEnter your card number:");
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
                userAccount.setSubMenu(Integer.parseInt(scanner.nextLine()));

                switch (userAccount.menu) {
                    case BALANCE -> System.out.println(userAccount.getBalance());
                    case LOGOUT -> userAccount.logOut();
                    case EXIT -> {
                        this.mainMenu = MainMenu.EXIT;
                        System.out.println("Bye!");
                    }
                }
            } while (userAccount.isLoggedIn() && mainMenu != MainMenu.EXIT);
        }
    }
}
