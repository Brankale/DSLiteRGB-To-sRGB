package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public class CIELab implements ChromaticityCoord {

    public static final double e = 0.008856;
    public static final double k = 903.3;

    public final double L;
    public final double a;
    public final double b;

    public CIELab(double L, double a, double b) {
        this.L = L;
        this.a = a;
        this.b = b;
    }

    @Override
    public CIELab toCIELab(CIEXYZ white) {
        return this;
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        throw new UnsupportedOperationException("not yet implemented");
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
