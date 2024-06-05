package renderer;

import org.junit.jupiter.api.Test;
import org.example.renderer.ImageWriter;
import org.example.primitives.Color;


public class ImageWriterTest {

    /**
     * Test method for {@link ImageWriter#writePixel(int, int, Color)}.
     * Simple test to create image.
     */
    @Test
    public void testWritePixel() {
        int nx = 800;
        int ny = 500;
        int rows = 16;
        int columns = 10;
        boolean isOnTheLine;
        ImageWriter imageWriter = new ImageWriter("ImageBasic", nx, ny);

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                isOnTheLine = i % (nx / rows) == 0 || j % (ny / columns) == 0;
                if (isOnTheLine) {
                    imageWriter.writePixel(i, j, new Color(0, 0, 255)); //blue
                } else {
                    imageWriter.writePixel(i, j, new Color(204, 153, 255)); //light purple
                }
            }
        }
        imageWriter.writeToImage();
    }
}
