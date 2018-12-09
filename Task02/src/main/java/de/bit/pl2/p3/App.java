package de.bit.pl2.p3;

import ij.ImagePlus;
import trainableSegmentation.WekaSegmentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // note all that naughty hardcoded stuff
        ImagePlus image = new ImagePlus("C:\\Users\\regin\\Desktop\\plant015_rgb.png");
        ImagePlus labels = new ImagePlus("C:\\Users\\regin\\Desktop\\plant015_fg.png");
        String inputDir = "C:\\Users\\regin\\Desktop\\testset";
        String outputDir = "C:\\Users\\regin\\Desktop\\output";
        boolean train = false;

        // read images from a folder and store as ImagePlus objects in List
        ImageReader imageReader = new ImageReader();
        List<ImagePlus> imageList = imageReader.readFilesFromFolder(new File(inputDir));

        List<ImagePlus> resultList = new ArrayList<>();
        if (train) {
            // train classifier
            Weka weka = new Weka();
            WekaSegmentation seg = weka.trainClassifier(image, labels);

            // apply classifier to images in list
            resultList = weka.applyClassifier(imageList, seg);
        } else if (!train) {
            // apply classifier to images in list
            Weka weka = new Weka();
            resultList = weka.applyClassifier(imageList);
        }

        // save images in list
        ImageSaver imageSaver = new ImageSaver();
        imageSaver.saveLabels(resultList, new File(outputDir));


//        boolean output = false;
//        ObjectFinder objectFinder = new ObjectFinder();
//        List<ImagePlus> objectList = objectFinder.parseList(imageList, output);
//
//        if (output) {
//            ImageSaver imageSaver = new ImageSaver();
//            imageSaver.saveColorLabel(objectList, folder);
//        }
//        else {
//            for (ImagePlus imp : objectList) {
//                imp.show();
//            }
//        }


    }

}
