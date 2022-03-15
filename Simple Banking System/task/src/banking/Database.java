package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    String path = "jdbc:sqlite3:";
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

    public Database(String name){
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
                    VALUES(
                """;
    }

}
