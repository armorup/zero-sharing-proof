import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zero.ImageRegion;
import zero.SpriteAnimation;
import zero.SketchApp;

/** Flappy Bird style game: Zero helpers, explicit updates, ordinary objects. */
public class Main extends SketchApp {
    final Game game = new Game();
    private Image skyImage;
    private ImageRegion pipeArt;
    SpriteAnimation wings;
    GameAudio audio;
    @Override public void settings() { title("Sky Hop"); size(480, 680); }
    @Override public void setup() {
        Image sheet = loadImage("/bird-flap-sheet.png");
        // Generated atlas: the lower row needs a 30-pixel alignment adjustment.
        wings = new SpriteAnimation(12,
                imageRegion(sheet, 0, 0, 701, 561), imageRegion(sheet, 701, 0, 701, 561),
                imageRegion(sheet, 0, 531, 701, 561), imageRegion(sheet, 701, 531, 701, 561));
        skyImage = loadImage("/sky.png");
        pipeArt = imageRegion(loadImage("/pipe-v2.png"), 316, 0, 388, 1536);
        audio = GameAudio.load(this);
        canvas().setAccessibleText("Sky Hop. Space or click to flap; P pauses; R restarts; M mutes.");

    }
    @Override public void update(double seconds) {
        boolean flapped = false;
        if (keyPressed(KeyCode.M)) audio.toggleMute();
        if (!windowFocused()) game.pause();
        if (keyPressed(KeyCode.R)) { game.reset(); wings.reset(); audio.restart(); }
        else if (keyPressed(KeyCode.P)) game.togglePause();
        else if (keyPressed(KeyCode.SPACE) || mousePressed(MouseButton.PRIMARY)) {
            if (game.state == Game.State.GAME_OVER) { wings.reset(); audio.restart(); }
            flapped = game.state != Game.State.PAUSED;
            game.flap();
        }
        game.update(seconds);
        audio.update(game.state, game.score, flapped);
        if (game.state == Game.State.READY || game.state == Game.State.PLAYING) {
            wings.update(seconds);
        }
    }
    @Override public void draw() {
        background(Color.web("#b9dfe0")); image(skyImage, 0, 0, width(), Game.GROUND_Y);
        for (Pipe pipe : game.pipes) drawPipe(pipe);
        drawBird(); drawGround(); drawHud();
        if (game.state != Game.State.PLAYING) drawOverlay();
    }
    private void drawPipe(Pipe pipe) {
        image(pipeArt, pipe.x, pipe.gapBottom(), Pipe.WIDTH, 360);
        imageFlippedY(pipeArt, pipe.x, pipe.gapTop() - 360, Pipe.WIDTH, 360);
    }
    private void drawBird() {
        double tilt = Math.max(-22, Math.min(65, game.bird.velocity / 10));
        imageCentered(wings, Bird.X, game.bird.y, 90, 72, tilt);
    }
    private void drawGround() {
        fill(Color.web("#365f50")); rect(0, Game.GROUND_Y, width(), 6);
        fill(Color.web("#d8e6ad")); rect(0, Game.GROUND_Y + 6, width(), 66);
    }
    private void drawHud() {
        if (game.state != Game.State.READY) {
            centeredText(Integer.toString(game.score), 44, 78, "#153f48");
        }
    }
    private void drawOverlay() {
        if (game.state == Game.State.READY) {
            centeredText("Sky Hop", 56, 200, "#153f48");
            centeredText("Space or Click to Start", 20, 246, "#153f48");
        } else if (game.state == Game.State.PAUSED) {
            centeredText("Paused", 48, 240, "#153f48");
            centeredText("P to Resume", 20, 282, "#153f48");
        } else {
            centeredText("Game Over", 48, 240, "#153f48");
            centeredText("Space or Click to Retry", 20, 282, "#153f48");
        }
    }
    private void centeredText(String value, double size, double y, String color) {
        graphics().setFont(Font.font("Arial", FontWeight.BOLD, size));
        fill(Color.web(color)); textCentered(value, width() / 2, y);
    }
    public static void main(String[] args) { launch(args); }
}
