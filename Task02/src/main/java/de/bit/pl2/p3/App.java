package de.bit.pl2.p3;

import ij.ImagePlus;

import java.io.File;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // note all that naughty hardcoded stuff that needs to be changed into CLI in the end
        String inputDir = "C:\\Users\\regin\\Desktop\\testset";
        String outputDir = "C:\\Users\\regin\\Desktop\\output";

        // read images from a folder and store as ImagePlus objects in List
        ImageReader imageReader = new ImageReader();
        List<ImagePlus> imageList = imageReader.readFilesFromFolder(new File(inputDir));

        // apply Weka (from resources) classifier to images in list
        Weka weka = new Weka();
        List<ImagePlus> resultList = weka.applyClassifier(imageList);

        // find objects with watershed
        ObjectFinder objectFinder = new ObjectFinder();
        List<ImagePlus> objectList = objectFinder.parseList(resultList);

        // save images
        ImageSaver imageSaver = new ImageSaver();
        imageSaver.saveImages(objectList, new File(outputDir));
    }
}
