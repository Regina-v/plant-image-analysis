package de.bit.pl2.p3;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ij.IJ;
import ij.ImagePlus;

public class ImageAnalyzerTest {
    @Test
    public void testAnalyze() throws IOException {
        ImageAnalyzer analyzer = new ImageAnalyzer();
        analyzer.analyzeImage(loadImage("plant027_rgb.png"), loadImage("plant027_rgb_class_watershed.png"));
        analyzer.writeResultsToCSVFile(new File("c:\\temp\\analyzer_output.csv"));
    }

    private ImagePlus loadImage(String name) throws IOException
    {
        File file = new File(this.getClass().getResource("/analyzer/" + name).getPath());
        ImagePlus image = IJ.openImage(file.getCanonicalPath());
        return image;
    }
}
