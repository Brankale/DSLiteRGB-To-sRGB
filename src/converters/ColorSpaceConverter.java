package converters;

import colorspaces.ColorSpace;
import colorspaces.utils.ChromaticAdaptation;
import colorspaces.utils.ColorCorrection;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;

public class ColorSpaceConverter {

    private final ColorSpace sourceCS;
    private final ColorSpace destCS;
    private final SimpleMatrix srcColorCorrectionMtx;
    private final SimpleMatrix destColorCorrectionMtx;
    private final SimpleMatrix chromaticAdaptationMtx;
    private final boolean clipColors;

    public ColorSpaceConverter(ColorSpace sourceCS, ColorSpace destCS, boolean clipColors) {
        this.sourceCS = sourceCS;
        this.destCS = destCS;
        this.clipColors = clipColors;
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

    public SimpleMatrix getSrcColorCorrectionMtx() {
        return srcColorCorrectionMtx;
    }

    public SimpleMatrix getDestColorCorrectionMtx() {
        return destColorCorrectionMtx;
    }

    public SimpleMatrix getChromaticAdaptationMtx() {
        return chromaticAdaptationMtx;
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
            SimpleMatrix XYZ = toCIEXYZ(color);
//            XYZ = chromaticAdaptation(chromaticAdaptationMtx, XYZ);
            return fromCIEXYZ(XYZ, clipColors);
        } catch (IllegalArgumentException e) {
            throw new OutsideGamutException();
        }
    }

    public SimpleMatrix toCIEXYZ(Color color) {
        color = sourceCS.beforeConversionTo(color);
        SimpleMatrix normalizedRGB = normalization(color);
        SimpleMatrix linearizedRGB = linearization(sourceCS, normalizedRGB);
        return RGBToXYZ(srcColorCorrectionMtx, linearizedRGB);
    }

    public Color fromCIEXYZ(SimpleMatrix XYZ, boolean clip) {
        SimpleMatrix srgb = XYZToRGB(destColorCorrectionMtx, XYZ);
        SimpleMatrix delinearizedRGB = delinearization(destCS, srgb);
        SimpleMatrix denormalizedRGB = denormalization(delinearizedRGB);

        Color convertedColor;

        if (clip) {
            convertedColor = new Color(
                    clip((int) denormalizedRGB.get(0, 0)),
                    clip((int) denormalizedRGB.get(1, 0)),
                    clip((int) denormalizedRGB.get(2, 0))
            );
        } else {
            convertedColor = new Color(
                    (int) denormalizedRGB.get(0, 0),
                    (int) denormalizedRGB.get(1, 0),
                    (int) denormalizedRGB.get(2, 0)
            );
        }

        convertedColor = destCS.afterConversionFrom(convertedColor);

        return convertedColor;
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

    /**
     * Performing a Chromatic Adaptation step means that the output colors are less accurate
     * but the final image should look equal to the original due to how eyes perceives different
     * white points.
     *
     * NB: If you want to perform accurate measurements, you MUSTN'T do this step!
     */
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

    public static SimpleMatrix denormalization(SimpleMatrix delinearizedRGB) {
        return new SimpleMatrix(3, 1, true, new double[] {
                denormalize(delinearizedRGB.get(0, 0)),
                denormalize(delinearizedRGB.get(1, 0)),
                denormalize(delinearizedRGB.get(2, 0))
        });
    }

    public static double normalize(int value) {
        return value / 255.0;
    }

    public static double denormalize(double value) {
        return value * 255.0;
    }

    public static int clip(double value) {
        return Math.max(0, Math.min((int) value, 255));
    }

}
