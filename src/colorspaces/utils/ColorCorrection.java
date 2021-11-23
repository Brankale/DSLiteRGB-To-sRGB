package colorspaces.utils;

import coordinates.data_types.CIEXYZ;
import coordinates.data_types.CIExyY;
import org.ejml.simple.SimpleMatrix;

public class ColorCorrection {

    private ColorCorrection() {
        // avoid instantiation
    }

    /**
     * @param red Y coord can be anything
     * @param green Y coord can be anything
     * @param blue Y coord can be anything
     * @param white Y coord can be anything
     */
    public static SimpleMatrix getColorCorrectionMtx(
            CIExyY red,
            CIExyY green,
            CIExyY blue,
            CIEXYZ white
    ) {
        CIEXYZ r = new CIEXYZ(red.x / red.y, 1, red.z / red.y);
        CIEXYZ g = new CIEXYZ(green.x / green.y, 1, green.z / green.y);
        CIEXYZ b = new CIEXYZ(blue.x / blue.y, 1, blue.z / blue.y);

        SimpleMatrix tmp = new SimpleMatrix(3, 3, true, new double[] {
                r.X, g.X, b.X,
                r.Y, g.Y, b.Y,
                r.Z, g.Z, b.Z
        });

        SimpleMatrix s = tmp.invert().mult(white.toSimpleMatrix());

        return new SimpleMatrix(3, 3, true, new double[] {
                s.get(0, 0) * r.X, s.get(1, 0) * g.X, s.get(2, 0) * b.X,
                s.get(0, 0) * r.Y, s.get(1, 0) * g.Y, s.get(2, 0) * b.Y,
                s.get(0, 0) * r.Z, s.get(1, 0) * g.Z, s.get(2, 0) * b.Z
        });
    }

}
