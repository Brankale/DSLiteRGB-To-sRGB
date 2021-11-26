package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

import java.util.Objects;

public class CIExyY implements ChromaticityCoord {

    public final double x;
    public final double y;
    public final double Y;
    public final double z;

    public CIExyY(double x, double y, double Y) {
        this.x = x;
        this.y = y;
        this.Y = Y;
        this.z = 1 - x - y;
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        return new CIEXYZ(
                (x * Y) / y,
                Y,
                (z * Y) / y
        );
    }

    @Override
    public CIExyY toCIExyY() {
        return this;
    }

    @Override
    public CIELab toCIELab(CIEXYZ white) {
        return toCIEXYZ().toCIELab(white);
    }

    @Override
    public CIELuv toCIELuv() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public SimpleMatrix toSimpleMatrix() {
        return new SimpleMatrix(3, 1, true, new double[] {
                x, y, Y
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CIExyY ciExyY = (CIExyY) o;
        return Double.compare(ciExyY.x, x) == 0 && Double.compare(ciExyY.y, y) == 0 && Double.compare(ciExyY.Y, Y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, Y);
    }

    @Override
    public String toString() {
        return "CIExyY{" +
                "x=" + x +
                ", y=" + y +
                ", Y=" + Y +
                '}';
    }
}
