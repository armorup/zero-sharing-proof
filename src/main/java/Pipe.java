import static zero.Collisions.circleHitsRect;

/** A moving pair of pipes with an opening between two solid rectangles. */
public class Pipe {
    public static final double WIDTH = 76, GAP = 168, RIM_HEIGHT = 38, SHAFT_INSET = 8;
    public double x;
    public final double openingY;
    public boolean scored;
    public Pipe(double x, double openingY) { this.x = x; this.openingY = openingY; }
    public void update(double seconds) { x -= Game.PIPE_SPEED * seconds; }
    public double gapTop() { return openingY - GAP / 2; }
    public double gapBottom() { return openingY + GAP / 2; }
    public boolean touches(Bird bird) {
        return circleHitsRect(Bird.X, bird.y, Bird.RADIUS, x, gapTop() - RIM_HEIGHT, WIDTH, RIM_HEIGHT)
            || circleHitsRect(Bird.X, bird.y, Bird.RADIUS, x + SHAFT_INSET, 0,
                WIDTH - 2 * SHAFT_INSET, gapTop() - RIM_HEIGHT)
            || circleHitsRect(Bird.X, bird.y, Bird.RADIUS, x, gapBottom(), WIDTH, RIM_HEIGHT)
            || circleHitsRect(Bird.X, bird.y, Bird.RADIUS, x + SHAFT_INSET, gapBottom() + RIM_HEIGHT,
                WIDTH - 2 * SHAFT_INSET, Game.GROUND_Y - gapBottom() - RIM_HEIGHT);
    }
}
