package de.bit.pl2.p3;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.io.FileSaver;
import ij.plugin.ChannelSplitter;
import ij.process.ImageProcessor;

import java.awt.*;


public class App 
{
    public static void main( String[] args )
    {
        System.setProperty("plugins.dir", "C:\\Users\\regin\\Fiji.app\\plugins");
        ImagePlus imp = IJ.openImage("C:\\Users\\regin\\Desktop\\plant022_rgb.png");

        ImageProcessor impProcessor = imp.getProcessor();
        IJ.run(imp, "Median...", "radius=2");
        ImagePlus[] channels = ChannelSplitter.split(imp);
        ImagePlus green = channels[1];
        ImageProcessor greenProcessor = green.getProcessor();
        IJ.run(green, "Auto Threshold", "method=Yen white");
        IJ.run(green, "Connected Components Labeling", "connectivity=4 type=[16 bits]");
        System.out.println(WindowManager.getWindowCount());
        //todo this shows that there are no windows, that's why label get nullpointer error

        ImagePlus label = WindowManager.getCurrentImage();
        ImageProcessor labelProcessor = label.getProcessor();
        label.show();
        IJ.run(label, "Keep Largest Label", "");
        IJ.run(label, "Distance Transform Watershed", "distances=[Quasi-Euclidean (1,1.41)] output=[32 bits] normalize dynamic=1 connectivity=4");
        IJ.run(label, "Labels To RGB", "colormap=[Mixed Colors] background=Black shuffle");
        green.show();

    }
}
