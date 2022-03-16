package banking;
import org.sqlite.SQLiteDataSource;
import java.sql.*;

public class Database {
    String path = "jdbc:sqlite:";
    SQLiteDataSource dataSource = new SQLiteDataSource();
    Connection connection;
    Statement statement;
    String createTable = """
                CREATE TABLE IF NOT EXISTS card(
                    id INTEGER,
                    number TEXT,
                    pin TEXT,
                    balance INTEGER DEFAULT 0)
            """;

    public Database(String name) {
        this.path += name;
        dataSource.setUrl(this.path);

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute(createTable);
        } catch (SQLException e) {
            System.out.println("Connection error");
        }
    }

    public void addBalance(Account userAccount, int income) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, income);
            statement.setString(2, userAccount.getAccountNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(Account userAccount) {
        String sql = "DELETE FROM card WHERE number = ?";
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userAccount.getAccountNumber());
            statement.executeUpdate();
            System.out.println("\nThe account has been closed!");
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer(Account fromAccount, String toAccount, int amount) {
        if (getBalance(fromAccount.getAccountNumber()) < amount) {
            System.out.println("Not enough money!");
        } else {
            String sql = "UPDATE card SET balance = ? WHERE number = ?";
            try {
                connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, getBalance(fromAccount.getAccountNumber()) - amount);
                statement.setString(2, fromAccount.getAccountNumber());
                statement.executeUpdate();

                statement.setInt(1, getBalance(toAccount) + amount);
                statement.setString(2, toAccount);
                statement.executeUpdate();
                System.out.println("Success!\n");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void insert(Account newAccount) {
        String sql = "INSERT INTO card VALUES(?, ?, ?, ?)";
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newAccount.getKey());
            statement.setString(2, newAccount.getAccountNumber());
            statement.setString(3, newAccount.getAccountPIN());
            statement.setInt(4, newAccount.getBalance());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isInDataBase(Account userAccount) {
        return accountFound("SELECT * FROM card WHERE number = %s AND pin = %s"
                .formatted(userAccount.getAccountNumber(), userAccount.getAccountPIN()));
    }

    public boolean isInDataBase(String accountNumber) {
        return accountFound("SELECT * FROM card WHERE number = %s".formatted(accountNumber));
    }

    public boolean accountFound(String sqlQuery) {
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            connection.close();
            return result.isBeforeFirst();
        }  catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getBalance(String accountNumber) {
        String sql = """
                    SELECT balance FROM card WHERE number = ?
                """;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, accountNumber);
                int balance = statement.executeQuery().getInt("balance");
                connection.close();
                return balance;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}