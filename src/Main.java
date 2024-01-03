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

        DatabaseFunctions.printListOfUsers(connection);

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 3) {
            switch (choice) {
                case 1 -> { // create new user
                    System.out.println("Input email");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    String newEmail = scanner.nextLine();

                    System.out.println("Input fullname");
                    String fullName = scanner.nextLine();

                    System.out.println("Input password");
                    String password = scanner.nextLine();

                    System.out.println("Input phone");
                    String phone = scanner.nextLine();

                    DatabaseFunctions.createNewUser(connection, newEmail, fullName, password, phone);

                    int userID = DatabaseFunctions.getUserID(connection, newEmail, password);


                    System.out.println("Choose account-type:");
                    System.out.println("1. Author   2. Reviewer   3. Admin:");
                    String accountType = scanner.nextLine();

                    if(accountType.equals("1")){
                        System.out.println("Please enter your affiliation:");
                        String affiliation = scanner.nextLine();
                        DatabaseFunctions.createNewAuthor(connection, userID, affiliation);
                    }
                    else if (accountType.equals("2")){
                        System.out.println("Please enter your researchArea:");
                        String researchArea = scanner.nextLine();
                        DatabaseFunctions.createNewReviewer(connection, userID, researchArea);
                    }
                    else if (accountType.equals("3")){
                        System.out.println("Creating admin...");
                        //String researchArea = scanner.nextLine();
                        DatabaseFunctions.createNewAdmin(connection, userID);
                    }
                    else {System.out.println("Wrong input...1");}
                }
                case 2 -> { // login
                    System.out.println("Login:");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Enter email:");
                    String email = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    if(DatabaseFunctions.validateLogin(connection, email, password)) {

                        int userID = DatabaseFunctions.getUserID(connection, email, password);
                        System.out.println("Login successful!");

                        //TODO: here should be a second create thing for if you are a reviewer or author, this should p
                        // probably not be done with a role tag i the database and should instead be someting the user
                        // chooses when creating the account and then the database should be updated accordingly
                        //String role = DatabaseFunctions.checkRole(connection, email, password);
                        String role = "Author";
                        switch (Objects.requireNonNull(role)) {
                            case "Admin" -> {
                                System.out.println("You are an admin");
                                AdminMenu(connection);
                            }
                            case "Reviewer" -> {
                                System.out.println("You are a reviewer");
                                ReviewerMenu(connection);
                            }
                            case "Author" -> {
                                System.out.println("You are an author");
                                AuthorMenu(connection, userID);
                            }
                        }


                    } else {
                        System.out.println("Login failed. Invalid email or password.");
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

        while (choice != 5){
            switch (choice) {
                case 1 -> { // add open submission period

                }
                case 2 -> { // add a reviewer

                }
                case 3 -> { // remove a reviewer

                }
                case 4 -> { // search submitted articles

                }
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }
        System.out.println("Exiting!");


    }

    public static void ReviewerMenu(Connection connection) throws SQLException {
        printReviewMenu();

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 3){
            switch (choice) {
                case 1 -> // see list of articles pending review
                        System.out.println("List of articles pending review:");
                case 2 -> // review article
                        System.out.println("Choose article to review:");
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }

    }

    public static void AuthorMenu(Connection connection, int id) throws SQLException {
        printAutorMenu();

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 5){
            switch (choice) {
                case 1 -> { // submit article
                    System.out.println("Input title");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    String title = scanner.nextLine();
                    System.out.println("titel " + title);
                    System.out.println("Input type");
                    String type = scanner.nextLine();
                    System.out.println("type " + type);
                    System.out.println("Input text");
                    String text = scanner.nextLine();
                    System.out.println("text " + text);
                    System.out.println("Input keywords");
                    String keywords = scanner.nextLine();
                    System.out.println("keywords " + keywords);
                    DatabaseFunctions.createArticle(connection, title, type, text, keywords, id);

                    printStartMenu();
                }
                case 2 -> { // list my articles
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("List of my articles:");
                    DatabaseFunctions.printListOfArticles(connection, id);
                    if (DatabaseFunctions.getNotEmpty()){
                        System.out.println("What article do you want to read?");
                        scanner.nextLine(); // consume the \n from hitting enter in the menu
                        int articleID = scanner2.nextInt();
                        System.out.println("Article: " + articleID);
                        DatabaseFunctions.printArticle(connection, articleID);
                        DatabaseFunctions.setNotEmptyTrue();
                    }

                }
                default -> System.out.println("Invalid choice, try again!");
            }
            choice = scanner.nextInt();
        }

    }

    private static void printAutorMenu() {
        System.out.println("1. Submit article"); //should also print submission period dates
        System.out.println("2. List my articles");
        System.out.println("3. Exit");
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
        System.out.println("5. Exit");
    }

    private static void printStartMenu() {
        System.out.println("1. Create new user");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }



}


 









