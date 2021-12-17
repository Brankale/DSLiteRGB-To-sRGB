package converters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FastDsToSRgb {

    private static final double[][] SRGB_CCM_INV = new double[][]{
            {+3.2406, -1.5372, -0.4986},
            {-0.9689, +1.8758, +0.0415},
            {+0.0557, -0.2040, +1.0570}
    };

    private static final double[][] DS_LITE_CCM = new double[][]{
            {0.422, 0.344, 0.203},
            {0.240, 0.635, 0.125},
            {0.034, 0.058, 1.071}
    };

    private static final double[][] MTX = new double[][] {
            {
                    SRGB_CCM_INV[0][0] * DS_LITE_CCM[0][0] + SRGB_CCM_INV[0][1] * DS_LITE_CCM[1][0] + SRGB_CCM_INV[0][2] * DS_LITE_CCM[2][0],
                    SRGB_CCM_INV[0][0] * DS_LITE_CCM[0][1] + SRGB_CCM_INV[0][1] * DS_LITE_CCM[1][1] + SRGB_CCM_INV[0][2] * DS_LITE_CCM[2][1],
                    SRGB_CCM_INV[0][0] * DS_LITE_CCM[0][2] + SRGB_CCM_INV[0][1] * DS_LITE_CCM[1][2] + SRGB_CCM_INV[0][2] * DS_LITE_CCM[2][2]
            },
            {
                    SRGB_CCM_INV[1][0] * DS_LITE_CCM[0][0] + SRGB_CCM_INV[1][1] * DS_LITE_CCM[1][0] + SRGB_CCM_INV[1][2] * DS_LITE_CCM[2][0],
                    SRGB_CCM_INV[1][0] * DS_LITE_CCM[0][1] + SRGB_CCM_INV[1][1] * DS_LITE_CCM[1][1] + SRGB_CCM_INV[1][2] * DS_LITE_CCM[2][1],
                    SRGB_CCM_INV[1][0] * DS_LITE_CCM[0][2] + SRGB_CCM_INV[1][1] * DS_LITE_CCM[1][2] + SRGB_CCM_INV[1][2] * DS_LITE_CCM[2][2]
            },
            {
                    SRGB_CCM_INV[2][0] * DS_LITE_CCM[0][0] + SRGB_CCM_INV[2][1] * DS_LITE_CCM[1][0] + SRGB_CCM_INV[2][2] * DS_LITE_CCM[2][0],
                    SRGB_CCM_INV[2][0] * DS_LITE_CCM[0][1] + SRGB_CCM_INV[2][1] * DS_LITE_CCM[1][1] + SRGB_CCM_INV[2][2] * DS_LITE_CCM[2][1],
                    SRGB_CCM_INV[2][0] * DS_LITE_CCM[0][2] + SRGB_CCM_INV[2][1] * DS_LITE_CCM[1][2] + SRGB_CCM_INV[2][2] * DS_LITE_CCM[2][2]
            }
    };

    private static final double DS_LITE_GAMMA_RED = 2.65;
    private static final double DS_LITE_GAMMA_GREEN = 2.75;
    private static final double DS_LITE_GAMMA_BLUE = 2.0;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            JOptionPane.showMessageDialog(null, "no arguments");
        } else {
            File in = new File(args[0]);
            convertImage(in);
        }
    }

    private static void convertImage(File in) throws IOException {
        if (!in.exists()) {
            JOptionPane.showMessageDialog(null, "file not found");
            return;
        }
        if (!in.isFile()) {
            JOptionPane.showMessageDialog(null, "not a file");
            return;
        }
        if (!in.canRead()) {
            JOptionPane.showMessageDialog(null, "can't read file");
            return;
        }

        File out = new File(in.getParent() + "/out.png");

        BufferedImage buf = ImageIO.read(in);
        Graphics2D g2d = buf.createGraphics();
        for (int width = 0; width < buf.getWidth(); ++width) {
            for (int height = 0; height < buf.getHeight(); ++height) {
                Color color = new Color(buf.getRGB(width, height));
                g2d.setColor(convertColor(color));
                g2d.fillRect(width, height, 1, 1);
            }
        }
        g2d.dispose();
        ImageIO.write(buf, "png", out);
    }

    private static Color convertColor(Color in) {
        Color color6bit = to6bit(in);

        // normalize
        double[] rgb = new double[] {
                color6bit.getRed() / 255.0,
                color6bit.getGreen() / 255.0,
                color6bit.getBlue() / 255.0
        };

        // linearize
        rgb = new double[] {
                Math.pow(rgb[0], DS_LITE_GAMMA_RED),
                Math.pow(rgb[1], DS_LITE_GAMMA_GREEN),
                Math.pow(rgb[2], DS_LITE_GAMMA_BLUE)
        };

        // DS Lite RGB -> XYZ
//        double[] XYZ = new double[] {
//                DS_LITE_CCM[0][0] * rgb[0] + DS_LITE_CCM[0][1] * rgb[1] + DS_LITE_CCM[0][2] * rgb[2],
//                DS_LITE_CCM[1][0] * rgb[0] + DS_LITE_CCM[1][1] * rgb[1] + DS_LITE_CCM[1][2] * rgb[2],
//                DS_LITE_CCM[2][0] * rgb[0] + DS_LITE_CCM[2][1] * rgb[1] + DS_LITE_CCM[2][2] * rgb[2],
//        };

        // XYZ -> sRGB
//        double[] srgb = new double[] {
//                SRGB_CCM_INV[0][0] * XYZ[0] + SRGB_CCM_INV[0][1] * XYZ[1] + SRGB_CCM_INV[0][2] * XYZ[2],
//                SRGB_CCM_INV[1][0] * XYZ[0] + SRGB_CCM_INV[1][1] * XYZ[1] + SRGB_CCM_INV[1][2] * XYZ[2],
//                SRGB_CCM_INV[2][0] * XYZ[0] + SRGB_CCM_INV[2][1] * XYZ[1] + SRGB_CCM_INV[2][2] * XYZ[2],
//        };

        // DS Lite RGB -> sRGB
        double[] srgb = new double[] {
                MTX[0][0] * rgb[0] + MTX[0][1] * rgb[1] + MTX[0][2] * rgb[2],
                MTX[1][0] * rgb[0] + MTX[1][1] * rgb[1] + MTX[1][2] * rgb[2],
                MTX[2][0] * rgb[0] + MTX[2][1] * rgb[1] + MTX[2][2] * rgb[2],
        };

        // delinearize
        srgb = new double[] {
                companding(srgb[0]),
                companding(srgb[1]),
                companding(srgb[2]),
        };

        srgb = new double[] {
                srgb[0] * 255,
                srgb[1] * 255,
                srgb[2] * 255,
        };

        return new Color(
                (int) Math.max(0, Math.min(srgb[0], 255)),
                (int) Math.max(0, Math.min(srgb[1], 255)),
                (int) Math.max(0, Math.min(srgb[2], 255))
        );
    }

    private static Color to6bit(Color color) {
        return new Color(
                color.getRed() & 0xFC,
                color.getGreen() & 0xFC,
                color.getBlue() & 0xFC
        );
    }

    public static double companding(double value) {
        if (value <= 0.0031308) {
            return 12.92 * value;
        } else {
            return 1.055 * Math.pow(value, 1.0 / 2.4) - 0.055;
        }
    }

}
