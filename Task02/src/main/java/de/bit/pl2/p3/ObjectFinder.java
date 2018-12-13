package de.bit.pl2.p3;

import ij.IJ;
import ij.ImagePlus;

import java.util.ArrayList;
import java.util.List;


public class ObjectFinder {

    /**
     * Iterates over binary images in an imageList, finds Objects with watershed and returns a list of binary images
     * with added watershed lines
     *
     * @param imageList input list of binary images
     * @return output list of binary images with watershed line
     */
    public List<ImagePlus> parseList(List<ImagePlus> imageList) {
        System.out.println("***** Compute Watershed *****");
        List<ImagePlus> resultList = new ArrayList<>();
        for (ImagePlus image : imageList) {
            ImagePlus temp = removeOutliers(image);
            ImagePlus res = findObjects(temp);
            resultList.add(res);
        }
        return resultList;
    }

    /**
     * Applies ImageJ's Remove Outliers (selective median filter) function to input image
     * @param input ImagePlus input
     * @return ImagePlus output
     */
    public ImagePlus removeOutliers(ImagePlus input) {
        IJ.run(input, "Remove Outliers...", "radius=6 threshold=50 which=Dark");
        IJ.run(input, "Remove Outliers...", "radius=6 threshold=50 which=Bright");
        return input;
    }

    /**
     * Takes an image, applies watershed, adds watershed lines.
     *
     * @param inputPlus binary input image as ImagePlus instance
     * @return binary output with watershed lines
     */
    private ImagePlus findObjects(ImagePlus inputPlus) {
        // distance transform watershed to get greyscale labels
//            ImageProcessor largestLabelProcessor = largestLabel.getProcessor();
//            ImageProcessor dist = BinaryImages.distanceMap(largestLabelProcessor, new float[]{1, (float) Math.sqrt(2)}, true);
//            dist.invert();
//
//            ImageProcessor watershedProcessor = ExtendedMinimaWatershed.extendedMinimaWatershed(dist, largestLabelProcessor, 1, 4, 32, false);
//            resPlus = new ImagePlus(largestLabel.getShortTitle() + "-dist-watershed", watershedProcessor);
//            resPlus.copyScale(largestLabel);

        // watershed with binary output
        ImagePlus resPlus = inputPlus.duplicate();
        IJ.run(resPlus, "Watershed", "only");
        resPlus.setTitle(resPlus.getShortTitle() + "_watershed");
        return resPlus;
    }
}
