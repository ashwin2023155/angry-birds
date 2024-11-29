import com.angrybirds.com.Structure;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class StructureTest {
    private Texture mockTexture;
    private SpriteBatch mockBatch;
    private Structure structure;

    @Before
    public void setUp() {
        mockTexture = mock(Texture.class);
        mockBatch = mock(SpriteBatch.class);
        structure = new Structure(mockTexture, 100, 200);
    }

    @Test
    public void testRender() {
        structure.render(mockBatch);
        verify(mockBatch).draw(mockTexture, 100, 200);
    }

    @Test
    public void testDispose() {
        structure.dispose();
        verify(mockTexture).dispose();
    }
}
