import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import converters.OutsideGamutException;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorSpaceConverterTest {

    private static final double DELTA = 0.000001;

    public static SRGB srgb = new SRGB();
    public static DSLite dsLite = new DSLite();
    public static ColorSpaceConverter dslToSRgbConverter = new ColorSpaceConverter(dsLite, srgb);
    public static ColorSpaceConverter sRgbToDslConverter = new ColorSpaceConverter(srgb, dsLite);

    @Test
    public void dslBlackShouldBeEqualToSRgbBlack() {
        assertEquals(Color.BLACK, dslToSRgbConverter.convert(Color.BLACK));
    }

    /**
     * xy coords are inside sRGB gamut but Y should
     * also be considered, but it's unknown
     */
    @Ignore
    public void dslRedShouldBeInsideSRgbGamut() {
        dslToSRgbConverter.convert(Color.RED);
    }

    /**
     * xy coords are outside sRGB gamut ==> color is outside sRGB gamut
     */
    @Test
    public void dslGreenShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            dslToSRgbConverter.convert(Color.GREEN);
        });
    }

    /**
     * xy coords are outside sRGB gamut ==> color is outside sRGB gamut
     */
    @Test
    public void dslBlueShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            dslToSRgbConverter.convert(Color.BLUE);
        });
    }

    @Test
    public void sRgbRedShouldBeOutsideDslGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            sRgbToDslConverter.convert(Color.RED);
        });
    }

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

}
