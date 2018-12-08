package de.bit.pl2.p3;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import inra.ijpb.label.LabelImages;
import inra.ijpb.util.ColorMaps;
import inra.ijpb.util.CommonColors;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImageSaver {
    public void saveColorLabel(List<ImagePlus> imageList, File folder) {
         for (ImagePlus image : imageList) {
            ImagePlus label = labelToRGB(image);
            FileSaver fs = new FileSaver(label);
            Path path = Paths.get(folder.toString(), image.getShortTitle() + ".png");
            fs.saveAsPng(String.valueOf(path));
        }

        // todo for later: store in separate output folder
//        File parentFolder = folder.getParentFile();
//        try {
//            Path output = Paths.get(parentFolder.toString(), "output");
//            if (!Files.exists(output)) {
//                Files.createDirectory(output);
//            }
//            for (ImagePlus image : imageList) {
//                ImagePlus label = labelToRGB(image);
//                FileSaver fs = new FileSaver(label);
//                Path path = Paths.get(output.toString(), image.getShortTitle() + ".png");
//                fs.saveAsPng(String.valueOf(path));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Creates a new Color Image that associate a given color to each label of the input image.
     * The code was adopted from the MorphoLibJ plugin "LabelToRgbPlugin"
     * author David Legland
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
