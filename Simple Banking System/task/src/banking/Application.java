package banking;
import java.util.*;

public class Application {
    enum MainMenu {
        CREATE(1),
        LOGIN(2),
        EXIT(0);

        int state;

        MainMenu(int input) {
            this.state = input;
        }

    }

    static Scanner scanner = new Scanner(System.in);
    MainMenu mainMenu;
    ArrayList<Account> accounts = new ArrayList<>();

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
        }

    }
}
