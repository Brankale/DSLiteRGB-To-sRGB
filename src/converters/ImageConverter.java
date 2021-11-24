package converters;

import colorspaces.ColorSpace;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageConverter {

    private ImageConverter() {
        // avoid instantiation
    }

    public static BufferedImage convert(BufferedImage buf, ColorSpace src, ColorSpace dest) {
        ColorSpaceConverter csConverter = new ColorSpaceConverter(src, dest);
        return convert(buf, csConverter);
    }

    public static BufferedImage convert(BufferedImage buf, ColorSpaceConverter csConverter) {
        for (int height = 0; height < buf.getHeight(); ++height) {
            for (int width = 0; width < buf.getWidth(); ++width) {
                Color color = new Color(buf.getRGB(width, height));

                // TODO: DS Lite display has 6bit per channel so I approximate the color here.
                //       Since the starting colorspace is not necessarily DSL you MUST put this
                //       thing somewhere else.
                color = new Color(
                        color.getRed() & 0xFC,
                        color.getGreen() & 0xFC,
                        color.getBlue() & 0xFC
                );

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
