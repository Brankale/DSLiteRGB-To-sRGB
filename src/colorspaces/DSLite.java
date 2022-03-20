package colorspaces;

import coordinates.data_types.CIExyY;

import java.awt.*;
import java.util.ArrayList;

public class DSLite extends ColorSpace {

    private final static CIExyY RED = new CIExyY(0.6068, 0.3449, 1.0);
    private final static CIExyY GREEN = new CIExyY(0.3314, 0.6124, 1.0);
    private final static CIExyY BLUE = new CIExyY(0.1451, 0.0893, 1.0);
    private final static CIExyY WHITE = new CIExyY(0.3093, 0.3193, 1.0);

    public static double GAMMA_R = 2.65;   // <-- red : blue -->
    public static double GAMMA_G = 2.75;   // <-- green : red -->
    public static double GAMMA_B = 2.0;    // <-- blue : green -->

    public static double GAMMA = 2.2;

//    public static double[] GAMMA_RED = {
//            1.70, 1.70, 1.75, 1.80, 1.85, 1.90, 1.95, 2.00,
//            2.05, 2.10, 2.10, 2.15, 2.15, 2.15, 2.10, 2.05,
//            2.00, 1.97, 1.95, 1.95, 1.95, 1.95, 1.95, 1.95,
//            1.95, 1.97, 2.00, 2.03, 2.05, 2.05, 2.05, 2.05,
//            2.05, 2.05, 2.05, 2.05, 2.05, 2.05, 2.05, 2.05,
//            2.03, 2.00, 1.95, 1.90, 1.83, 1.76, 1.69, 1.62,
//            1.55, 1.48, 1.41, 1.34, 1.27, 1.20, 1.13, 1.06,
//            1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00
//    };

    public static double[] GAMMA_RED = {
            1.70, 1.70, 1.75, 1.80, 1.85, 1.90, 1.95, 2.00,     // 0 -> 28
            2.05, 2.10, 2.10, 2.15, 2.15, 2.15, 2.12, 2.07,     // 32 -> 60
            2.03, 2.00, 1.97, 1.95, 1.93, 1.90, 1.90, 1.93,     // 64 -> 92
            1.95, 1.95, 2.00, 2.03, 2.05, 2.07, 2.10, 2.10,     // 96 -> 124
            2.10, 2.10, 2.10, 2.10, 2.10, 2.10, 2.10, 2.10,     // 128 -> 156
            2.07, 2.05, 2.03, 1.97, 1.92, 1.87, 1.80, 1.75,     // 160 -> 188
            1.70, 1.65, 1.60, 1.55, 1.50, 1.45, 1.40, 1.35,     // 192 -> 220
            1.30, 1.25, 1.20, 1.15, 1.10, 1.05, 1.00, 1.00      // 224 -> 252
    };

    public static double[] GAMMA_RED_2 = {
            1.00, 1.00, 1.10, 1.40, 1.30, 1.50, 1.80, 1.65,
            1.80, 1.90, 1.90, 1.90, 2.00, 2.20, 2.00, 1.90,
            1.80, 1.80, 1.80, 1.70, 1.80, 1.80, 1.90, 1.80,
            1.90, 2.00, 2.00, 2.00, 2.05, 2.10, 2.10, 2.00,
            2.10, 2.00, 2.10, 2.10, 2.00, 2.00, 2.00, 2.10,
            2.00, 2.00, 2.00, 1.90, 1.80, 1.70, 1.65, 1.30,
            1.20, 1.20, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
            1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00
    };

    public static double[] GAMMA_GREEN = {
            1.00, 1.30, 1.50, 1.60, 1.70, 1.80, 1.85, 1.90,     // 0 -> 28
            1.95, 1.97, 1.95, 1.95, 1.95, 1.95, 1.95, 1.95,     // 32 -> 60
            1.90, 1.87, 1.85, 1.85, 1.85, 1.87, 1.90, 1.95,     // 64 -> 92
            2.00, 2.01, 2.02, 2.03, 2.04, 2.05, 2.06, 2.07,     // 96 -> 124
            2.08, 2.09, 2.10, 2.07, 2.05, 2.03, 2.00, 2.00,     // 128 -> 156
            2.00, 1.95, 1.90, 1.85, 1.80, 1.75, 1.65, 1.60,     // 160 -> 188
            1.55, 1.45, 1.40, 1.35, 1.30, 1.25, 1.20, 1.15,     // 192 -> 220
            1.10, 1.05, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00      // 224 -> 252
    };

    public static double[] GAMMA_BLUE = {
            1.00, 1.00, 1.10, 1.20, 1.30, 1.50, 1.50, 1.60,
            1.80, 1.90, 1.80, 1.90, 2.00, 1.90, 2.00, 1.90,
            1.80, 1.70, 1.60, 1.60, 1.50, 1.70, 1.80, 1.70,
            1.80, 1.90, 1.90, 1.90, 1.95, 1.90, 1.90, 1.90,
            1.90, 1.95, 1.90, 1.90, 1.90, 1.80, 1.70, 1.50,
            1.30, 1.20, 1.10, 1.00, 1.00, 1.00, 1.00, 1.00,
            1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
            1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00
    };


    public static void main(String[] args) {
        for (int i = 0; i < 64; ++i) {
//            System.out.println("(" + (i * 4) / 255.0 + ", " + GAMMA_GREEN[i] + ")");
            System.out.println("(" + ((i * 4) / 255.0) + ", " + Math.pow((i * 4)/255.0 , GAMMA_RED_2[i]) + ")");
        }
    }

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
//        return GAMMA;
        switch (channel) {
            case RED:
//                return getRedGamma(value);
//                return GAMMA_RED[(int)(value * 255 / 4)];
//                return 2.05;
                return getRed(value);
            case GREEN:
//                return GAMMA_GREEN[(int)(value * 255 / 4)];
//                return 2.03;
                return getGreen(value);
            case BLUE:
//                return GAMMA_BLUE[(int)(value * 255 / 4)];
                return getBlue(value);
//            default: return getGreyGamma(value);
        }
        throw new RuntimeException();
    }

    public double getRed(double value) {
        if (value < 0.65)
            return 2.05;
        else if (value >= 0.65)
            return 1.05 * Math.exp(-10 * (value - 0.65)) + 1.4;
        throw new RuntimeException();
    }

    public double getGreen(double value) {
        if (value < 0.63)
            return 2.03;
        else if (value >= 0.63)
            return 1.03 * Math.exp(-4.1 * (value - 0.63)) + 1.4;
        throw new RuntimeException();
    }

    public double getBlue(double value) {
        if (value < 0.57)
            return 1.9;
        else if (value >= 0.57)
            return 0.9 * Math.exp(-8.8 * (value - 0.57)) + 1.4;
        throw new RuntimeException();
    }

    public double gaussian(double value, double mu, double sigma) {
        return (1.0 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(value - mu, 2) / (2 * Math.pow(sigma, 2)) );
    }

    // TODO: NEW METHOD WITH GAMMA CARDS

    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }


    public static double getRedGamma(double value) {

        // OLD
//        value *= 255.0;
//        double gamma = 0.0000000000426 * Math.pow(value, 5)
//                - 0.0000000261846 * Math.pow(value, 4)
//                + 0.0000059553663 * Math.pow(value, 3)
//                - 0.0006779694306 * Math.pow(value, 2)
//                + 0.0399099809983 * value
//                + 0.9542674730971;

        // NEW

        double gamma = 131.8640 * Math.pow(value, 8)
                - 329.4449 * Math.pow(value, 7)
                + 198.7624 * Math.pow(value, 6)
                + 116.4504 * Math.pow(value, 5)
                - 179.0315 * Math.pow(value, 4)
                + 72.1532 * Math.pow(value, 3)
                - 10.2985 * Math.pow(value, 2)
                + 0.5900 * value
                - 0.0031;

        if (0.075 <= value && value <= 0.255) {
            gamma = 204956.453517 * Math.pow(value, 8)
                - 606122.863218 * Math.pow(value, 7)
                + 465646.071153 * Math.pow(value, 6)
                - 164151.387667 * Math.pow(value, 5)
                + 31059.749941 * Math.pow(value, 4)
                - 3284.034876 * Math.pow(value, 3)
                + 188.997901 * Math.pow(value, 2)
                - 5.256461 * value
                + 0.058823;
        }

        return Math.max(gamma, 1.0);
    }

    public static double getGreenGamma(double value) {
//        value *= 255.0;
//
//        double gamma = 0.0000000000868 * Math.pow(value, 5)
//                - 0.0000000513306 * Math.pow(value, 4)
//                + 0.0000108596668 * Math.pow(value, 3)
//                - 0.0010509450228 * Math.pow(value, 2)
//                + 0.0465724643645 * value
//                + 0.9925945272027;
//
//        gamma = Math.max(gamma, 1.0);
//        return gamma;

        return 3 * getGreyGamma(value) - getRedGamma(value) - getBlueGamma(value);
    }

    public static double getBlueGamma(double value) {
        value *= 255.0;
        double gamma = 0.000000000131 * Math.pow(value, 5)
                - 0.000000077001 * Math.pow(value, 4)
                + 0.000016069774 * Math.pow(value, 3)
                - 0.001494128188 * Math.pow(value, 2)
                + 0.060706079415 * value
                + 0.858453709717;
        return gamma;
    }

    private static double interpolateGamma(double value, ArrayList<Pair<Integer, Double>> gamma) {
        for (int i = 0; i < gamma.size() - 1; ++i) {
            double tmp1 = gamma.get(i).first / 255.0;
            double tmp2 = gamma.get(i + 1).first / 255.0;

            double c = 1.0 / (tmp2 - tmp1);

            if (tmp1 <= value && value <=  tmp2) {
                return c * (value - tmp1) * gamma.get(i).second + c * (tmp2 - value) * gamma.get(i + 1).second;
            }
        }
        throw new RuntimeException();
    }




    // TODO: END



//    private double getGamma(double value, Channel channel) {
//        switch (channel) {
//            case RED: return 2.25 * (1 - value) + 2.1 * value;
//            case GREEN: return 2.1;
//            case BLUE: return 2.1;
//            default: return getGreyGamma(value);
//        }
//    }

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


    // TODO: from here there is the approximation of the luminance function

//    private static final double GREY_GAMMA_OFFSET = 0.4;
//    private static final double GAMMA_OFFSET_RED = 0.3; // red <- -> blue
//    private static final double GAMMA_OFFSET_GREEN = 0.65; // yellow <- -> blue
//
    public static double getGreyGamma(double x) {
        double luminance = -32.337*pow(x,8) + 119.839*pow(x,7) - 181.549*pow(x,6)
                + 144.668*pow(x,5) - 64.994*pow(x,4) + 15.946*pow(x,3) - 0.660*pow(x,2) + 0.089*x + 0.001;

        // you cannot choose log with base x so I need the following calc
        double gamma = Math.log10(luminance) / Math.log10(x);

        return Math.max(1.0, gamma);
    }

    private static double pow(double x, int exp) {
        return Math.pow(x, exp);
    }
//
//    public static double getGammaOffsetMultiplier(double x) {
//        double a = 0.5;
//        double multiplier = a * (getGreyGamma(x) - 1.0 - GREY_GAMMA_OFFSET);
//        return Math.max(0.0, multiplier);
//    }
//
//    public static double getRedGamma(double x) {
//        double gamma = getGreyGamma(x) + GAMMA_OFFSET_RED * getGammaOffsetMultiplier(x);
//        return Math.max(1.0, gamma);
//    }
//
//    public static double getGreenGamma(double x) {
//        double gamma =  getGreyGamma(x) + GAMMA_OFFSET_GREEN * getGammaOffsetMultiplier(x);
//        return Math.max(1.0, gamma);
//    }
//
//    public static double getBlueGamma(double x) {
//        double gamma =  getGreyGamma(x) * 3 - getRedGamma(x) - getGreenGamma(x);
//        return Math.max(1.0, gamma);
//    }


}
