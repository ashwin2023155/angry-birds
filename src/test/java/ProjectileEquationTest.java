import com.angrybirds.com.ProjectileEquation;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectileEquationTest {
    private ProjectileEquation projectileEquation;

    @Before
    public void setUp() {
        projectileEquation = new ProjectileEquation();
        projectileEquation.gravity = 9.8f;
        projectileEquation.startVelocity.set(10, 10);
        projectileEquation.startPoint.set(0, 0);
    }

    @Test
    public void testGetX() {
        float t = 2.0f;
        float expectedX = 20.0f; // 10 * 2 + 0
        assertEquals(expectedX, projectileEquation.getX(t), 0.001f);
    }

    @Test
    public void testGetY() {
        float t = 2.0f;
        float expectedY = 0.5f * 9.8f * 2 * 2 + 10 * 2 + 0; // 0.5 * 9.8 * 4 + 20
        assertEquals(expectedY, projectileEquation.getY(t), 0.001f);
    }

    @Test
    public void testGetTForGivenX() {
        float x = 20.0f;
        float expectedT = 2.0f; // (20 - 0) / 10
        assertEquals(expectedT, projectileEquation.getTForGivenX(x), 0.001f);
    }
}
