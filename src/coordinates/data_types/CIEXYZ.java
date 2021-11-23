package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public class CIEXYZ implements ChromaticityCoord {

    public final double X;
    public final double Y;
    public final double Z;

    public CIEXYZ(double X, double Y, double Z) {
        if (X < 0.0 || 0.95047 < X)
            throw new IllegalArgumentException("CIEXYZ: X = " + X + " is out of range ");
        if (Y < 0.0 || 1.0 < Y)
            throw new IllegalArgumentException("CIEXYZ: Y = " + Y + " is out of range ");
        if (Z < 0.0 || 1.08883 < Z)
            throw new IllegalArgumentException("CIEXYZ: Z = " + Z + " is out of range ");

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

    public boolean isInvalid() {
        if (X < 0.0 || 0.95047 < X)
            return true;
        if (Y < 0.0 || 1.0 < Y)
            return true;
        if (Z < 0.0 || 1.08883 < Z)
            return true;
        return false;
    }

    public CIEXYZ adjustValues() {
        double X = ChromaticityCoord.valueInRange(this.X, 0.0, 0.95047);
        double Y = ChromaticityCoord.valueInRange(this.Y, 0.0, 1.0);
        double Z = ChromaticityCoord.valueInRange(this.Z, 0.0, 1.08883);
        return new CIEXYZ(X, Y, Z);
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
            fx = Math.pow(xw, 1.0 / 3.0);
        } else {
            fx = (CIELab.k * xw + 16) / 116;
        }

        if (yw > CIELab.e) {
            fy = Math.pow(xw, 1.0 / 3.0);
        } else {
            fy = (CIELab.k * xw + 16) / 116;
        }

        if (zw > CIELab.e) {
            fz = Math.pow(xw, 1.0 / 3.0);
        } else {
            fz = (CIELab.k * xw + 16) / 116;
        }

        double L = 116 * fy - 16;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);

        return new CIELab(L, a, b);
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
