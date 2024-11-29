import com.angrybirds.com.Score;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreTest {
    private Score score;

    @Before
    public void setUp() {
        score = new Score();
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, score.getCurrentScore());
    }

    @Test
    public void testAddPoints() {
        score.addPoints(10);
        assertEquals(10, score.getCurrentScore());

        score.addPoints(5);
        assertEquals(15, score.getCurrentScore());
    }
}
