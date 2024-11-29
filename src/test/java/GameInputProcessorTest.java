import com.angrybirds.com.GameInputProcessor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameInputProcessorTest {
    private GameInputProcessor gameInputProcessor;

    @Before
    public void setUp() {
        gameInputProcessor = new GameInputProcessor();
    }

    @Test
    public void testTouchDown() {
        assertFalse(gameInputProcessor.touchDown(100, 200, 0, 0));
    }

    @Test
    public void testTouchDragged() {
        assertFalse(gameInputProcessor.touchDragged(100, 200, 0));
    }

    @Test
    public void testTouchUp() {
        assertFalse(gameInputProcessor.touchUp(100, 200, 0, 0));
    }
}
