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
            try {
                statement = connection.createStatement();
                statement.execute(createTable);
            } catch (SQLException e) {
                System.out.println("Statement error");
            }
        } catch (SQLException e) {
            System.out.println("Connection error");
        }
    }

    public void deleteAccount(Account userAccount) {
        String sql = """
                    DELETE FROM card
                        WHERE number = ?
                """;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userAccount.getAccountNumber());
            statement.executeUpdate();
            System.out.println("\nThe account has been closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transfer(Account fromAccount, String toAccount, int amount) {
        if (fromAccount.getBalance() < amount) {
            System.out.println("Not enough money!");
        } else {
            String sql = """
                        UPDATE card
                            SET balance = ?
                            WHERE number = ?
                    """;
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, fromAccount.getBalance() - amount);
                statement.setString(2, fromAccount.getAccountNumber());
                statement.executeUpdate();

                statement.setInt(1, getBalance(toAccount) + amount);
                statement.setString(2, toAccount);
                statement.executeUpdate();

                connection.commit();
                System.out.println("Success!\n");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void insert(Account newAccount) {
        String sql = """
                INSERT INTO card
                    VALUES(?, ?, ?, ?)
                """;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newAccount.getKey());
            statement.setString(2, newAccount.getAccountNumber());
            statement.setString(3, newAccount.getAccountPIN());
            statement.setInt(4, newAccount.getBalance());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isInDataBase(Account userAccount) {
        String sql = """
                SELECT * FROM card
                WHERE number = %s AND pin = %s
                """.formatted(userAccount.getAccountNumber(), userAccount.getAccountPIN());
        return accountFound(sql);
    }

    public boolean isInDataBase(String accountNumber) {
        String sql = """
                SELECT * FROM card
                WHERE number = %s
                """.formatted(accountNumber);
        return accountFound(sql);

    }

    public boolean accountFound(String sqlQuery) {
        try {
            connection = dataSource.getConnection();
            try {
                statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sqlQuery);
                return result.isBeforeFirst();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, accountNumber);

            return statement.executeQuery().getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }
}