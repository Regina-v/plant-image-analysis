package de.bit.pl2.p3;

import ij.IJ;
import ij.ImagePlus;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ObjectAnalyzerTest {
    @Test
    public void testAnalyze() throws IOException {
        ObjectAnalyzer analyzer = new ObjectAnalyzer();
        analyzer.analyzeImage(loadImage("plant027_rgb.png"), loadImage("plant027_rgb_class_watershed.png"));
        analyzer.writeResultsToCSVFile(new File("c:\\temp\\analyzer_output.csv"));
    }

    private ImagePlus loadImage(String name) throws IOException {
        File file = new File(this.getClass().getResource("/analyzer/" + name).getPath());
        ImagePlus image = IJ.openImage(file.getCanonicalPath());
        return image;
    }
}
