package converters;

import colorspaces.ColorSpace;
import colorspaces.utils.ChromaticAdaptation;
import colorspaces.utils.ColorCorrection;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.util.HashMap;

public class ColorSpaceConverter {

    private final ColorSpace sourceCS;
    private final ColorSpace destCS;
    private final SimpleMatrix srcColorCorrectionMtx;
    private final SimpleMatrix destColorCorrectionMtx;
    private final SimpleMatrix chromaticAdaptationMtx;

    public ColorSpaceConverter(ColorSpace sourceCS, ColorSpace destCS) {
        this.sourceCS = sourceCS;
        this.destCS = destCS;
        srcColorCorrectionMtx = createColorCorrectionMtx(sourceCS);
        destColorCorrectionMtx = createColorCorrectionMtx(destCS);
        chromaticAdaptationMtx = createChromaticAdaptationMtx(sourceCS, destCS);
    }

    public static SimpleMatrix createColorCorrectionMtx(ColorSpace colorSpace) {
        return ColorCorrection.getColorCorrectionMtx(
                colorSpace.r, colorSpace.g, colorSpace.b, colorSpace.w.toCIEXYZ()
        );
    }

    public static SimpleMatrix createChromaticAdaptationMtx(ColorSpace source, ColorSpace dest) {
        return ChromaticAdaptation.getChromaticAdaptationMtx(
                source.w.toCIEXYZ(),
                dest.w.toCIEXYZ()
        );
    }

    /**
     * @param color the color to convert to another colorspace
     * @throws OutsideGamutException if the converted color is outside the destination colorspace gamut
     *
     * @return the color in the destination colorspace
     */
    public Color convert(Color color) {

        // TODO: implement a cache for colors that works with
        //       changes on-the-fly like gamma correction

        try {
            SimpleMatrix normalizedRGB = normalization(color);
            SimpleMatrix linearizedRGB = linearization(sourceCS, normalizedRGB);
            SimpleMatrix XYZ = RGBToXYZ(srcColorCorrectionMtx, linearizedRGB);

            // if you perform conversion without chromatic adaptation,
            // XYZ values will be in a lot of cases out of range.
            XYZ = chromaticAdaptation(chromaticAdaptationMtx, XYZ);

            // even if chromatic adaptation is performed there are still some edge cases
            // that can be out of range for a very small error
            XYZ = CIEXYZ.fromSimpleMatrix(XYZ).toSimpleMatrix();

            SimpleMatrix srgb = XYZToRGB(destColorCorrectionMtx, XYZ);
            SimpleMatrix delinearizedRGB = delinearization(destCS, srgb);
            Color convertedColor = denormalization(delinearizedRGB);

            return convertedColor;

        } catch (IllegalArgumentException e) {
            throw new OutsideGamutException();
        }
    }

    public static SimpleMatrix normalization(Color color) {
        return new SimpleMatrix(3, 1, true, new double[] {
                normalize(color.getRed()),
                normalize(color.getGreen()),
                normalize(color.getBlue())
        });
    }

    public static SimpleMatrix linearization(ColorSpace source, SimpleMatrix normalizedRGB) {
        return new SimpleMatrix(3, 1, true, new double[] {
                source.inverseCompanding(normalizedRGB.get(0, 0),
                        ColorSpace.Channel.RED),
                source.inverseCompanding(normalizedRGB.get(1, 0),
                        ColorSpace.Channel.GREEN),
                source.inverseCompanding(normalizedRGB.get(2, 0),
                        ColorSpace.Channel.BLUE)
        });
    }

    public static SimpleMatrix RGBToXYZ(SimpleMatrix ccm, SimpleMatrix linearizedRGB) {
        return ccm.mult(linearizedRGB);
    }

    public static SimpleMatrix chromaticAdaptation(SimpleMatrix cam, SimpleMatrix XYZ) {
        return cam.mult(XYZ);
    }

    public static SimpleMatrix XYZToRGB(SimpleMatrix ccm, SimpleMatrix XYZ) {
        return ccm.invert().mult(XYZ);
    }

    public static SimpleMatrix delinearization(ColorSpace dest, SimpleMatrix rgb) {
        return new SimpleMatrix(3, 1, true, new double[] {
                dest.companding(rgb.get(0, 0), ColorSpace.Channel.RED),
                dest.companding(rgb.get(1, 0), ColorSpace.Channel.GREEN),
                dest.companding(rgb.get(2, 0), ColorSpace.Channel.BLUE)
        });
    }

    public static Color denormalization(SimpleMatrix delinearizedRGB) {
        return new Color(
                denormalize(delinearizedRGB.get(0, 0)),
                denormalize(delinearizedRGB.get(1, 0)),
                denormalize(delinearizedRGB.get(2, 0))
        );
    }

    public static double normalize(int value) {
        return value / 255.0;
    }

    public static int denormalize(double value) {
        return (int) (value * 255.0);
    }

}
