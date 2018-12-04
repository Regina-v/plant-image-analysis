package de.bit.pl2.p3;

import fiji.threshold.Auto_Threshold;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ChannelSplitter;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;


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
        
//        IJ.run(label, "Distance Transform Watershed", "distances=[Quasi-Euclidean (1,1.41)] output=[32 bits] normalize dynamic=1 connectivity=4");
//        IJ.run(label, "Labels To RGB", "colormap=[Mixed Colors] background=Black shuffle");
    }
}
