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
        var textComparator = new TextComparator();

        while (true) {
            ConsoleIO.clearConsole();
            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*         Computing the Sørensen-Dice similarity           *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(1) Upload Text File A [Current - "
                    + (textComparator.getTextFileAPath() == null ? "Not Set" : textComparator.getTextFileAPath())
                    + "]");
            System.out.println("(3) Upload Stop Words List File [Current - "
                    + (textComparator.getStopWordsFilePath() == null ? "Not Set"
                            : textComparator.getStopWordsFilePath())
                    + "]");
            System.out.println("(S) Get System Status");
            System.out.println("(Q) Quit");

            // Output a menu of options and solicit text from the user
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-?]> ");

            // Read user choice
            var choice = s.nextLine();
            ConsoleIO.clearConsole();

            // Perform user choice
            switch (choice) {
                case "1":
                    textComparator.uploadTextFileA();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
                case "3":
                    textComparator.uploadStopWordFilter();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
                case "S":
                    textComparator.getSystemStatus();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
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
