package za.co.wethinkcode.robots.AcceptanceTests;
import org.junit.jupiter.api.Disabled;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import za.co.wethinkcode.robots.server.RobotWorldClient;
import za.co.wethinkcode.robots.server.RobotWorldJsonClient;

/**
 * Tests for the "state" command in the Robot World Protocol.
 */
@Disabled
class RobotStateTests {
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

    /**
     * Scenario: The robot exists in the world.
     * Should return the robot's state.
     */
    @Test
    void getState_whenRobotExists_returnsRobotState() {
            //TODO [Shannon Keating] - Implement this test
            // Launch a robot
        String launchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        String request = "{" +
                "\"robot\": \"HAL\","+
                "\"command\": \"state\"," +
                "\"arguments\": []" +
                "}";
        // robot type, max shields, max shots
        JsonNode response = serverClient.sendRequest(request);
            // Check launch success
        assertEquals("OK", response.get("result").asText());
        assertTrue(response.has("state"));
    }

    @Test
    void getStateWhenRobotDoesNotExist(){
        String request = "{" +
                "\"robot\": \"Phumi\","+
                "\"command\": \"state\"," +
                "\"arguments\": []" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        assertEquals("ERROR", response.get("result").asText());
        assertNotNull(response.get("data"));
        assertTrue(response.get("data").get("message").asText().toLowerCase().contains("not exist"));


    }
}