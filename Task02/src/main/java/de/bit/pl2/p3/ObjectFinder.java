package de.bit.pl2.p3;

import fiji.threshold.Auto_Threshold;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ChannelSplitter;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;
import inra.ijpb.plugins.DistanceTransformWatershed;
import inra.ijpb.watershed.ExtendedMinimaWatershed;


public class ObjectFinder {
    public void findObjects() {
        System.setProperty("plugins.dir", "C:\\Users\\regin\\Fiji.app\\plugins");
        ImagePlus imp = IJ.openImage("C:\\Users\\regin\\Desktop\\plant022_rgb.png");

        // apply median filter to reduce noise
        ImageProcessor impProcessor = imp.getProcessor();
        IJ.run(imp, "Median...", "radius=2");

        // split channels and get green channel
        ImagePlus[] channels = ChannelSplitter.split(imp);
        ImagePlus green = channels[1];

        // apply auto threshold with Yen to get binary image
        Auto_Threshold auto_threshold = new Auto_Threshold();
        auto_threshold.exec(green, "Yen", false, false, true, false, false, false);

        // get connected components
        ImagePlus componentLabel = BinaryImages.componentsLabeling(green, 4, 16);

        // keep largest label todo not best solution
        ImagePlus largestLabel = BinaryImages.keepLargestRegion(componentLabel);

        // distance transform watershed
        DistanceTransformWatershed distanceTransformWatershed = new DistanceTransformWatershed();
        ImageProcessor largestLabelProcessor = largestLabel.getProcessor();

        ImageProcessor dist = BinaryImages.distanceMap( largestLabelProcessor, new float[] {1, (float) Math.sqrt(2) }, true );
        dist.invert();

        ImageProcessor watershedProcessor = ExtendedMinimaWatershed.extendedMinimaWatershed(dist, largestLabelProcessor, 1, 4, 32, false );
        ImagePlus resPlus = new ImagePlus(largestLabel.getShortTitle()+"-dist-watershed", watershedProcessor);
        resPlus.copyScale(largestLabel);
        resPlus.show();

        // for later analysis this step is not needed, only for output
//        IJ.run(label, "Labels To RGB", "colormap=[Mixed Colors] background=Black shuffle");
    }
}
