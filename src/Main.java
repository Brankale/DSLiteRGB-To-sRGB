import colorspaces.DSLite;
import colorspaces.SRGB;
import com.sun.istack.internal.NotNull;
import converters.ImageConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final File INPUT_DIR = new File("C:\\xampp\\htdocs\\ds\\imgs\\prev");
    private static final File OUTPUT_DIR = new File("C:\\xampp\\htdocs\\ds\\imgs\\after");

    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        List<File> images = getImagesInDir(INPUT_DIR);
        if (images != null)
            convertImages(images, OUTPUT_DIR);

        time = System.currentTimeMillis() - time;
        System.out.println(time);
    }

    public static List<File> getImagesInDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            List<File> images = new ArrayList<>();
            for (File file : files) {
                if (file.isFile())
                    images.add(file);
            }
            return images;
        }
        return null;
    }

    public static void convertImages(@NotNull List<File> images, @NotNull File outputDir) {
        ExecutorService es = Executors.newFixedThreadPool(4);

        for (File image : images) {
            es.execute(() -> {
                try {
                    convertImage(image, outputDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            es.shutdown();
            es.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void convertImage(File image, File outputDir) throws IOException {
        BufferedImage buf = ImageIO.read(image);
        buf = ImageConverter.convert(buf, new DSLite(), new SRGB());
        File outputFile = new File(outputDir.getAbsolutePath() + "\\" + image.getName());
        ImageIO.write(buf, "png", outputFile);
    }

    /*
     * IMPORTANT NOTES ABOUT CONVERSION:
     *
     * A good result has been obtained following these steps:
     * 1) DSL->XYZ
     * 2) XYZ->sRGB (using DSL companding functions)
     * NB: no CAM has been used
     *
     * Further tests have shown that with no CAM, XYZ coords have
     * invalid coords.
     */

}
