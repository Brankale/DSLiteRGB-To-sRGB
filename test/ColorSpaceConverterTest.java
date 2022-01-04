import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColorSpaceConverterTest {

    private static final double DELTA = 0.00001;

    public static SRGB srgb = new SRGB();
    public static DSLite dsLite = new DSLite();
    public static ColorSpaceConverter dslToSRgbConverter = new ColorSpaceConverter(dsLite, srgb, false);

    @Test
    public void dslRedShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = dslToSRgbConverter.toCIEXYZ(Color.RED);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.r.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.r.y, DELTA);
    }

    @Test
    public void dslGreenShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = dslToSRgbConverter.toCIEXYZ(Color.GREEN);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.g.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.g.y, DELTA);
    }

    @Test
    public void dslBlueShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = dslToSRgbConverter.toCIEXYZ(Color.BLUE);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.b.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.b.y, DELTA);
    }

    @Test
    public void dslWhiteShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = dslToSRgbConverter.toCIEXYZ(Color.WHITE);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.w.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.w.y, DELTA);
    }

    @Test
    public void averageOfChannelGammaMustBeEqualToGreyscaleGamma() {
        for (int i = 0; i <= 255; i += 4) {
            double normalizedColor = i / 255.0;
            double gammaRed = DSLite.getRedGamma(normalizedColor);
            double gammaGreen = DSLite.getGreenGamma(normalizedColor);
            double gammaBlue = DSLite.getBlueGamma(normalizedColor);
            double gammaGrey = DSLite.getGreyGamma(normalizedColor);
            assertEquals(gammaGrey, (gammaRed + gammaGreen + gammaBlue) / 3.0, DELTA);
        }
    }

    @Test
    public void dslBlackShouldBeEqualToSRgbBlack() {
        assertEquals(Color.BLACK, dslToSRgbConverter.convert(Color.BLACK));
    }

    @Test
    public void darkestRedMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(4, 0, 0);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

    // TODO: should be outside the gamut?
    @Test
    public void darkestGreenMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(0, 4, 0);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

    // TODO: should be outside the gamut?
    @Test
    public void darkestBlueMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(0, 0, 4);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

    // TODO: should be outside the gamut?
    @Test
    public void darkestCyanMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(0, 4, 4);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

    @Test
    public void darkestMagentaMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(4, 0, 4);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

    @Test
    public void darkestYellowMustBeDifferentFromBlackAfterConversion() {
        Color color = new Color(4, 4, 0);
        assertNotEquals(Color.BLACK, dslToSRgbConverter.convert(color));
    }

}
