package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public class CIEXYZ implements ChromaticityCoord {

    public final double X;
    public final double Y;
    public final double Z;

    public CIEXYZ(double X, double Y, double Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public static CIEXYZ fromSimpleMatrix(SimpleMatrix sm) {
        return new CIEXYZ(
                sm.get(0, 0),
                sm.get(1, 0),
                sm.get(2, 0)
        );
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        return this;
    }

    @Override
    public CIExyY toCIExyY() {
        return new CIExyY(
                X / (X + Y + Z),
                Y / (X + Y + Z),
                Y
        );
    }

    @Override
    public CIELab toCIELab(CIEXYZ white) {
        double xw = X / white.X;
        double yw = Y / white.Y;
        double zw = Z / white.Z;

        double fx, fy, fz;

        if (xw > CIELab.e) {
            fx = Math.cbrt(xw);
        } else {
            fx = (CIELab.k * xw + 16) / 116.0;
        }

        if (yw > CIELab.e) {
            fy = Math.cbrt(yw);
        } else {
            fy = (CIELab.k * yw + 16) / 116.0;
        }

        if (zw > CIELab.e) {
            fz = Math.cbrt(zw);
        } else {
            fz = (CIELab.k * zw + 16) / 116.0;
        }

        double L = 116 * fy - 16;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);

        return new CIELab(L, a, b, white);
    }

    @Override
    public CIELuv toCIELuv() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public SimpleMatrix toSimpleMatrix() {
        return new SimpleMatrix(3, 1, true, new double[] {
                X, Y, Z
        });
    }

    @Override
    public String toString() {
        return "CIEXYZ{" +
                "X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                '}';
    }
}
