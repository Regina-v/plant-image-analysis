package de.bit.pl2.p3;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import inra.ijpb.label.LabelImages;
import inra.ijpb.util.ColorMaps;
import inra.ijpb.util.CommonColors;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImageSaver {
    /**
     * Saves all images from an imageList as png files in the given output folder.
     * @param imageList list of ImagePlus objects
     * @param outputDir output folder
     */
    public void saveImages(List<ImagePlus> imageList, File outputDir) {
        System.out.println("***** Saving images to " + outputDir.toString() + " *****");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        for (ImagePlus image : imageList) {
            FileSaver fs = new FileSaver(image);
            Path path = Paths.get(outputDir.toString(), image.getShortTitle() + ".png");
            fs.saveAsPng(String.valueOf(path));
        }
    }

    /**
     * Converts the output (greyscale label image) from the MorphoLibJ Distance Transform Watershed to a RGB image
     * and saves the RGB file in the given output folder.
     * @param imageList list of greyscale label images
     * @param outputDir output folder
     */
    public void saveColorLabel(List<ImagePlus> imageList, File outputDir) {
        System.out.println("***** Saving images to " + outputDir.toString() + " *****");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        for (ImagePlus image : imageList) {
            ImagePlus label = labelToRGB(image);
            FileSaver fs = new FileSaver(label);
            Path path = Paths.get(outputDir.toString(), image.getShortTitle() + ".png");
            fs.saveAsPng(String.valueOf(path));
        }
    }

    /**
     * Creates a new Color Image that associate a given color to each label of the input image.
     * The code was adopted from the MorphoLibJ plugin "LabelToRgbPlugin"
     * author David Legland
     * @param imp input greyscale label image
     */
    private ImagePlus labelToRGB(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        // compute max label
        int maxLabel = 0;
        for (int i = 0; i < ip.getPixelCount(); i++) {
            maxLabel = Math.max(maxLabel, (int) ip.getf(i));
        }

        // Create a new LUT
        String lutName = "Mixed Colors";
        String bgColorName = "Black";
        Color bgColor = CommonColors.fromLabel(bgColorName).getColor();
        boolean shuffleLut = true;
        byte[][] lut = ColorMaps.CommonLabelMaps.fromLabel(lutName).computeLut(maxLabel, shuffleLut);

        // Create a new RGB image from index image and LUT options
        ImagePlus resPlus = LabelImages.labelToRgb(imp, lut, bgColor);
        resPlus.copyScale(imp);
        return resPlus;
    }
}
