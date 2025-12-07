package za.co.wethinkcode.robots.domain;

import java.util.List;

public class World {

    private final String name;
    private final int width;
    private final int height;
    private final List<Obstacle> obstacles;

    public World(String name, int width, int height, List<Obstacle> obstacles) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
    }

    public String getName() { return name; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<Obstacle> getObstacles() { return obstacles; }

}
