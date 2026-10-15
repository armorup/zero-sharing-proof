import java.util.ArrayList;
import java.util.Random;

/** Plain Java rules, independent of drawing and window focus. */
public class Game {
    public enum State { READY, PLAYING, PAUSED, GAME_OVER }
    public static final double GROUND_Y = 608, PIPE_SPEED = 170, PIPE_SPACING = 280;
    public final Bird bird = new Bird();
    public final ArrayList<Pipe> pipes = new ArrayList<>();
    public State state = State.READY;
    public int score, best; // Best lasts for this session; no file writes.
    private final Random random;
    public Game() { this(new Random()); }
    public Game(Random random) { this.random = random; reset(); }
    public void reset() {
        bird.reset(); score = 0; pipes.clear();
        for (int i = 0; i < 3; i++) pipes.add(newPipe(550 + i * PIPE_SPACING));
        state = State.READY;
    }
    private Pipe newPipe(double x) {
        return new Pipe(x, 220 + random.nextDouble() * 210);
    }
    public void flap() {
        if (state == State.PAUSED) return;
        if (state == State.GAME_OVER) reset();
        state = State.PLAYING; bird.flap();
    }
    public void pause() { if (state == State.PLAYING) state = State.PAUSED; }
    public void togglePause() {
        if (state == State.PLAYING) state = State.PAUSED;
        else if (state == State.PAUSED) state = State.PLAYING;
    }
    public void update(double seconds) {
        if (state != State.PLAYING || !Double.isFinite(seconds) || seconds <= 0) return;
        // Small steps prevent crossing obstacles during a slow frame.
        double remaining = Math.min(seconds, 0.1);
        while (remaining > 0.000001 && state == State.PLAYING) {
            double step = Math.min(remaining, 1.0 / 120);
            bird.update(step);
            for (Pipe pipe : pipes) pipe.update(step);
            if (bird.y - Bird.RADIUS <= 0 || bird.y + Bird.RADIUS >= GROUND_Y) { endRound(); break; }
            for (Pipe pipe : pipes) {
                if (pipe.touches(bird)) { endRound(); break; }
            }
            if (state != State.PLAYING) break;
            for (Pipe pipe : pipes) {
                if (!pipe.scored && pipe.x + Pipe.WIDTH < Bird.X - Bird.RADIUS) {
                    pipe.scored = true; score++; best = Math.max(best, score);
                }
            }
            if (pipes.get(0).x + Pipe.WIDTH < 0) {
                double nextX = pipes.get(pipes.size() - 1).x + PIPE_SPACING;
                pipes.remove(0); pipes.add(newPipe(nextX));
            }
            remaining -= step;
        }
    }
    private void endRound() {
        state = State.GAME_OVER;
        bird.y = Math.max(Bird.RADIUS, Math.min(GROUND_Y - Bird.RADIUS, bird.y));
    }
}
