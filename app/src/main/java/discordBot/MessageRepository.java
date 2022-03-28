package discordBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageRepository {
    private static int messageRepositorySize;
    private static final String CONNECTION_STRING_FILE = "jdbc:h2:./msgtable_db";

    public static boolean createTable() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS MSGTABLE");
            statement.execute("CREATE TABLE MSGTABLE " +
                    "(ID BIGINT AUTO_INCREMENT, " +
                    "DISCORDID VARCHAR(255), " +
                    "USERNAME VARCHAR(255), " +
                    "MESSAGECOUNT INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public User create(User user) {

        Connection connection = null;
        String discordID = user.getDiscordId();

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statementToCreate = connection.prepareStatement("INSERT INTO MSGTABLE (DISCORDID, USERNAME, MESSAGECOUNT) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statementToCreate.setString(1, user.getDiscordId());
            statementToCreate.setString(2, user.getUserName());
            statementToCreate.setInt(3, user.getMsgCount());

            // Creates user in database
            // first instance gets id 1 and auto-increments by one with every new instance
            // username is authors username - this is an issue since usernames are not unique
            // messagecount default 1 as author is put into database at first message.
            // author is first created, then saved in database and fields for id and messagecount are updated after creation with data from database

            statementToCreate.execute();

            var resultSetCreate = statementToCreate.getGeneratedKeys();
            if (resultSetCreate.next()) {
                user.setDatabaseId(resultSetCreate.getInt(1));
            }
            var statementToRead = connection.prepareStatement("SELECT ID, DISCORDID, USERNAME, MESSAGECOUNT FROM MSGTABLE WHERE DISCORDID = ?");
            statementToRead.setString(1, user.getDiscordId());
            var resultSetRead = statementToRead.executeQuery();

            if (resultSetRead.next()) {
                user = new User(discordID);
                user.setDatabaseId(resultSetRead.getInt(1));
                user.setDiscordId(resultSetRead.getString(2));
                user.setUserName(resultSetRead.getString(3));
                user.setMsgCount(resultSetRead.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
                System.out.println("User " + user.getUserName() + " with Discord ID " + user.getDiscordId() + " was saved in database with database id " + user.getDatabaseId());
                System.out.print("Message count of database id " + user.getDatabaseId() + " is currently " + user.getMsgCount());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void update(User user) {
        Connection connection = null;
        int updatedMsgCount = user.getMsgCount() + 1;
        System.out.println("Updating user " + user.getDatabaseId() + " " + user.getUserName() + " messagecount from " + user.getMsgCount() + " to " + updatedMsgCount);

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statementToUpdate = connection.prepareStatement("UPDATE MSGTABLE SET MESSAGECOUNT = ? WHERE DISCORDID = ?;");
            statementToUpdate.setInt(1, updatedMsgCount);
            statementToUpdate.setString(2, user.getDiscordId());
            statementToUpdate.executeUpdate();

            var statementToUpdatePOJO = connection.prepareStatement("SELECT MESSAGECOUNT FROM MSGTABLE WHERE DISCORDID = ?");
            statementToUpdatePOJO.setString(1, user.getDiscordId());
            var resultSet = statementToUpdatePOJO.executeQuery();
            if (resultSet.next()) {
                user.setMsgCount(resultSet.getInt(1));
                System.out.println("Database id " + user.getDatabaseId() + " user " + user.getDiscordId() + " " + user.getUserName() + " messagecount updated to " + user.getMsgCount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void printById(String discordId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);
            var statement = connection.prepareStatement("SELECT USERNAME, MESSAGECOUNT FROM MSGTABLE WHERE ID = ?");
            statement.setString(1, discordId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User id " + discordId + " is called " + resultSet.getString(1) + " with messagecount " + resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void printAll() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING_FILE);

            for (int i = 1; i <= messageRepositorySize; i++) {
                var statement = connection.prepareStatement("SELECT USERNAME, MESSAGECOUNT FROM MSGTABLE WHERE ID = ?");
                statement.setInt(1, i);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("User id " + i + " is called " + resultSet.getString(1) + " with messagecount " + resultSet.getInt(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setMessageRepositorySize(int messageRepositorySize) {
        MessageRepository.messageRepositorySize = messageRepositorySize;
    }
}
