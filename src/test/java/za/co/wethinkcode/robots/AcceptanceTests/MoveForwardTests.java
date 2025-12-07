package za.co.wethinkcode.robots.AcceptanceTests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import za.co.wethinkcode.robots.server.RobotWorldClient;
import za.co.wethinkcode.robots.server.RobotWorldJsonClient;
@Disabled
class MoveForwardTests {
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
    void moveForwardShouldUpdateRobotPosition() {
        // Step 1: Launch the robot at (0,0) facing NORTH
        String launchRequest = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\", 5, 5]" +
                "}";
        JsonNode launchResponse = serverClient.sendRequest(launchRequest);
        assertEquals("OK", launchResponse.get("result").asText());

        JsonNode initialPosition = launchResponse.get("data").get("position");
        int startX = initialPosition.get(0).asInt();
        int startY = initialPosition.get(1).asInt();
        // Step 2: Send forward command to move 1 step
        String moveRequest = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"forward\"," +
                "\"arguments\": [1]" +
                "}";
        JsonNode moveResponse = serverClient.sendRequest(moveRequest);
        // Step 3: Validate response is OK
        assertEquals("OK", moveResponse.get("result").asText());
        // Step 4: Check the new position is updated correctly
        JsonNode newPosition = moveResponse.get("data").get("position");
        int newX = newPosition.get(0).asInt();
        int newY = newPosition.get(1).asInt();

        assertEquals(startX, newX, "X position should remain the same");
        assertEquals(startY + 1, newY, "Y position should increase by 1 when facing NORTH");
        // Step 5: Confirm robot state is included
        assertNotNull(moveResponse.get("state"));
        assertEquals("NORMAL", moveResponse.get("state").get("status").asText());
    }
}

