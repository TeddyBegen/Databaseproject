// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        //connect to database

         Connection connection = DatabaseFunctions.ConnectToDatabase();

        //create a scanner
        Scanner scanner = new Scanner(System.in);

        printMenu();

        int choice = scanner.nextInt();

        while (choice != 5) {
            switch (choice) {
                case 1 -> {
                    System.out.println("Input email");
                    scanner.nextLine(); //consume the \n from htting enter in the menu
                    String email = scanner.nextLine();
                    System.out.println("Input fullname");
                    String fullname = scanner.nextLine();
                    System.out.println("Input password");
                    String password = scanner.nextLine();
                    DatabaseFunctions.createNewUser(connection, email, fullname, password);
                }
                case 2 -> {
                    System.out.println("Delete user");
                    DatabaseFunctions.deleteUser();
                }
                case 3 -> {
                    System.out.println("Update user");
                    DatabaseFunctions.editUser();
                }

                //TODO: make admin only function
                case 4 -> {
                    System.out.println("Print list of users");
                    DatabaseFunctions.printListOfUsers(connection);
                }
                default -> System.out.println("Invalid choice, try again!");
            }
            printMenu();
            choice = scanner.nextInt();
        }



    }


    public static void printMenu() {
        System.out.println("1. Create new user");
        System.out.println("2. Delete user");
        System.out.println("3. Update user");
        System.out.println("4. User list");
        System.out.println("5. Exit");

    }



}

 









