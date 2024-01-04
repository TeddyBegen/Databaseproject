// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

//This class contains the UI printing methods and is responsible for the logic in the project
public class Main {

    public static void main(String[] args) throws SQLException {
        //connect to database
        Connection connection = DatabaseFunctions.ConnectToDatabase();
        startMenu(connection);
    }

    public static void startMenu(Connection connection) throws SQLException {
        printStartMenu();
        //DatabaseFunctions.printListOfUsers(connection);
        String ANSI_RESET = "\u001B[0m";
        String ANSI_ITALIC = "\u001B[3m";
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 3) {
            switch (choice) {
                case 1 -> { // create new user
                    System.out.println("Creating a new user account...");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Input email:");
                    String newEmail = scanner.nextLine();
                    System.out.println("Input password:");
                    String newPassword = scanner.nextLine();
                    System.out.println("Input fullname:");
                    String fullname = scanner.nextLine();
                    System.out.println("Input phonenumber:");
                    String phonenumber = scanner.nextLine();
                    DatabaseFunctions.createNewUser(connection, newEmail, newPassword, fullname, phonenumber);
                    String userID = String.valueOf(DatabaseFunctions.getUserIdByEmail(connection,newEmail));
                    System.out.println();
                    System.out.println("Choose account-type:");
                    System.out.println("1.Author-account    2.Admin-acount" + ANSI_ITALIC + "(admin-key required)" + ANSI_RESET );
                    String accountType = scanner.nextLine();

                    switch (accountType) {
                        case "1" -> {
                            System.out.println("Please enter your affiliation:");
                            String affiliation = scanner.nextLine();
                            DatabaseFunctions.createNewAuthor(connection, userID, affiliation);
                            printStartMenu();
                        }
                        case "2" -> {
                            System.out.println("Please input the secret admin code:");
                            String adminCode = scanner.nextLine();
                            if (adminCode.equals("password123")) {
                                System.out.println("Code correct! Creating admin...");
                                DatabaseFunctions.createNewAdmin(connection, userID);
                                printStartMenu();
                            } else {
                                System.out.println("That is NOT the secret admin code... Admin account was not created.");
                            }
                        }
                        default -> System.out.println("Wrong input...");
                    }
                }
                case 2 -> { // login
                    System.out.println("Login with a user account.");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Enter email:");
                    String email = scanner.nextLine();
                    System.out.println("Enter password:");
                    String userPassword = scanner.nextLine();
                    if(DatabaseFunctions.validateLogin(connection, email, userPassword)) {
                        System.out.println("Login successful!");
                        int userID = DatabaseFunctions.getUserIdByEmail(connection, email);

                        switch (DatabaseFunctions.checkUserIdAndRole(connection,userID)) {
                            case 1: {
                                System.out.println("------ Admin Menu ------");
                                AdminMenu(connection);
                            }
                            case 2: {
                                System.out.println("----- Reviewer Menu -----");
                                ReviewerMenu(connection, userID);
                            }
                            case 3: {
                                System.out.println("------ Author Menu ------");
                                AuthorMenu(connection, userID);
                            }
                        }
                    } else {
                        System.out.println("Login failed. Invalid email or password.");
                        System.out.println("Login with a user account.");
                    }
                }
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }
        System.out.println("Exiting!");
    }

    public static void AdminMenu(Connection connection) throws SQLException {
        printAdminMenu();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 6){
            switch (choice) {
                case 1 -> { // add open submission period
                    System.out.println("Create a submission-period...");
                    DatabaseFunctions.createSubmissionPeriod(connection);
                }
                case 2 -> { // add a reviewer
                    System.out.println("Add a reviewer-account...");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Input email:");
                    String newEmail = scanner.nextLine();
                    System.out.println("Input password:");
                    String newPassword = scanner.nextLine();
                    System.out.println("Input fullname:");
                    String fullname = scanner.nextLine();
                    System.out.println("Input phonenumber:");
                    String phonenumber = scanner.nextLine();
                    DatabaseFunctions.createNewUser(connection, newEmail, newPassword, fullname, phonenumber);
                    String userID = String.valueOf(DatabaseFunctions.getUserIdByEmail(connection,newEmail));
                    System.out.println("Please enter your researchArea:");
                    String researchArea = scanner.nextLine();
                    DatabaseFunctions.createNewReviewer(connection, userID, researchArea);
                    printAdminMenu();
                }
                case 3 -> { // remove a reviewer
                    DatabaseFunctions.printListOfReviewers(connection);
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Input UserID-number for which reviewer you want to remove:");
                    int userID = Integer.parseInt(scanner.nextLine());
                    DatabaseFunctions.removeReviewer(connection, userID);
                    System.out.println("Reviewer removed.");
                    DatabaseFunctions.printListOfReviewers(connection);
                    printAdminMenu();
                }
                case 4 -> { // search submitted articles
                    DatabaseFunctions.printSubmittedArticles( connection);
                }
                case 5 -> {
                    System.out.println("Printing out a list of submitted articles and available reviewers... \n ");
                    DatabaseFunctions.printAssignmentList(connection);
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Input ArticleID-number for which article to be reviewed:");
                    String articleID = scanner.nextLine();
                    System.out.println("Input UserID-number for the first reviewer you want to use for the article:");
                    String userID1 = scanner.nextLine();
                    DatabaseFunctions.createArticleReview(connection, userID1, articleID);
                    System.out.println("Input UserID-number for the second reviewer you want to use for the article:");
                    String userID2 = scanner.nextLine();
                    DatabaseFunctions.createArticleReview(connection, userID2, articleID);
                    System.out.println("\nReview assignment created!.");
                }
                case 6 -> {
                    System.out.println("Exiting back to main menu...");

                }
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }
        System.out.println("Exiting!");
    }

    public static void ReviewerMenu(Connection connection, int userID) throws SQLException {
        printReviewMenu();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 3) {
            switch (choice) {
                case 1 -> {// see list of articles pending review
                    System.out.println("List of articles pending review:");
                    DatabaseFunctions.printListOfArticles(connection, userID);
                }
                case 2 -> {// review article
                    System.out.println("Choose article to review:");
                    scanner = new Scanner(System.in);
                    System.out.print("Enter Article ID: ");
                    int articleId = scanner.nextInt();
                    DatabaseFunctions.reviewArticle(connection, articleId, userID);
                }
                case 3 -> {System.out.println("Exiting back to main menu...");}
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }
    }

    public static void AuthorMenu(Connection connection, int id) throws SQLException {
        printAutorMenu();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 4){
            switch (choice) {
                case 1 -> { // submit article
                    if (DatabaseFunctions.checkIfInSubmissionPeriod(connection)) {
                        System.out.println("You are inside the submission period.");
                        System.out.println("Input title:");
                        scanner.nextLine(); // consume the \n from hitting enter in the menu
                        String title = scanner.nextLine();
                        System.out.println("Titel: " + title);
                        System.out.println("Type of aricle:  1.Short article   2.Full article   3.Poster]");
                        int inputType = scanner.nextInt();
                        String type = "Undefined";
                        switch (inputType) {
                            case 1 ->
                                type = "short article";
                            case 2 ->
                                type = "Full article";
                            case 3 ->
                                type = "Poster";
                        }
                        System.out.println("Type: " + type);
                        System.out.println("Input text:");
                        String text = scanner.nextLine();
                        System.out.println("Text: " + text);
                        System.out.println("Input keyword:");
                        String keywords = scanner.nextLine();
                        System.out.println("Keywords: " + keywords);
                        DatabaseFunctions.createArticle(connection, title, type, text, keywords, id);
                    }
                    else {System.out.println("Sorry but you can not submit a article outside submission period...");}
                }
                case 2 -> { // list my articles AND COMMENTS
                    DatabaseFunctions.printAuthorsArticles(connection, id);
                }
                case 3 -> {
                    System.out.println("check submission date"); // list my articles
                    DatabaseFunctions.checkSubmissionDate(connection);; // list my articles
                }


                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }
    }

    private static void printAutorMenu() {
        System.out.println("1. Submit article"); //should also print submission period dates
        System.out.println("2. List my articles");
        System.out.println("3. Check submission period");
        System.out.println("4. Exit");
    }

    private static void printReviewMenu() {
        System.out.println("1. See list of articles pending review");
        System.out.println("2. Review article");
        System.out.println("3. Exit");
    }

    private static void printAdminMenu() {
        System.out.println("1. Add open submission period");
        System.out.println("2. Add a reviewer");
        System.out.println("3. Remove a reviewer");
        System.out.println("4. Search submitted articles");
        System.out.println("5. Assign reviewers to a submitted articles");
        System.out.println("6. Exit");
    }

    private static void printStartMenu() {
        System.out.println("1. Create new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }
}


 









