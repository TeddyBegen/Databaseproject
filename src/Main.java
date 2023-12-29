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
                    String fullname = scanner.nextLine();
                    System.out.println("Input password");
                    String newPassword = scanner.nextLine();
                    DatabaseFunctions.createNewUser(connection, newEmail, fullname, newPassword);
                }
                case 2 -> { // login
                    System.out.println("Login:");
                    scanner.nextLine(); // consume the \n from hitting enter in the menu
                    System.out.println("Enter email:");
                    String email = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    if(DatabaseFunctions.validateLogin(connection, email, password)) {
                        System.out.println("Login successful!");


                        //TODO: here should be a second create thing for if you are a reviewer or author, this should p
                        // probably not be done with a role tag i the database and should instead be someting the user
                        // chooses when creating the account and then the database should be updated accordingly
                        String role = DatabaseFunctions.checkRole(connection, email, password);

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
                                AuthorMenu(connection);
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

    public static void AuthorMenu(Connection connection) throws SQLException {
        printAutorMenu();

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        while (choice != 5){
            switch (choice) {
                case 1 -> { // submit article

                }
                case 2 -> { // list my articles

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


 









