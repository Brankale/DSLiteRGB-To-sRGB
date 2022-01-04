package colorspaces;

import coordinates.data_types.CIExyY;

import java.awt.*;

public class DSLite extends ColorSpace {

    private final static CIExyY RED = new CIExyY(0.6068, 0.3449, 1.0);
    private final static CIExyY GREEN = new CIExyY(0.3314, 0.6124, 1.0);
    private final static CIExyY BLUE = new CIExyY(0.1451, 0.0893, 1.0);
    private final static CIExyY WHITE = new CIExyY(0.3093, 0.3193, 1.0);

    public static double GAMMA_R = 2.65;   // <-- red : blue -->
    public static double GAMMA_G = 2.75;   // <-- green : red -->
    public static double GAMMA_B = 2.0;    // <-- blue : green -->

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

    /**
     * @param value value of the color on a specific channel, value is between [0, 1]
     * @param channel
     * @return channel gamma
     */
    private double getGamma(double value, Channel channel) {
        switch (channel) {
            case RED: return getRedGamma(value);
            case GREEN: return getGreenGamma(value);
            case BLUE: return getBlueGamma(value);
            default: return getGreyGamma(value);
        }
    }

//    private double applyGamma(double value, double gamma) {
//        double perc = (value * (1 - Math.pow(value, gamma - 1))) / Math.pow((1 / gamma), 1 / (gamma - 1));
//        return (gamma * perc) + 2.2 * (1.0 - perc);
//    }
//
//    private double getGammaA(double value, Channel channel) {
//        switch (channel) {
//            case RED: return greyScaleGamma(value) + 0.5;
//            case GREEN: return greyScaleGamma(value) + 0.6;
//            case BLUE: return greyScaleGamma(value) - 0.1;
//            default: return 1.85;
//        }
//    }

//    private double greyScaleGamma(double value) {
//        final double A = -1.2329440680538717;
//        final double H = 3.078786460797779;
//        final double x0 = 0.470051355290925;
//        final double W = 1.1160685950193598;
//
//        return A + H * Math.exp(- Math.pow(value - x0, 2) / (2 * W*W));
//    }

    /*
     * NOTES ABOUT GAMMA FUNCTION:
     *
     * - the function cannot be a piecewise function otherwise
     *   you'll get terrible gradients (banding issues) which
     *   are not present on the DS Lite
     *
     * - the function can be an approximation of a piecewise
     *   function but depending on the approximation, the function
     *   can be significantly different from the real gamma
     *
     * - only the gamma of the greyscale is documented:
     *         1.0,            // 0.00 -> 0.05
     *         2.006465043,    // 0.05 -> 0.10
     *         1.855040371,    // 0.10 -> 0.15
     *         1.923901315,    // 0.15 -> 0.20
     *         1.844355523,    // 0.20 -> 0.25
     *         1.868117567,    // 0.25 -> 0.30
     *         1.933324852,    // 0.30 -> 0.35
     *         2.125580712,    // 0.35 -> 0.40
     *         1.927827728,    // 0.40 -> 0.45
     *         1.885737039,    // 0.45 -> 0.50
     *         1.637888673,    // 0.50 -> 0.55
     *         1.794104051,    // 0.55 -> 0.60
     *         1.773001944,    // 0.60 -> 0.65
     *         2.297985583,    // 0.65 -> 0.70
     *         1.234178603,    // 0.70 -> 0.75
     *         1.979039594,    // 0.75 -> 0.80
     *         1.638626526,    // 0.80 -> 0.85
     *         1.489711139,    // 0.85 -> 0.90
     *         1.837363028,    // 0.90 -> 0.95
     *         1.0             // 0.95 -> 1.00
     *
     * - Best channel gamma values:
     *   (2.65, 2.75, 2.0) // a little to blue-ish but it's the closest
     *   (2.3, 2.45, 1.85) // good "white balancing"
     */

    // ------------------- NEW System ----------------

    // f(x) = cd/m^2 normalized from (0.31 cd/m^2 - 200 cd/m^2) to (0, 1) range
    // x = channel value normalized from (0, 255) to (0, 1)
    // f(x) = −32.337x^8 + 119.839x^7 − 181.549x^6 + 144.668x^5 − 64.994x^4 + 15.946x^3 − 0.660x^2 + 0.089x + 0.001

    // gamma function:
    // gamma = log_x(f(x))


    // I need that ( redGamma() + greenGamma() + blueGamma() ) / 3 = greyGamma()
    // ==> blueGamma() = 3*greyGamma() - redGamma() - greenGamma()

    /**
     * a = 0.5, GREY_GAMMA = 0.4, OFFSET_RED = 0.35, OFFSET_GREEN = 0.45
     */

    private static final double GREY_GAMMA_OFFSET = 0.2;
    private static final double GAMMA_OFFSET_RED = 0.35; // red <- -> blue
    private static final double GAMMA_OFFSET_GREEN = 0.45; // yellow <- -> blue

    public static double getGreyGamma(double x) {
        double luminance = -32.337*pow(x,8) + 119.839*pow(x,7) - 181.549*pow(x,6)
                + 144.668*pow(x,5) - 64.994*pow(x,4) + 15.946*pow(x,3) - 0.660*pow(x,2) + 0.089*x + 0.001;

        // you cannot choose log with base x so I need the following calc
        double gamma = Math.log10(luminance) / Math.log10(x);

        return Math.max(1.0, gamma + GREY_GAMMA_OFFSET);
    }

    private static double pow(double x, int exp) {
        return Math.pow(x, exp);
    }

    public static double getGammaOffsetMultiplier(double x) {
        double a = 0.5;
        double multiplier = a * (getGreyGamma(x) - 1.0 - GREY_GAMMA_OFFSET);
        return Math.max(0.0, multiplier);
    }

    public static double getRedGamma(double x) {
        double gamma = getGreyGamma(x) + GAMMA_OFFSET_RED * getGammaOffsetMultiplier(x);
        return Math.max(1.0, gamma);
    }

    public static double getGreenGamma(double x) {
        double gamma =  getGreyGamma(x) + GAMMA_OFFSET_GREEN * getGammaOffsetMultiplier(x);
        return Math.max(1.0, gamma);
    }

    public static double getBlueGamma(double x) {
        double gamma =  getGreyGamma(x) * 3 - getRedGamma(x) - getGreenGamma(x);
        return Math.max(1.0, gamma);
    }


}
