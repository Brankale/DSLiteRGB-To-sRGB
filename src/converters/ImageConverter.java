package converters;

import colorspaces.ColorSpace;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageConverter {

    private ImageConverter() {
        // avoid instantiation
    }

    public static BufferedImage convert(BufferedImage buf, ColorSpace src, ColorSpace dest) {
        ColorSpaceConverter csConverter = new ColorSpaceConverter(src, dest, true);
        return convert(buf, csConverter);
    }

    public static BufferedImage convert(BufferedImage buf, ColorSpaceConverter csConverter) {
        for (int height = 0; height < buf.getHeight(); ++height) {
            for (int width = 0; width < buf.getWidth(); ++width) {
                Color color = new Color(buf.getRGB(width, height));
                try {
                    Color convertedColor = csConverter.convert(color);
                    buf.setRGB(width, height, convertedColor.getRGB());
                } catch (OutsideGamutException e) {
                    buf.setRGB(width, height, Color.BLACK.getRGB());
                }
            }
        }
        return buf;
    }

}
