package za.co.wethinkcode.robots.client;

import za.co.wethinkcode.robots.commands.Command;
import za.co.wethinkcode.robots.commands.DisconnectCommand;
import za.co.wethinkcode.robots.server.Response;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientApp {
    /***
     * The ClientApp class is a Java console application that connects to a RobotWorld server,
     * allows users to launch robots, and send commands to control them. It validates input and
     * manages communication with the server.
     */

    // Maximum number of robots a single client can launch
    private static final int MAX_ROBOTS = 2;
    // List of accepted robot types
    private static final List<String> VALID_ROBOT_TYPES = List.of("sniper", "tank");

    /**
     * Main method for the client application. Handles connection setup, robot launch,
     * and main command loop for controlling the launched robot.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException if an I/O error occurs when creating or using the socket.
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        // Map to store launched robot names and their types (currently unused beyond checking size) 
        Map<String, String> robots = new HashMap<>();

        // Prompt user for server connection details
        String host = prompt(scanner, "Hello! Welcome to RobotWorld. Please enter the IP address of the server you'd like to connect to:");
        int portNumber = promptInt(scanner, "Enter the port number:");

        // Try-with-resources statement ensures the socket, PrintWriter, and BufferedReader are closed automatically
        try (
                Socket socket = new Socket(host, portNumber);
                // PrintWriter for sending commands to the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // BufferedReader for receiving responses from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Ready for launch!");

            // Loop to handle the launch of robots up to MAX_ROBOTS limit
            while (robots.size() < MAX_ROBOTS) {
                String robotName = prompt(scanner, "Enter a name for your robot:");
                if (!isValidRobotName(robotName)) {
                    System.out.println("Robot name must only contain letters and numbers. Please try again.");
                    continue;
                }

                String robotType = prompt(scanner, "Enter a type for your robot (sniper/tank):").toLowerCase().trim();
                if (!VALID_ROBOT_TYPES.contains(robotType)) {
                    System.out.println("Invalid robot type. Valid types are: sniper, tank. Please try again.");
                    continue;
                }

                Command cmd;
                try {
                    // Create a LaunchCommand from input string
                    cmd = Command.fromInput("launch " + robotType + " " + robotName, robotName);
                    // Send the command to the server as a JSON string
                    out.println(cmd.toJSONString());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Command. Try again");
                    continue;
                }

                // Read the server's response to the launch command
                String jsonString = in.readLine();
                Response response = Response.responseFromJSONString(jsonString);

                // Check if the launch was successful (server returned an "OK" status)
                if (!response.isOKResponse()) {
                    System.out.println("Server: " + response.getMessage());
                    continue;
                }

                System.out.println("Launching your robot into the world 🚀");
                sleep(4000);
                System.out.println(response.getMessage());
                robots.put(robotName, robotType); // Record the successfully launched robot
                System.out.println("To check what you can do: use 'help'\n");

                // Enter the command handling loop for the launched robot
                handleCommands(scanner, robotName, in, out);
                break; // Exit the launch loop after launching the first robot and entering command mode
            }

            // Display an error if the launch loop terminates because the limit was reached
            if (robots.size() >= MAX_ROBOTS) {
                System.out.println("ERROR: Cannot launch more than " + MAX_ROBOTS + " robots.");
            }

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    /**
     * Handles the continuous loop of sending commands to the server and processing responses.
     *
     * @param scanner The Scanner object for reading user input.
     * @param robotName The name of the robot being controlled.
     * @param in The BufferedReader for reading server responses.
     * @param out The PrintWriter for sending commands to the server.
     * @throws IOException If an I/O error occurs during communication.
     */
    private static void handleCommands(Scanner scanner, String robotName, BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            System.out.print("Enter command: ");
            String message = scanner.nextLine();

            // Special handling for the 'disconnect' command
            if (message.equalsIgnoreCase("disconnect")) {
                // Send a DisconnectCommand to the server
                out.println(new DisconnectCommand().toJSONString());
                System.out.println("Server: " + in.readLine());
                break; // Exit the command loop
            }

            // Prevent client from executing server-only administrative commands
            if (isRestrictedCommand(message)) {
                System.out.println("This command can only be run by the server admin");
                continue;
            }

            try {
                // Parse input and create the appropriate Command object
                Command cmd = Command.fromInput(message, robotName);
                out.println(cmd.toJSONString()); // Send command to server
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Command. Try again");
                continue; // Prompt for new command if parsing failed
            }
            // Read and display the primary response from the server
            System.out.println("Server: " + Response.responseFromJSONString(in.readLine()).getMessage());

            // Special handling for commands that trigger a delayed secondary response (e.g., due to waiting)
            if (message.contains("reload") || message.contains("repair")) {
                // Read and display the secondary response from the server
                System.out.println("Server: " + Response.responseFromJSONString(in.readLine()).getMessage());
            }
        }
    }

    /**
     * Helper method to prompt the user and read a line of string input.
     *
     * @param scanner The Scanner object.
     * @param message The prompt message to display.
     * @return The user's input string.
     */
    private static String prompt(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    /**
     * Helper method to prompt the user and read a validated integer input.
     *
     * @param scanner The Scanner object.
     * @param message The prompt message to display.
     * @return The validated integer input.
     */
    private static int promptInt(Scanner scanner, String message) {
        System.out.println(message);
        // Loop until a valid integer is entered
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number:");
            scanner.next(); // Discard the invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }

    /**
     * Validates if a robot name contains only letters and numbers and is not empty.
     *
     * @param name The name string to validate.
     * @return true if the name is valid, false otherwise.
     */
    private static boolean isValidRobotName(String name) {
        return !name.trim().isEmpty() && name.matches("[a-zA-Z0-9]+");
    }

    /**
     * Checks if the user command is one of the server-only administrative commands.
     *
     * @param message The command string entered by the user.
     * @return true if the command is restricted, false otherwise.
     */
    private static boolean isRestrictedCommand(String message) {
        String lower = message.toLowerCase();
        // The commands 'quit', 'robots', and 'dump' are admin-only
        return lower.equals("quit") || lower.equals("robots") || lower.equals("dump");
    }

    /**
     * Simple utility method to pause the execution for a given number of milliseconds.
     *
     * @param millis The duration to sleep in milliseconds.
     */
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
