package colorspaces;

import coordinates.data_types.CIExyY;

import java.awt.*;

public class DSLite extends ColorSpace {

    public static double GAMMA_RED = 1.85;
    public static double GAMMA_GREEN = 1.85;
    public static double GAMMA_BLUE = 1.85;

    public DSLite() {
        super(
                new CIExyY(0.6068, 0.3449, 1.0),
                new CIExyY(0.3314, 0.6124, 1.0),
                new CIExyY(0.1451, 0.0893, 1.0),
                new CIExyY(0.3093, 0.3193, 1.0)
        );
    }

    @Override
    public Color beforeConversionTo(Color color) {
        // DS Lite has a 6bit per channel display
        return new Color(
                color.getRed() & 0xFC,
                color.getGreen() & 0xFC,
                color.getBlue() & 0xFC
        );
    }

    @Override
    public Color afterConversionFrom(Color color) {
        // DS Lite has a 6bit per channel display
        return new Color(
                color.getRed() & 0xFC,
                color.getGreen() & 0xFC,
                color.getBlue() & 0xFC
        );
    }

    @Override
    public double inverseCompanding(double value, Channel channel) {
        return Math.pow(value, getGamma(value, channel));
    }

    @Override
    public double companding(double value, Channel channel) {
        return Math.pow(value, 1.0 / getGamma(value, channel));
    }

    private double getGamma(double value, Channel channel) {
        switch (channel) {
            case RED: return GAMMA_RED;
            case GREEN: return GAMMA_GREEN;
            case BLUE: return GAMMA_BLUE;
            default: return getGreyScaleGamma(value);
        }
    }

    private double getGreyScaleGamma(double value) {
        value = (value * 100.0) / 255.0;

        if (value < 10.0) {
            return 2.035;
        } else if (value < 30.0) {
            return 1.86;
        } else if (value < 40.0) {
            return 2.026;
        } else if (value < 70.0) {
            return 1.82;
        } else {
            return 1.57;
        }
    }

}
