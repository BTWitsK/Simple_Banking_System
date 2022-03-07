package banking;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();

        do {
            app.setState();

            switch(app.mainMenu) {
                case CREATE -> app.createAccount();
                case LOGIN -> app.logInAccount();
                case EXIT -> System.out.println("Bye!");
            }

        } while (app.mainMenu != Application.MainMenu.EXIT);
    }
}