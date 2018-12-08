package de.bit.pl2.p3;

import fiji.threshold.Auto_Threshold;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ChannelSplitter;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;
import inra.ijpb.watershed.ExtendedMinimaWatershed;
import inra.ijpb.plugins.DistanceTransformWatershed;

import java.util.ArrayList;
import java.util.List;


public class ObjectFinder {

    public List<ImagePlus> parseList(List<ImagePlus> imageList, boolean output) {
        List<ImagePlus> resultList = new ArrayList<>();
        for (ImagePlus image : imageList) {
            ImagePlus res = findObjects(image, output);
            resultList.add(res);
        }
        return resultList;
    }

    private ImagePlus findObjects(ImagePlus imp, boolean output) {
        // apply median filter to reduce noise
        ImageProcessor impProcessor = imp.getProcessor();
        IJ.run(imp, "Median...", "radius=2");

        // split channels and get green channel
        ImagePlus[] channels = ChannelSplitter.split(imp);
        ImagePlus green = channels[1];
        green.setTitle(imp.getShortTitle() + "-green");

        // apply auto threshold with Yen to get binary image
        Auto_Threshold auto_threshold = new Auto_Threshold();
        auto_threshold.exec(green, "Yen", false, false, true, false, false, false);

        // get connected components
        ImagePlus componentLabel = BinaryImages.componentsLabeling(green, 4, 16);
        componentLabel.setTitle(green.getShortTitle() + "-lbl");

        // keep largest label todo not best solution
        ImagePlus largestLabel = BinaryImages.keepLargestRegion(componentLabel);
        largestLabel.setTitle(componentLabel.getShortTitle() + "-largest");
        ImagePlus resPlus;

        if (output) {
            // distance transform watershed to get greyscale labels
            ImageProcessor largestLabelProcessor = largestLabel.getProcessor();
            ImageProcessor dist = BinaryImages.distanceMap(largestLabelProcessor, new float[]{1, (float) Math.sqrt(2)}, true);
            dist.invert();

            ImageProcessor watershedProcessor = ExtendedMinimaWatershed.extendedMinimaWatershed(dist, largestLabelProcessor, 1, 4, 32, false);
            resPlus = new ImagePlus(largestLabel.getShortTitle() + "-dist-watershed", watershedProcessor);
            resPlus.copyScale(largestLabel);
        } else {
            // distance transform watershed with binary output
            resPlus = largestLabel.duplicate();
            IJ.run(resPlus,"Watershed", "only");
        }

        return resPlus;
    }
}
