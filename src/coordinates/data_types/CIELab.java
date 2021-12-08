package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public class CIELab implements ChromaticityCoord {

    public static final double e = 0.008856;
    public static final double k = 903.3;

    public double L;
    public double a;
    public double b;

    private final CIEXYZ white;

    public CIELab(double L, double a, double b, CIEXYZ white) {
        this.L = L;
        this.a = a;
        this.b = b;
        this.white = white;
    }

    @Override
    public CIELab toCIELab(CIEXYZ white) {
        return this;
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        double fy = (L + 16) / 116.0;
        double fx = (a / 500.0) + fy;
        double fz = fy - (b / 200.0);

        double xr, yr, zr;

        if (Math.pow(fx, 3) > e) {
            xr = Math.pow(fx, 3);
        } else {
            xr = (116 * fx  - 16) / k;
        }

        if (L > (k * e)) {
            yr = Math.pow(fy, 3);
        } else {
            yr = L / k;
        }

        if (Math.pow(fz, 3) > e) {
            zr = Math.pow(fz, 3);
        } else {
            zr = (116 * fz  - 16) / k;
        }

        return new CIEXYZ(
                xr * white.X,
                yr * white.Y,
                zr * white.Z
        );
    }

    @Override
    public CIExyY toCIExyY() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public CIELuv toCIELuv() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public SimpleMatrix toSimpleMatrix() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
