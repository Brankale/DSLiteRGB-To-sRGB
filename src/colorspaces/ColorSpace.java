package colorspaces;

import coordinates.data_types.CIExyY;

import java.awt.*;

public abstract class ColorSpace {

    public enum Channel {
        RED, GREEN, BLUE
    }

    public final CIExyY r, g, b, w;

    protected ColorSpace(CIExyY r, CIExyY g, CIExyY b, CIExyY w) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.w = w;
    }

    public Color beforeConversionTo(Color color) { return color; }
    public Color afterConversionFrom(Color color) { return color; }

    public abstract double inverseCompanding(double value, Channel channel);
    public abstract double companding(double value, Channel channel);

}
