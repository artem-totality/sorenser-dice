package ie.atu.sw;

/**
 * Application entry point.
 *
 * <p>
 * This class serves as the main launcher for the application.
 * It initializes the program and delegates control to the
 * console-based menu system.
 *
 * <p>
 * The application allows rapid comparison of two text files
 * by computing the Sørensen–Dice similarity coefficient, with
 * optional stop-word filtering and noise analysis.
 *
 * <p>
 * <b>Module:</b> Software Design & Data Structures – Final Project
 * 
 * @author Artem Vasylevytskyi
 * @version 1.0
 */

public class Runner {

    /**
     * Main method invoked by the Java Virtual Machine (JVM).
     *
     * Starts the application by launching the interactive
     * console menu.
     */
    public static void main(String[] args) {
        // Start the application by invoking the main menu loop
        Menu.performMenu();
    }

}