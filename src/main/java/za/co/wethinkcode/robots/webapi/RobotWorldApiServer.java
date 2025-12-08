package za.co.wethinkcode.robots.webapi;

import io.javalin.Javalin;

public class RobotWorldApiServer {
    private final Javalin server;

    public RobotWorldApiServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
        });

        server.get("/world", RobotWorldApiHandler::getCurrentWorld);
        server.get("/world/{name}", RobotWorldApiHandler::getNamedWorld);
        server.post("/robot/{name}", RobotWorldApiHandler::launchRobot);
    }

    public void start(int port) {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        RobotWorldApiHandler handler = new RobotWorldApiHandler();

        app.get("/world", RobotWorldApiHandler::getCurrentWorld);
        app.get("/world/{name}", RobotWorldApiHandler::getNamedWorld);
        app.post("/robot/{name}", handler::robotCommand);
   
    }
}