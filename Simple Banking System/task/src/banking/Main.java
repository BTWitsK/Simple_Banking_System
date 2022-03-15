package banking;

public class Main {
    public static void main(String[] args) {
        Application app = new Application(args[1]);

        do {
            switch(app.mainMenu) {
                case CREATE -> app.createAccount();
                case LOGIN -> app.logInAccount();
                case EXIT -> System.out.println("Bye!");
            }
            if (app.mainMenu != Application.MainMenu.EXIT) {
                app.printMenu();
                app.mainMenu = app.setState();
            }

        } while (app.mainMenu != Application.MainMenu.EXIT);
    }
}