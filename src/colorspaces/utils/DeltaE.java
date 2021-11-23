package colorspaces.utils;

import colorspaces.ColorSpace;
import converters.ColorSpaceConverter;
import coordinates.data_types.CIELab;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;

public class DeltaE {

    public static CIELab toCIELab(Color color, ColorSpace source) {
        SimpleMatrix normalizedRGB = ColorSpaceConverter.normalization(color);
        SimpleMatrix linearizedRGB = ColorSpaceConverter.linearization(source, normalizedRGB);
        SimpleMatrix XYZ = ColorSpaceConverter.RGBToXYZ(
                ColorSpaceConverter.createColorCorrectionMtx(source),
                linearizedRGB
        );
        return new CIEXYZ(
                XYZ.get(0,0),
                XYZ.get(1,0),
                XYZ.get(2,0)
        ).toCIELab(source.w.toCIEXYZ());
    }

    /**
     * Calculate eye perceptual difference between
     * two colors using Delta E 1994 standard.
     *
     * This more accurate than Delta E 1976.
     */
    public static double deltaE94(CIELab lab1, CIELab lab2) {
        double K1 = 0.045;
        double K2 = 0.015;

        double deltaA = lab1.a - lab2.a;
        double deltaB = lab1.b - lab2.b;

        double C1 = Math.sqrt(square(lab1.a) + square(lab1.b));
        double C2 = Math.sqrt(square(lab2.a) + square(lab2.b));

        double deltaL = lab1.L - lab2.L;
        double deltaC = C1 - C2;
        double deltaH = Math.sqrt(square(deltaA) + square(deltaB) - square(deltaC));

        double Kl = 1;
        double Kc = 1;
        double Kh = 1;

        double Sl = 1;
        double Sc = 1 + K1 * C1;
        double Sh = 1 + K2 * C2;

        return Math.sqrt(
                square(deltaL / (Kl * Sl)) + square(deltaC / (Kc * Sc)) + square(deltaH / (Kh * Sh))
        );
    }

    private static double square(double value) {
        return value * value;
    }

}
