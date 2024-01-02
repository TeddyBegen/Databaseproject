import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

//This class contains all the functions that interact with the database to make the code in the main class more readable



public class DatabaseFunctions {

    //static String username = getLoginInfo(0);
    //tatic String password = getLoginInfo(1);

    static String usernameSamuel = "am2701";
    static String passwordSamuel = "0oo0mggp";


    static Connection ConnectToDatabase() throws SQLException {
        //create a try catch block

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Include it in your project.");
        }

        Connection connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/am2701", usernameSamuel, passwordSamuel);
        System.out.println("Connected to the database: pgserver.mau.se:5432/am2701");

        return connection;
    }

    static String getUserCount(Connection connection){

        String sqlQuery = "SELECT COUNT(*) FROM UserAccount";
        int rowCount = 0;
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            resultSet.next();
            rowCount = resultSet.getInt(1);

            // Print the row count
            System.out.println("Row count: " + rowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(rowCount);
    }

    static void createNewUser(Connection connection, String userID, String username, String userpassword, String fullname, String phonenumber, String email) {

        System.out.println("INSERT INTO UserAccount(UserId, Username, UserPassword, FullName, PhoneNumber, Email) VALUES('" + username + "','" + userpassword + "','" + fullname + "','" + phonenumber + "','" + email + "')" );

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO UserAccount(UserId, Username, UserPassword, FullName, PhoneNumber, Email) VALUES('" + userID + "','" + username + "','" + userpassword + "','" + fullname + "','" + phonenumber + "','" + email + "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }

    }

    static void createNewAuthor(Connection connection, String userID, String affiliation) {

        System.out.println("INSERT INTO Author(AuthorId, Affiliation) VALUES('" + userID + "','" + affiliation + "')" );

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Author(AuthorId, Affiliation) VALUES('" + userID + "','" + affiliation + "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static void createNewReviewer(Connection connection, String userID, String researchArea) {

        System.out.println("INSERT INTO Reviewer(AuthorId, researchArea) VALUES('" + userID + "','" + researchArea + "')" );

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Reviewer(AuthorId, researchArea) VALUES('" + userID + "','" + researchArea + "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    //TODO Fixa denna...
    static void createNewAdmin(Connection connection, String userID) {

        System.out.println("UPDATE UserAccount SET AdminUser TRUE" );

        try (
                // Create a prepared statement for the SQL update query
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE UserAccount SET AdminUser ='TRUE' WHERE UserId ='"  + userID + "';"
                )
        ) {
            // Set the parameters in the prepared statement
            //preparedStatement.setBoolean(1, true);

            // Set the condition for the update
            // For example, updating where another column equals a specific value
            //preparedStatement.setString(2, userID);

            // Execute the update query
            int affectedRows = preparedStatement.executeUpdate();

            // Print the number of affected rows
            System.out.println("Number of affected rows: " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void printListOfUsers(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserAccount");

            while(resultSet.next()) {
                System.out.printf("%s || %s || %s\n" , resultSet.getString("email"), resultSet.getString("fullname"), resultSet.getString("UserPassword"));

            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static boolean validateLogin(Connection connection, String username, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserAccount WHERE Username = '" + username + "' AND UserPassword = '" + password + "'");

            if(resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return false;
    }

    static String checkRole(Connection connection, String username, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Role FROM UserAccount WHERE Username = '" + username + "' AND UserPassword = '" + password + "'");

            if(resultSet.next()) {
                return resultSet.getString("Role");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    /*
    //TODO: This should be a sepeparate class
    private static String getLoginInfo(int x) {
        //create a try catch block
        String[] loginInfo = new String[2];
        try {

            //create a file reader that reads the textfile on the desktop containing the login info
            FileReader fr = new FileReader("C:\\Users\\tedlj\\Desktop\\loginInfo.txt");
            //create a buffered reader
            BufferedReader br = new BufferedReader(fr);
            //create a string that reads the first line in the textfile
            loginInfo[0] = br.readLine();
            //create a string that reads the second line in the textfile
            loginInfo[1] = br.readLine();

            br.close();
            fr.close();
            return loginInfo[x];

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }


        return null;
    }*/

}
