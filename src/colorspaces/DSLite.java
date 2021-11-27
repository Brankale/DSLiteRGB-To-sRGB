package colorspaces;

import coordinates.data_types.CIExyY;

import java.awt.*;

public class DSLite extends ColorSpace {

    private final static CIExyY RED = new CIExyY(0.6068, 0.3449, 1.0);
    private final static CIExyY GREEN = new CIExyY(0.3314, 0.6124, 1.0);
    private final static CIExyY BLUE = new CIExyY(0.1451, 0.0893, 1.0);
    private final static CIExyY WHITE = new CIExyY(0.3093, 0.3193, 1.0);


    public static double OFFSET = 0.0;
    public static double GAMMA_R = 1.85 + OFFSET;   // <-- red : blue -->
    public static double GAMMA_G = 1.85 + OFFSET;   // <-- green : red -->
    public static double GAMMA_B = 1.85 + OFFSET;   // <-- blue : green -->

    public DSLite() {
        super(RED, GREEN, BLUE, WHITE);
    }

    @Override
    public Color beforeConversionTo(Color color) {
        return to6bit(color);
    }

    @Override
    public Color afterConversionFrom(Color color) {
        return to6bit(color);
    }

    /**
     * DS Lite screen can display just 6bit per channel
     */
    private Color to6bit(Color color) {
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
            case RED: return GAMMA_R;
            case GREEN: return GAMMA_G;
            case BLUE: return GAMMA_B;
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
