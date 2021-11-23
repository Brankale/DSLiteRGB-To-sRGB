package colorspaces;

import coordinates.primaries.BT709;

public class SRGB extends ColorSpace {

    public SRGB() {
        super(
                BT709.red,
                BT709.green,
                BT709.blue,
                BT709.white
        );
    }

    @Override
    public double inverseCompanding(double value, Channel channel) {
        if (value <= 0.04045) {
            return value / 12.92;
        } else {
            return Math.pow((value + 0.055) / 1.055, 2.4);
        }
    }

    @Override
    public double companding(double value, Channel channel) {
        if (value <= 0.0031308) {
            return 12.92 * value;
        } else {
            return 1.055 * Math.pow(value, 1.0 / 2.4) - 0.055;
        }
    }

}
