package coordinates.data_types;

import org.ejml.simple.SimpleMatrix;

public interface  ChromaticityCoord {

    CIELab toCIELab(CIEXYZ white);
    CIELuv toCIELuv();
    CIExyY toCIExyY();
    CIEXYZ toCIEXYZ();
    SimpleMatrix toSimpleMatrix();

    static double valueInRange(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

}
