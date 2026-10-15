/** Ordinary object: Game explicitly calls update; Zero discovers nothing. */
public class Bird {
    public static final double X = 132, RADIUS = 15;
    public static final double GRAVITY = 1250, FLAP_SPEED = -370;
    public double y = 285, velocity;
    public void flap() { velocity = FLAP_SPEED; }
    public void update(double seconds) {
        velocity = Math.min(650, velocity + GRAVITY * seconds);
        y += velocity * seconds;
    }
    public void reset() { y = 285; velocity = 0; }
}
