import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import converters.OutsideGamutException;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorSpaceConverterTest {

    public static SRGB srgb = new SRGB();
    public static DSLite dsLite = new DSLite();

    @Test
    public void dslRedShouldBeInsideSRgbGamut() {
        ColorSpaceConverter csc = new ColorSpaceConverter(dsLite, srgb);
        csc.convert(Color.RED);
    }

    @Test
    public void dslGreenShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            ColorSpaceConverter csc = new ColorSpaceConverter(dsLite, srgb);
            csc.convert(Color.GREEN);
        });
    }

    @Test
    public void dslBlueShouldBeOutsideSRgbGamut() {
        assertThrows(OutsideGamutException.class, () -> {
            ColorSpaceConverter csc = new ColorSpaceConverter(dsLite, srgb);
            csc.convert(Color.BLUE);
        });
    }

}
