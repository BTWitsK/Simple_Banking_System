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

    public void insert(Account newAccount) {
        String sql = """
                INSERT INTO card
                    VALUES(%d, %s, %s, %d)
                """.formatted(newAccount.getKey(), newAccount.getAccountNumber(),
                newAccount.getAccountPIN(), newAccount.getBalance());

        try {
            connection = dataSource.getConnection();
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isInDataBase(Account userAccount) {
        String sql = """
                SELECT * FROM card
                WHERE number = %s AND pin = %s
                """.formatted(userAccount.getAccountNumber(), userAccount.getAccountPIN());

        try {
            connection = dataSource.getConnection();
            try {
                statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql);
                return result.isBeforeFirst();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }  catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
