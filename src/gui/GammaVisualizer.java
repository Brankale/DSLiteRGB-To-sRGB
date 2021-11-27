package gui;

import colorspaces.DSLite;
import colorspaces.SRGB;
import converters.ColorSpaceConverter;
import converters.ImageConverter;
import gui.components.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GammaVisualizer extends JFrame {

    private static final String INPUT_IMAGE_PATH =
            "C:\\xampp\\htdocs\\ds\\imgs\\prev\\img2.png";

    private static final ColorSpaceConverter converter =
            new ColorSpaceConverter(new DSLite(), new SRGB());

    private BufferedImage bufferedImage;

    private JPanel root;
    private JSlider gammaRed;
    private JSlider gammaGreen;
    private JSlider gammaBlue;
    private ImagePanel imageOriginal;
    private ImagePanel imageModified;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new GammaVisualizer();
    }

    public GammaVisualizer() {
        Dimension dimension = new Dimension(30, 350);
        gammaRed.setPreferredSize(dimension);
        gammaGreen.setPreferredSize(dimension);
        gammaBlue.setPreferredSize(dimension);

        updateImage();

        gammaRed.addChangeListener(e -> {
            int newValue = gammaRed.getValue();
            DSLite.GAMMA_R = newValue / 100.0;
            updateImage();
        });
        gammaGreen.addChangeListener(e -> {
            int newValue = gammaGreen.getValue();
            DSLite.GAMMA_G = newValue / 100.0;
            updateImage();
        });
        gammaBlue.addChangeListener(e -> {
            int newValue = gammaBlue.getValue();
            DSLite.GAMMA_B = newValue / 100.0;
            updateImage();
        });

        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void createUIComponents() throws IOException {
        File file = new File(INPUT_IMAGE_PATH);
        if (!file.exists())
            throw new FileNotFoundException();
        bufferedImage = ImageIO.read(file);

        imageOriginal = new ImagePanel(bufferedImage);
        imageModified = new ImagePanel(bufferedImage);
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void updateImage() {
        BufferedImage copy = deepCopy(bufferedImage);
        ImageConverter.convert(copy, converter);
        imageModified.setImage(copy);
        imageModified.repaint();
    }

}
