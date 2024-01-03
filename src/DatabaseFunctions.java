import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.Scanner;

//This class contains all the functions that interact with the database to make the code in the main class more readable


public class DatabaseFunctions {

    //static String username = getLoginInfo(0);
    //static String password = getLoginInfo(1);

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

    static String getUserCount(Connection connection) {

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

    static void createNewUser(Connection connection, String email, String userpassword, String fullname, String phonenumber) {

        System.out.println("INSERT INTO UserAccount( Email, UserPassword, FullName, PhoneNumber) VALUES('" + email + "','" + userpassword + "','" + fullname + "','" + phonenumber + "')");

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO UserAccount( Email, UserPassword, FullName, PhoneNumber) VALUES('" + email + "','" + userpassword + "','" + fullname + "','" + phonenumber + "')");

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }

    }

    static void createNewAuthor(Connection connection, String userID, String affiliation) {

        System.out.println("INSERT INTO Author(UserId, Affiliation) VALUES('" + userID + "','" + affiliation + "')");

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Author(UserId, Affiliation) VALUES('" + userID + "','" + affiliation + "')");

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Samuels special-kod
     */
    String ANSI_RESET = "\u001B[0m";
    String ANSI_ITALIC = "\u001B[3m";

    static void createArticle(Connection connection, String title, String articletype, String text, String keywords, int id) {

        // Inserting the article with an array literal for articletype
        String insertQuery = "INSERT INTO article(userID, title, articletype, articletext, keywords, articleStatus, submissionDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, articletype);
            preparedStatement.setString(4, text);
            preparedStatement.setArray(5, connection.createArrayOf("text", keywords.split(","))); // Assuming keywords is a comma-separated string
            preparedStatement.setInt(1, id);
            preparedStatement.setString(6, "submitted");

            /** IF WE WANT TO GIVE THE DATE MANUALLY
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
             Scanner scanner = new Scanner(System.in);
             System.out.print("Enter a date (yyyy-MM-dd): ");
             String dateString = scanner.next();
             Date date = dateFormat.parse(dateString);
             java.sql.Date sqlDate = new java.sql.Date(date.getTime()); */

            //Inserting a current date value below :)
            LocalDate currentDate = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
            preparedStatement.setDate(7, sqlDate);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Error inserting article: " + e.getMessage());
        }


        // Print the table
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM article");

            while (resultSet.next()) {
                System.out.printf("%s || %s || %s || %s || %s || %s\n", resultSet.getString("title"),
                        resultSet.getArray("articletype"), resultSet.getString("articletext"),
                        resultSet.getString("keywords"), resultSet.getString("articleStatus"),
                        resultSet.getDate("submissionDate"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving articles: " + e.getMessage());
        }
    }

    static void printSubmittedArticles(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Article WHERE ArticleStatus = 'submitted'")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf("ArticleId: %d | UserID: %d | Title: %s | ArticleType: %s | " +
                                "ArticleText: %s | Keywords: %s | ReviewerId_1: %d | " +
                                "ReviewerId_2: %d | ReviewerStatus_1: %s | ReviewerStatus_2: %s | " +
                                "ArticleStatus: %s | SubmissionDate: %s | ConferenceID: %d\n",
                        resultSet.getInt("ArticleId"),
                        resultSet.getInt("UserID"),
                        resultSet.getString("Title"),
                        resultSet.getString("ArticleType"),
                        resultSet.getString("ArticleText"),
                        resultSet.getString("Keywords"),
                        resultSet.getInt("ReviewerId_1"),
                        resultSet.getInt("ReviewerId_2"),
                        resultSet.getString("ReviewerStatus_1"),
                        resultSet.getString("ReviewerStatus_2"),
                        resultSet.getString("ArticleStatus"),
                        resultSet.getDate("SubmissionDate"),
                        resultSet.getInt("ConferenceID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createNewReviewer(Connection connection, String userID, String researchArea) {

        System.out.println("INSERT INTO Reviewer(UserId, researchArea) VALUES('" + userID + "','" + researchArea + "')");

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate("INSERT INTO Reviewer(UserId, researchArea) VALUES('" + userID + "','" + researchArea + "')");

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static void removeReviewer(Connection connection, int userID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Reviewer WHERE userid = ?")) {
            // Set the parameter in the prepared statement as an integer
            preparedStatement.setInt(1, userID);

            // Execute the delete query
            int affectedRows = preparedStatement.executeUpdate();

            System.out.println("Number of affected rows (Reviewer): " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM UserAccount WHERE userid = ?")) {
            // Set the parameter in the prepared statement as an integer
            preparedStatement.setInt(1, userID);

            // Execute the delete query
            int affectedRows = preparedStatement.executeUpdate();

            System.out.println("Number of affected rows (UserAccount): " + affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createNewAdmin(Connection connection, String userID) {

        System.out.println("UPDATE UserAccount SET AdminUser TRUE");

        try (
                // Create a prepared statement for the SQL update query
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE UserAccount SET AdminUser ='TRUE' WHERE UserId ='" + userID + "';"
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

    static void checkSubmissionDate(Connection connection){
        String selectQuery = "SELECT startdate ANC enddate FROM SubmissionPeriod";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.printf("%s || %s\n", resultSet.getString("startdate"), resultSet.getString("enddate"));
            }
            else {
                System.out.println("No submission period found");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving articles: " + e.getMessage());

        }

    }

    static void createSubmissionPeriod(Connection connection) {

        String insertQuery = "INSERT INTO SubmissionPeriod(StartDate, EndDate) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter start date (yyyy-mm-dd): ");
            String dateString1 = scanner.next();
            Date date1 = dateFormat.parse(dateString1);
            java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());

            System.out.print("Enter end date (yyyy-mm-dd): ");
            String dateString2 = scanner.next();
            Date date2 = dateFormat.parse(dateString2);
            java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());

            preparedStatement.setDate(1, sqlDate1);
            preparedStatement.setDate(2, sqlDate2);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException | ParseException e) {
            System.err.println("Error inserting article: " + e.getMessage());
        }
    }

    static void printListOfUsers(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserAccount");

            while (resultSet.next()) {
                System.out.printf("%s || %s || %s\n", resultSet.getString("email"), resultSet.getString("fullname"), resultSet.getString("UserPassword"));

            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static void printListOfReviewers(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT ua.UserId, r.ResearchArea, ua.Email, ua.FullName, ua.UserPassword " +
                        "FROM UserAccount ua JOIN Reviewer r ON ua.UserId = r.UserId")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf("%s || %s || %s || %s || %s\n",
                        resultSet.getString("UserId"),
                        resultSet.getString("ResearchArea"),
                        resultSet.getString("FullName"),
                        resultSet.getString("Email"),
                        resultSet.getString("UserPassword"));
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    static boolean validateLogin(Connection connection, String email, String password) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserAccount WHERE Email = '" + email + "' AND UserPassword = '" + password + "'");

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return false;
    }

    /*static String checkRole(Connection connection, String email, String password) {
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Role FROM UserAccount WHERE Email = '" + username + "' AND UserPassword = '" + password + "'");

            if(resultSet.next()) {
                return resultSet.getString("Role");
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }*/

    public static int checkUserIdAndRole(Connection connection, int userId) {
        boolean adminIdExists = checkTableForUserId(connection, "UserAccount", userId);
        boolean reviewerIdExists = checkTableForUserId(connection, "Reviewer", userId);
        boolean authorIdExists = checkTableForUserId(connection, "Author", userId);

        // Check the conditions: UserId should exist, and either ReviewerId or AuthorId should exist, but not both.
        //return userIdExists && (reviewerIdExists || authorIdExists) && !(reviewerIdExists && authorIdExists);
        int intCase = 0;
        if (adminIdExists) {
            intCase = 1;
        }
        if (reviewerIdExists) {
            intCase = 2;
        }
        if (authorIdExists) {
            intCase = 3;
        }

        return intCase;
    }

    private static boolean checkTableForUserId(Connection connection, String tableName, int userId) {

        if (tableName.equals("UserAccount")) {
            boolean adminUserValue = false; // Default value in case the user is not found

            String sqlQuery = "SELECT AdminUser FROM UserAccount WHERE UserId = ?";

            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
            ) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        adminUserValue = resultSet.getBoolean("AdminUser");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return adminUserValue;
        }

        boolean userIdExists = false;
        String sqlQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE UserId = ?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                userIdExists = count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIdExists;
    }

    public static int getUserIdByEmail(Connection connection, String email) {
        int userId = -1; // Default value in case the user is not found

        String sqlQuery = "SELECT UserId FROM UserAccount WHERE Email = ?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("UserId");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
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
