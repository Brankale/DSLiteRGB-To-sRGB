package colorspaces.utils;

import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;

public class ChromaticAdaptation {

    private static final SimpleMatrix XYZ_SCALING = new SimpleMatrix(3, 3, true, new double[] {
            1.0, 0.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 0.0, 1.0
    });

    private static final SimpleMatrix VON_KRIES = new SimpleMatrix(3, 3, true, new double[] {
            +0.40024,  +0.70760, -0.08081,
            -0.22630,  +1.16532, +0.04570,
            +0.00000,  +0.00000, +0.91822,
    });

    /**
     * This is considered the best transform mtx of the three
     */
    private static final SimpleMatrix BRADFORD = new SimpleMatrix(3, 3, true, new double[] {
            +0.8951, +0.2664, -0.1614,
            -0.7502, +1.7135, +0.0367,
            +0.0389, -0.0685, +1.0296
    });

    private ChromaticAdaptation() {
        // avoid instantiation
    }

    public static SimpleMatrix getChromaticAdaptationMtx(
            CIEXYZ sourceWhite,
            CIEXYZ destWhite
    ) {
        SimpleMatrix srcLMS =
                BRADFORD.mult(sourceWhite.toSimpleMatrix());
        SimpleMatrix destLMS =
                BRADFORD.mult(destWhite.toSimpleMatrix());

        SimpleMatrix tmp = new SimpleMatrix(3, 3, true, new double[] {
                destLMS.get(0, 0) / srcLMS.get(0, 0), 0.0, 0.0,
                0.0, destLMS.get(1, 0) / srcLMS.get(1, 0), 0.0,
                0.0, 0.0, destLMS.get(2, 0) / srcLMS.get(2, 0)
        });

        return BRADFORD.invert().mult(tmp).mult(BRADFORD);
    }

}
