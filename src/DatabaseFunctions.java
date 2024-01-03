import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

//This class contains all the functions that interact with the database to make the code in the main class more readable



public class DatabaseFunctions {

    static String username = getDatabaseLogin(0);
    static String password = getDatabaseLogin(1);

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


    static void createNewUser(Connection connection, String email, String fullname, String password, String phone) {

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO useraccount(email, fullname, userpassword, phonenumber) VALUES('" + email + "','" + fullname + "','" + password + "','" + phone +"')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }

    }

    static void createNewAuthor(Connection connection, int userID, String affiliation) {

        System.out.println("INSERT INTO Author(UserId, Affiliation) VALUES('" + userID + "','" + affiliation + "')" );

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Author(UserId, Affiliation) VALUES('" + userID + "','" + affiliation + "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static void printListOfArticles(Connection connection, int id) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM article WHERE userID = '" + id + "'");

            while(resultSet.next()) {
                System.out.printf("%s || %s || %s\n" , resultSet.getString("title"), resultSet.getString("articletype"), resultSet.getString("keywords"));

            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }



    static void createArticle(Connection connection, String title, String articletype, String text, String keywords, int id) {
        // Inserting the article with an array literal for articletype
        String insertQuery = "INSERT INTO article(userID, title, articletype, articletext, keywords) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, articletype);
            preparedStatement.setString(4, text);
            preparedStatement.setArray(5, connection.createArrayOf("text", keywords.split(","))); // Assuming keywords is a comma-separated string
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Error inserting article: " + e.getMessage());
        }



        // Print the table
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM article");

            while (resultSet.next()) {
                System.out.printf("%s || %s || %s || %s\n", resultSet.getString("title"),
                        resultSet.getArray("articletype"), resultSet.getString("articletext"),
                        resultSet.getString("keywords"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving articles: " + e.getMessage());
        }
    }

    static void createNewReviewer(Connection connection, int userID, String researchArea) {

        System.out.println("INSERT INTO Reviewer(UserId, researchArea) VALUES('" + userID + "','" + researchArea + "')" );

        try(Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Reviewer(UserId, researchArea) VALUES('" + userID + "','" + researchArea + "')" );

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static void createNewAdmin(Connection connection, int userID) {

        System.out.println("UPDATE UserAccount SET AdminUser TRUE" );

        try (
                // Create a prepared statement for the SQL update query
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE UserAccount SET AdminUser ='TRUE' WHERE UserId ='"  + userID + "';"
                )
        ) {
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM useraccount");

            while(resultSet.next()) {
                System.out.printf("%s || %s || %s\n" , resultSet.getString("email"), resultSet.getString("fullname"), resultSet.getString("userpassword"));

            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static boolean validateLogin(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM useraccount WHERE email = '" + email + "' AND userpassword = '" + password + "'");
            if(resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return false;
    }

    static int getUserID(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT UserID FROM useraccount WHERE email = '" + email + "' AND userpassword = '" + password + "'");

            if(resultSet.next()) {
                return resultSet.getInt("UserID");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return 0;
    }

    static String checkRole(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Role FROM useraccount WHERE email = '" + email + "' AND userpassword = '" + password + "'");

            if(resultSet.next()) {
                return resultSet.getString("Role");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    //TODO: This should be a sepeparate class
    private static String getDatabaseLogin(int x) {
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
