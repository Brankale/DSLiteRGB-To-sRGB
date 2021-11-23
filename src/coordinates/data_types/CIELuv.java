package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public class CIELuv implements ChromaticityCoord {

    @Override
    public CIELab toCIELab(CIEXYZ white) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public CIELuv toCIELuv() {
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
    public SimpleMatrix toSimpleMatrix() {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
