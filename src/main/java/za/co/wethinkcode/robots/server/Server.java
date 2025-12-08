package za.co.wethinkcode.robots.server;
import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main server class that accepts client connections and provides an admin console for server control.
 * Supports real-time robot monitoring, world state inspection, and graceful shutdown.
 */
public class Server {
    // Flag to control the main server loop and threads
    private static volatile boolean isRunning = true;
    private static ServerSocket serverSocket;

    /**
     * Main method to start the server.
     * It prompts for a port number if not provided as a command-line argument,
     * initializes the server socket, and starts the admin console thread.
     * It then enters a loop to continuously accept new client connections.
     *
     * @param args Optional array containing the port number at index 0.
     */
    public static void main(String[] args) {
        int portNumber;
        // World is a singleton instance that manages the game state, robots, and obstacles.
        World world = World.getInstance();
        // Check for command-line argument for port number, otherwise prompt user
        if (args.length != 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the port number: ");
            portNumber = scanner.nextInt();
            scanner.nextLine();
        } else {
            portNumber = Integer.parseInt(args[0]);
        }

        try {
            serverSocket = new ServerSocket(portNumber); // Initialize the server socket
            System.out.println("Server started on port " + portNumber + ". Waiting for clients...");

            // launch admin console thread to handle server management commands
            startAdminConsole(world);

            // Main loop to continuosly listen for and accept client connections
            while (isRunning) {
                Socket clientSocket = serverSocket.accept(); // Blocking call: waits for a client to connect
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket, world)).start(); // start new thread to handle multiple clients
            }

        } catch (IOException e) {
            // Check if error is due to a deliberate shutdown or another IO issue
            if (!isRunning) {
                System.out.println("Sever shutdown.");
            } else {
                System.out.println("Got an error: " + e);
            }
        }
    }
    /**
     * Starts a new thread for the administrative console.
     * The console allows the server operator to monitor and control the server.
     *
     * @param world The current game world instance to interact with.
     */
    private static void startAdminConsole(World world) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (isRunning) {
                System.out.println("Valid Commands: 'quit', 'robots', 'dump', 'display'");
                System.out.print("[Admin]: ");
                String input = scanner.nextLine().trim().toLowerCase();
                
                // process admin command input
                switch (input) {

                    case "quit":
                        System.out.println("Shutting down server...");
                        shutdown(); // init graceful server shutdown
                        break;

                    case "robots":
                        //Display info about all active robots
                         System.out.println(world.getAllRobotsInfo());
                        break;

                    case "dump":
                        //Display full raw state of the game world
                        System.out.println(world.getFullWorldState());
                        break;

                    case "display":
                        // Display a visual representation of the game world
                        world.displayWorld();
                        break;

                    default:
                        System.out.println("Unknown admin command.");
                }
            }
        }, "AdminConsole").start();
    }

    /**
     * Initiates a graceful shutdown of the server.
     * Sets the isRunning flag to false to stop all loops and closes the ServerSocket.
     */
    public static void shutdown() {
        isRunning = false;
        try {
            serverSocket.close(); // Closes the socket, causing serverSocket.accept() to throw an exception
        } catch (IOException e) {
            System.out.println("Got an error when shutting down: " + e);
        }
    }
    static {
        new Recorder().logRun();
    }
}

