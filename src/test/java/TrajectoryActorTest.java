import com.angrybirds.com.TrajectoryActor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrajectoryActorTest {
    private Sprite mockSprite;
    private TrajectoryActor trajectoryActor;

    @Before
    public void setUp() {
        mockSprite = mock(Sprite.class);
        trajectoryActor = new TrajectoryActor(mockSprite);
    }

    @Test
    public void testSetTrajectoryData() {
        Vector2 startPosition = new Vector2(10, 20);
        Vector2 velocity = new Vector2(5, 10);

        trajectoryActor.setTrajectoryData(startPosition, velocity);

        assertEquals(startPosition, trajectoryActor.startPosition);
        assertEquals(velocity, trajectoryActor.velocity);
    }

    @Test
    public void testAct() {
        Vector2 startPosition = new Vector2(0, 0);
        Vector2 velocity = new Vector2(10, 10);
        trajectoryActor.setTrajectoryData(startPosition, velocity);

        trajectoryActor.act(0.1f);

        verify(mockSprite, atLeastOnce()).setPosition(anyFloat(), anyFloat());
    }

    @Test
    public void testDraw() {
        Batch mockBatch = mock(Batch.class);
        trajectoryActor.draw(mockBatch, 1.0f);

        verify(mockSprite).draw(mockBatch);
    }
}
