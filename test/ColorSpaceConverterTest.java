import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import converters.OutsideGamutException;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorSpaceConverterTest {

    private static final double DELTA = 0.000001;

    public static SRGB srgb = new SRGB();
    public static DSLite dsLite = new DSLite();
    public static ColorSpaceConverter csc = new ColorSpaceConverter(dsLite, srgb);

    @Test
    public void dslRedShouldBeInsideSRgbGamut() {
        csc.convert(Color.RED);
    }

    @Test
    public void dslGreenShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            csc.convert(Color.GREEN);
        });
    }

    @Test
    public void dslBlueShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            csc.convert(Color.BLUE);
        });
    }

    @Test
    public void dslRedShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = csc.toCIEXYZ(Color.RED);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.r.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.r.y, DELTA);
    }

    @Test
    public void dslGreenShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = csc.toCIEXYZ(Color.GREEN);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.g.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.g.y, DELTA);
    }

    @Test
    public void dslBlueShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = csc.toCIEXYZ(Color.BLUE);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.b.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.b.y, DELTA);
    }

    @Test
    public void dslWhiteShouldBeConvertedToItsChromaticityValue() {
        SimpleMatrix XYZ = csc.toCIEXYZ(Color.WHITE);
        CIEXYZ ciexyz = CIEXYZ.fromSimpleMatrix(XYZ);

        assertEquals(ciexyz.toCIExyY().x, dsLite.w.x, DELTA);
        assertEquals(ciexyz.toCIExyY().y, dsLite.w.y, DELTA);
    }

}
