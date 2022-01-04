package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DSScreenSimulator {

    private static final double PERC = 0.8;

    public static void modifyImage(File image) throws IOException {
        BufferedImage buf = ImageIO.read(image);


        BufferedImage out = new BufferedImage(buf.getWidth() * 2, buf.getHeight() * 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = out.createGraphics();

        for (int width = 0; width < buf.getWidth(); ++width) {
            for (int height = 0; height < buf.getHeight(); ++height) {
                Color c = new Color(buf.getRGB(width, height));
                g2d.setColor(c);
                g2d.fillRect(width * 2, height * 2, 1, 1);
                g2d.setColor(new Color(
                        (int)(c.getRed() * PERC),
                        (int)(c.getGreen() * PERC),
                        (int)(c.getBlue() * PERC)
                ));
                g2d.fillRect(width * 2 + 1, height * 2, 1, 1);
                g2d.fillRect(width * 2, height * 2 + 1, 1, 1);
                g2d.fillRect(width * 2 + 1, height * 2 + 1, 1, 1);
            }
        }

        g2d.dispose();
        ImageIO.write(out, "png", image);
    }

}
