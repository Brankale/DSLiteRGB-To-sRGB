package utils;

import colorspaces.DSLite;
import colorspaces.SRGB;
import colorspaces.utils.DeltaE;
import converters.ColorSpaceConverter;
import coordinates.data_types.CIELab;
import coordinates.data_types.CIEXYZ;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;

public class DeltaEChanges {

    enum Channel {
        RED, GREEN, BLUE
    }

    public static void main(String[] args) {
        drawDeltaEGraph(Channel.GREEN, 1.65, 1.8);
    }

    /**
     * Print the deltaE2000 function between all the pairs of DSLite colors
     * with two different gammas applied
     * @param channel RED, GREEN, BLUE
     * @param gamma1 the first gamma
     * @param gamma2 the second gamma
     */
    public static void drawDeltaEGraph(Channel channel, double gamma1, double gamma2) {
        SRGB srgb = new SRGB();
        DSLite dsLite = new DSLite();
        ColorSpaceConverter csc = new ColorSpaceConverter(dsLite, srgb, false);

        CIEXYZ[] xyz1 = getCIEXYZValues(csc, channel, gamma1);
        CIEXYZ[] xyz2 = getCIEXYZValues(csc, channel, gamma2);

        CIELab[] lab1 = new CIELab[64];
        CIELab[] lab2 = new CIELab[64];

        CIEXYZ white = dsLite.w.toCIEXYZ();
        for (int i = 0; i < 64; ++i) {
            lab1[i] = xyz1[i].toCIELab(white);
            lab2[i] = xyz2[i].toCIELab(white);
        }

        for (int i = 0; i < 64; ++i) {
            // implementation notes:
            // never use deltaE94 since it's less accurate, and it also gives
            // extremely different results if you swap the two CIELab values.
            // In some cases there's a difference of 14 of DeltaE which is massive!
            // Use always DeltaE2000 which is the most precise formula and it has not
            // the problem when swapping the two CIELab values.
            double deltaE = DeltaE.deltaE2000(lab1[i], lab2[i]);
            System.out.println("("+ i + "," + deltaE + ")");
        }
    }

    private static CIEXYZ[] getCIEXYZValues(ColorSpaceConverter csc, Channel channel, double gamma) {
        CIEXYZ[] xyz = new CIEXYZ[64];
        DSLite.GAMMA = gamma;

        for (int i = 0; i < 64; ++i) {
            SimpleMatrix m;
            switch (channel) {
                case RED:   m = csc.toCIEXYZ(new Color(i * 4, 0, 0)); break;
                case GREEN: m = csc.toCIEXYZ(new Color(0, i * 4, 0)); break;
                case BLUE:  m = csc.toCIEXYZ(new Color(0, 0, i * 4)); break;
                default: throw new IllegalArgumentException();
            }
            xyz[i] = CIEXYZ.fromSimpleMatrix(m);
        }
        return xyz;
    }

}
