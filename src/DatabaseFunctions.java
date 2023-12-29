import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

//This class contains all the functions that interact with the database to make the code in the main class more readable



public class DatabaseFunctions {

    static String username = getLoginInfo(0);
    static String password = getLoginInfo(1);

    static Connection ConnectToDatabase() throws SQLException {
        //create a try catch block

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Include it in your project.");
        }

        Connection connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/am3594", username, password);
        System.out.println("Connected to the database!");

        return connection;
    }


    static void createNewUser(Connection connection, String email, String fullname, String password) {

        System.out.println("INSERT INTO enduser(email, fullname, password) VALUES (" + email + ',' + fullname + ',' + password +  ")");

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO enduser(email, fullname, password) VALUES('" + email + "','" + fullname + "','" + password +  "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }

    }

    static void printListOfUsers(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM enduser");

            while(resultSet.next()) {
                System.out.printf("%s || %s || %s\n" , resultSet.getString("email"), resultSet.getString("fullname"), resultSet.getString("password"));

            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static boolean validateLogin(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM enduser WHERE email = '" + email + "' AND password = '" + password + "'");

            if(resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return false;
    }

    static String checkRole(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Role FROM enduser WHERE email = '" + email + "' AND password = '" + password + "'");

            if(resultSet.next()) {
                return resultSet.getString("Role");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

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
    }

}
