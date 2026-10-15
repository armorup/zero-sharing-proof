import zero.Music;
import zero.Sound;
import zero.SimpleApp;
import javafx.scene.media.MediaException;

/** Game-specific sound rules. Zero loads and cleans up the underlying audio. */
public final class GameAudio {
    private final Music music;
    private final Sound flap, score, crash;
    private Game.State previousState = Game.State.READY;
    private int previousScore;
    private boolean muted, playing, failed;

    /** Missing files still fail clearly; a native media failure permits silent play. */
    public static GameAudio load(SimpleApp app) {
        try {
            return new GameAudio(app.loadSound("/audio/flap.wav"), app.loadSound("/audio/score.wav"),
                    app.loadSound("/audio/crash.wav"), app.loadMusic("/audio/sky-hop-loop.wav"));
        } catch (IllegalArgumentException failure) {
            if (!(failure.getCause() instanceof MediaException)) throw failure;
            System.err.println("Sky Hop audio is unavailable; continuing without sound.");
            return new GameAudio();
        }
    }
    private GameAudio() { flap = null; score = null; crash = null; music = null; failed = true; }
    public GameAudio(Sound flap, Sound score, Sound crash, Music music) {
        this.flap = flap; this.score = score; this.crash = crash; this.music = music;
        flap.setVolume(.32); score.setVolume(.46); crash.setVolume(.45); music.setVolume(.24);
    }
    /** Main reports events; this class never changes physics or game state. */
    public void update(Game.State state, int points, boolean flapped) {
        if (failed) return;
        try {
            boolean shouldPlay = state == Game.State.PLAYING;
            if (shouldPlay != playing) {
                if (shouldPlay) music.loop(); else music.pause();
                playing = shouldPlay;
            }
            if (flapped) flap.play();
            if (points > previousScore) score.play();
            if (state == Game.State.GAME_OVER && previousState != Game.State.GAME_OVER) crash.play();
            if (state == Game.State.PAUSED && previousState != Game.State.PAUSED) stopEffects();
            previousState = state; previousScore = points;
        } catch (IllegalStateException unavailable) { disableAudio(); }
    }
    public void restart() {
        if (failed) return;
        try {
            stopEffects(); music.stop(); playing = false;
            previousState = Game.State.READY; previousScore = 0;
        } catch (IllegalStateException unavailable) { disableAudio(); }
    }
    public void toggleMute() {
        if (failed) return;
        try {
            muted = !muted;
            music.setMuted(muted); flap.setMuted(muted); score.setMuted(muted); crash.setMuted(muted);
        } catch (IllegalStateException unavailable) { disableAudio(); }
    }
    public boolean isMuted() { return muted; }
    private void stopEffects() { flap.stop(); score.stop(); crash.stop(); }
    private void disableAudio() {
        failed = true;
        flap.close(); score.close(); crash.close(); music.close();
        System.err.println("Sky Hop audio is unavailable; continuing without sound.");
    }
}
