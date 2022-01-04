package utils;

import colorspaces.ColorSpace;
import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import converters.OutsideGamutException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class InGamutColorGenerator {

    private static final Random random = new Random();

    private static final int WIDTH = 256;
    private static final int HEIGHT = 192;

    public static void main(String[] args) throws IOException {
        File colorCard = new File("C:\\xampp\\htdocs\\ds\\color_cards\\___.png");
        generateColorCard(colorCard);

        colorCard = new File("C:\\xampp\\htdocs\\ds\\color_cards\\r.png");
        generateRedColorCard(colorCard);
        colorCard = new File("C:\\xampp\\htdocs\\ds\\color_cards\\g.png");
        generateGreenColorCard(colorCard);
        colorCard = new File("C:\\xampp\\htdocs\\ds\\color_cards\\b.png");
        generateBlueColorCard(colorCard);
    }

    /**
     * Given a source and a destination color space,
     * It will be generated an image with random color that is present
     * in both the color spaces
     */
    public static void generateColorCard(File dest) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Color color = generateInGamutColor(new DSLite(), new SRGB());
        g2d.setColor(color);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.dispose();
        ImageIO.write(bufferedImage, "png", dest);
    }

    public static void generateRedColorCard(File dest) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Color color = generateInGamutColor(new DSLite(), new SRGB());
        for (int i = 0; i < WIDTH; ++i) {
            g2d.setColor(new Color(i, 0, 0));
            g2d.fillRect(i, 0, 1, HEIGHT);
        }
        g2d.dispose();
        ImageIO.write(bufferedImage, "png", dest);
    }

    public static void generateGreenColorCard(File dest) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Color color = generateInGamutColor(new DSLite(), new SRGB());
        for (int i = 0; i < WIDTH; ++i) {
            g2d.setColor(new Color(0, i, 0));
            g2d.fillRect(i, 0, 1, HEIGHT);
        }
        g2d.dispose();
        ImageIO.write(bufferedImage, "png", dest);
    }

    public static void generateBlueColorCard(File dest) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Color color = generateInGamutColor(new DSLite(), new SRGB());
        for (int i = 0; i < WIDTH; ++i) {
            g2d.setColor(new Color(0, 0, i));
            g2d.fillRect(i, 0, 1, HEIGHT);
        }
        g2d.dispose();
        ImageIO.write(bufferedImage, "png", dest);
    }

    public static Color generateInGamutColor(ColorSpace source, ColorSpace dest) {
        ColorSpaceConverter cs = new ColorSpaceConverter(source, dest, false);

        while (true) {
            try {
                Color color = generateRandomColor();
                cs.convert(color);
                return color;
            } catch (OutsideGamutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Color generateRandomColor() {
        return new Color(
                Math.abs(random.nextInt()) % 256,
                Math.abs(random.nextInt()) % 256,
                Math.abs(random.nextInt()) % 256
        );
    }

}
