package za.co.wethinkcode.robots.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.Runnable;
import org.json.JSONObject;
import org.json.JSONException;
import za.co.wethinkcode.robots.commands.*;
import za.co.wethinkcode.robots.server.World;

/**
 * Handles communication with a single client in the Robot World server.
 * Parses incoming JSON commands and delegates them to CommandHandler for processing.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final World world;
    private final CommandHandler commandHandler; // CommandHandler for processing commands

    public ClientHandler(Socket socket, World world) {
        this.clientSocket = socket;
        this.world = world;
        this.commandHandler = new CommandHandler(world); // Initialize CommandHandler
    }

    @Override
    public void run() {
        String clientId = clientSocket.getRemoteSocketAddress().toString(); // Unique client ID

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client [" + clientId + "]: " + message);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message);
                } catch (JSONException e) {
                    out.println("{\"error\": \"Invalid command format. Commands must be in JSON.\"}");
                    continue;
                }

                Command command;
                try {
                    command = Command.fromJSON(jsonObject);
                } catch (IllegalArgumentException e) {
                    out.println("{\"error\": \"Unknown or unsupported command.\"}");
                    continue;
                }

                synchronized (world) {
                    commandHandler.handle(command, clientId, response -> {
                        out.println(response.toJSONString());
                        this.world.displayWorld();
                    });
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client " + clientId + ": " + e);
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientId);
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e);
            }
        }
    }
}