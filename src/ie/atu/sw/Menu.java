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
            System.out.println("(2) Upload Text File B [Current - "
                    + (textComparator.getTextFileBPath() == null ? "Not Set" : textComparator.getTextFileBPath())
                    + "]");
            System.out.println("(3) Upload Stop Words List File [Current - "
                    + (textComparator.getStopWordsFilePath() == null ? "Not Set"
                            : textComparator.getStopWordsFilePath())
                    + "]");
            System.out.println("(4) Compare Text Files");
            System.out.println("(5) Switch Filtering Mode [Current - "
                    + (textComparator.getIsFiltering() ? "Enabled" : "Disabled")
                    + "]");
            System.out.println("(6) Noise Analizer");
            System.out.println("(7) Get System Status");
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
                case "2":
                    textComparator.uploadTextFileB();
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
                case "4":
                    textComparator.compareFiles();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
                case "5":
                    textComparator.switchFilteringMode();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
                case "6":
                    textComparator.noiseAnalyzer();
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
                case "7":
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
                    System.out.println("Please input number between 1 and 7 or Q for quit!");
                    System.out.println();
                    System.out.println("Please press Enter to continue...");
                    s.nextLine();
                    break;
            }
        }
    }
}
