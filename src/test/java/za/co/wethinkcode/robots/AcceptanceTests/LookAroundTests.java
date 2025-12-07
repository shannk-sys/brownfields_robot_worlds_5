package za.co.wethinkcode.robots.AcceptanceTests;
import org.junit.jupiter.api.Disabled;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.server.RobotWorldClient;
import za.co.wethinkcode.robots.handlers.VisibilityHandler;
import za.co.wethinkcode.robots.server.RobotWorldClient;
import za.co.wethinkcode.robots.server.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class LookAroundTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer() {
        serverClient.disconnect();
    }


    @Test
    void lookWhenWorldIsEmptyShouldReturnError() {
        //TODO [Khumo Tsoeu] - Implement this test
        // Given I am connected to the Robot World server
        assertTrue(serverClient.isConnected());

        // And no robots have been launched (world is empty)

        // When I send a "look" command from a robot that doesn't exist
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"" +
                "}";

        JsonNode response = serverClient.sendRequest(request);

        // Then the server should return an error
        assertNotNull(response.get("result"), "Response missing 'result' field.");
        assertEquals("ERROR", response.get("result").asText(), "Expected ERROR result for non-existent robot.");

        // And the error message should mention that the robot does not exist or is not launched
        assertNotNull(response.get("data"), "Response missing 'data' field.");
        assertNotNull(response.get("data").get("message"), "Response missing error message.");
        String message = response.get("data").get("message").asText().toLowerCase();
        assertTrue(message.contains("not launched") || message.contains("does not exist"),
                "Expected error message about robot not being launched, got: " + message);
    }
}