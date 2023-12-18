import java.io.BufferedReader;
import java.io.FileReader;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {




    public static void main(String[] args) {

        //I need a class for handeling different users called reviewer and author



        //create a string array that holds the login information
        String[] loginInfo = getLoginInformation();

        String username = loginInfo[0];
        String password = loginInfo[1];


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Include it in your project.");
            return;
        }

        // Establishing the connection
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://pgserver.mau.se:5432/am3594", username, password)) {
            System.out.println("Connected to the database!");

            //I want to retriev the data from the table "student"
            //create a statement
            Statement statement = connection.createStatement();
            //create a resultset
            ResultSet resultSet = statement.executeQuery("SELECT * FROM registration");

            while (resultSet.next()) {
                System.out.printf("%s %s %s %s %s\n",
                        resultSet.getString("number"),
                        resultSet.getString("stu_id"),
                        resultSet.getString("cou_code"),
                        resultSet.getString("date"),
                        resultSet.getString("status"));
            }




        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }



    }

    public static String[] getLoginInformation() {
        //create a try catch block
        String[] loginInfo = new String[2];
        try {


            //create a file reader that reads the textfile on the desktop
            FileReader fr = new FileReader("C:\\Users\\tedlj\\Desktop\\loginInfo.txt");
            //create a buffered reader
            BufferedReader br = new BufferedReader(fr);
            //create a string that reads the first line in the textfile
            loginInfo[0] = br.readLine();
            //create a string that reads the second line in the textfile
            loginInfo[1] = br.readLine();

            //close the readers
            br.close();
            fr.close();
        }
        //catch the exception
        catch (Exception e) {
            //print out the exception
            System.out.println(e);
        }

        return loginInfo;
    }


}
