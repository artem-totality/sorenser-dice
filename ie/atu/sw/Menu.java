package ie.atu.sw;

import java.util.Scanner;

/**
 * Class for creating and printing main console-based menu
 * 
 */

public class Menu {

    /**
     * Method prints out the menu in the loop
     */

    public static void performMenu() {
        Scanner s = new Scanner(System.in);

        while (true) {
            ConsoleIO.clearConsole();
            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*         Computing the Sørensen-Dice similarity           *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(Q) Quit");

            // Output a menu of options and solicit text from the user
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-?]> ");

            // Read user choice
            var choice = s.nextLine();
            ConsoleIO.clearConsole();

            // Perform user choice
            switch (choice) {
                case "Q":
                    System.out.println("Bye, bye!");
                    s.close();
                    return;
                // if was entered wrong option ask to repeat the process
                default:
                    System.out.println("Please input number between 1 and X or Q for quit!");
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
            }
        }
    }
}
